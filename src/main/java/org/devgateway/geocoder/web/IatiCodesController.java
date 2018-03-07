package org.devgateway.geocoder.web;

import org.devgateway.geocoder.domain.GeographicFeatureDesignation;
import org.devgateway.geocoder.repositories.GeographicExactnessRepository;
import org.devgateway.geocoder.repositories.GeographicFeatureDesignationRepository;
import org.devgateway.geocoder.repositories.GeographicLocationClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Sebastian Dimunzio
 */

@RestController
@CrossOrigin
@CacheConfig(cacheNames = "iatiCodes")
public class IatiCodesController {

    @Autowired
    GeographicFeatureDesignationRepository geographicFeatureDesignationRepository;

    @Autowired
    GeographicLocationClassRepository geographicLocationClassRepository;

    @Autowired
    GeographicExactnessRepository geographicExactnessRepository;

    @Cacheable
    @RequestMapping(value = "/codes/featureDesignation", method = RequestMethod.GET)
    public List<GeographicFeatureDesignation> getCountryList() {
        return geographicFeatureDesignationRepository.findAll();

    }
}
