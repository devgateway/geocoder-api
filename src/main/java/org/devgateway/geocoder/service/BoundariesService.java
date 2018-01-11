package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Country;
import org.devgateway.geocoder.geo.GeoJsonBuilder;
import org.devgateway.geocoder.geo.GeoJsonUtils;
import org.devgateway.geocoder.repositories.BoundaryRepository;
import org.devgateway.geocoder.repositories.CountryRepository;
import org.devgateway.geocoder.repositories.helpers.BoundaryWrapper;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoundariesService {

    @Autowired
    BoundaryRepository boundaryRepository;

    @Autowired
    CountryRepository countryRepository;


    public FeatureCollection getBoundariesGeoJson(final List<String> isoCodes, final Double simplifyFactor) {

        List<BoundaryWrapper> results = boundaryRepository.getBoundaries(isoCodes, simplifyFactor);

        GeoJsonBuilder geoJsonBuilder = new GeoJsonBuilder();

        results.stream().forEach(boundaryInstance -> geoJsonBuilder.add(boundaryToMap(boundaryInstance), GeoJsonUtils.jtsGeometryToGeoJson(boundaryInstance.getGeometry())));

        return geoJsonBuilder.getFeatures();
    }


    private HashMap<String, Object> boundaryToMap(final BoundaryWrapper instance) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("ID", instance.getGid());
        map.put("ADMIN_0_CODE", instance.getAdmin0Code());
        map.put("ADMIN_1_CODE", instance.getAdmin1Code());
        map.put("ADMIN_2_CODE", instance.getAdmin2Code());
        map.put("ADMIN_0_NAME", instance.getAdmin0Name());
        map.put("ADMIN_1_NAME", instance.getAdmin1Name());
        map.put("ADMIN_2_NAME", instance.getAdmin2Name());
        map.put("ISO", instance.getIso());

        return map;
    }


    public List<Country> getCountryList() {
        return countryRepository.findAll();
    }
}
