package org.devgateway.geocoder.web;

import org.devgateway.geocoder.domain.Country;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author idobre
 * @since 14/12/2017
 */
@RestController
@CrossOrigin
@CacheConfig(cacheNames = "filtersController")
@Cacheable
public class FiltersController {
    @Autowired
    ActivityRepository activityRepository;

    @RequestMapping(value = "/filters/countries", method = RequestMethod.GET)
    public List<Country> getCounries() {
        return activityRepository.findDistinctCountries();
    }
}
