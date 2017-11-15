package org.devgateway.geocoder.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Sebastian Dimunzio on 11/7/2017.
 */
@Entity
@DiscriminatorValue("FEATURE_DESIGNATION")
public class GeographicFeatureDesignation extends IatiCodes {
    public GeographicFeatureDesignation() {
    }

    public GeographicFeatureDesignation(String code, String name, String description, String lan) {
        super(code, name, description, lan);
    }

    public GeographicFeatureDesignation(String code, String name) {
        super(code, name);
    }
}
