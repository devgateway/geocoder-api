package org.devgateway.geocoder.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */

@Entity
@DiscriminatorValue("GAZETTEER_AGENCY")
public class GazetteerAgency extends IatiCodes {


}
