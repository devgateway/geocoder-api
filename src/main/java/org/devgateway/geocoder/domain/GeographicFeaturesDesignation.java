package org.devgateway.geocoder.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Sebastian Dimunzio on 11/7/2017.
 */
@Entity
@DiscriminatorValue("FEATURE_DESIGNATION")
public class GeographicFeaturesDesignation extends IatiCodes {
}
