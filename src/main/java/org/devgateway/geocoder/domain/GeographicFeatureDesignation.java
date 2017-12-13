package org.devgateway.geocoder.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Sebastian Dimunzio on 11/7/2017.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
