package org.devgateway.geocoder.web.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author idobre
 * @since 21/12/17
 *
 * {@link KeyGenerator} that uses all parameters to create a key.
 */
public class GenericKeyGenerator implements KeyGenerator {
    private final Logger logger = LoggerFactory.getLogger(GenericKeyGenerator.class);

    private final ObjectMapper objectMapper;

    public GenericKeyGenerator(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object generate(final Object target, final Method method, final Object... params) {
        try {
            StringBuilder key = new StringBuilder(method.toString());
            for (Object param : params) {
                key.append(objectMapper.writeValueAsString(param));
            }

            return key.toString().hashCode();
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
