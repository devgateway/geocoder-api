package org.devgateway.geocoder.web;

import org.devgateway.geocoder.domain.Country;
import org.devgateway.geocoder.request.BoundaryRequest;
import org.devgateway.geocoder.service.BoundariesService;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@CacheConfig(keyGenerator = "genericKeyGenerator", cacheNames = "boundariesController")
@Cacheable
public class BoundariesController {


    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    BoundariesService boundariesService;

    @RequestMapping(value = "/boundary", method = RequestMethod.GET)
    public FeatureCollection getBoundary(BoundaryRequest request) {
        return boundariesService.getBoundariesGeoJson(request.getCodes(), 0.001);

    }


    @RequestMapping(value = "/boundaries/list", method = RequestMethod.GET)
    public List<Country> getCountryList() {

        return boundariesService.getCountryList();

    }

}
