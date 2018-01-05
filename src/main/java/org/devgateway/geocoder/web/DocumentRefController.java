package org.devgateway.geocoder.web;

import org.devgateway.geocoder.domain.auto.Extract;
import org.devgateway.geocoder.repositories.auto.ExtractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author idobre
 * @since 05/01/2018
 */
@RestController
@CrossOrigin
@CacheConfig(cacheNames = "documentRefController")
@Cacheable
public class DocumentRefController {
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ExtractRepository extractRepository;

    @RequestMapping(value = "/documentref/{locationId}", method = RequestMethod.GET)
    public List<Extract> getDocumentRefByLocationId(@PathVariable Long locationId) {
        return extractRepository.findByLocationId(locationId);
    }
}
