package org.devgateway.geocoder.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Entity
@DiscriminatorValue("LOCATION_PRECISION")
public class GeographicalPrecision extends IatiCodes {
}
