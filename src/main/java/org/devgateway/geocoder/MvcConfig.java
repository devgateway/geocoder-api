package org.devgateway.geocoder;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.devgateway.geocoder.web.generators.GenericKeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Value("${geocoder.ui.path}")
    private String uiPath;

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:index.html");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (uiPath != null) {
            registry.addResourceHandler("/**").addResourceLocations("file:" + uiPath);
        }
    }

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }

    @Bean(name = "genericKeyGenerator")
    public KeyGenerator genericKeyGenerator(final ObjectMapper objectMapper) {
        return new GenericKeyGenerator(objectMapper);
    }
}