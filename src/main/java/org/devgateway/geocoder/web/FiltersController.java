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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author idobre
 * @since 14/12/2017
 */
@RestController
@CrossOrigin
@CacheConfig(cacheNames = "filtersController")
@Cacheable
public class FiltersController {
    private static final String COUNTRIES = "countries";
    private static final String YEARS = "years";

    @Autowired
    ActivityRepository activityRepository;

    /**
     * Return a {@link Map} with all filters used on project list page.
     */
    @RequestMapping(value = "/filters/all", method = RequestMethod.GET)
    public Map getAllFilters() {
        final Map allFilters = new HashMap();

        allFilters.put(COUNTRIES, getCounries());
        allFilters.put(YEARS, getYears());

        return allFilters;
    }

    /**
     * Return a distinct list of all activities countries.
     */
    @RequestMapping(value = "/filters/countries", method = RequestMethod.GET)
    public List<Country> getCounries() {
        return activityRepository.findDistinctCountries();
    }

    /**
     * Return a distinct list of all activities years.
     */
    @RequestMapping(value = "/filters/years", method = RequestMethod.GET)
    public List<Integer> getYears() {
        return activityRepository.findDistinctYears();
    }
}
