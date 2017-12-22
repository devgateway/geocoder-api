package org.devgateway.geocoder.service;

import com.sun.org.apache.xalan.internal.utils.FeatureManager;
import org.devgateway.geocoder.domain.Boundary;
import org.devgateway.geocoder.geo.GeoJsonBuilder;
import org.devgateway.geocoder.geo.GeoJsonUtils;
import org.devgateway.geocoder.repositories.BoundaryRepository;
import org.devgateway.geocoder.repositories.helpers.BoundaryDao;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoundariesService {

    @Autowired
    BoundaryRepository boundaryRepository;


    public FeatureCollection getBoundariesGeoJson(final List<String> isoCodes, final Double simplifyFactor) {

        List<BoundaryDao> results = boundaryRepository.getBoundaries(isoCodes, simplifyFactor);

        GeoJsonBuilder geoJsonBuilder = new GeoJsonBuilder();

        results.stream().forEach(boundaryInstance -> geoJsonBuilder.add(boundaryToMap(boundaryInstance), GeoJsonUtils.jtsGeometryToGeoJson(boundaryInstance.getGeometry())));

        return geoJsonBuilder.getFeatures();


    }


    private HashMap<String, Object> boundaryToMap(final BoundaryDao instance) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("ID", instance.getId());
        return map;
    }
}
