package org.devgateway.geocoder.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Entity
@DiscriminatorValue("EXACTNESS")
public class GeographicExactness extends IatiCodes {

}
