package org.devgateway.geocoder.geo;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;

import java.util.Map;

/**
 * Created by sebas on 9/2/2016.
 */
public class GeoJsonBuilder {

    private final FeatureCollection features;

    public GeoJsonBuilder() {
        this.features = new FeatureCollection();
    }

    public void add(final Feature feature) {
        this.features.add(feature);
    }

    public void add(final Map<String, Object> properties, final GeoJsonObject object) {
        this.add(createFeature(properties, object));
    }

    private Feature createFeature(final Map<String, Object> properties, final GeoJsonObject object) {
        Feature feature1 = new Feature();
        feature1.setGeometry(object);
        feature1.setProperties(properties);
        return feature1;
    }

    public FeatureCollection getFeatures() {
        return features;
    }
}

