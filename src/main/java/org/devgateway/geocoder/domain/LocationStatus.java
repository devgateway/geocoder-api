package org.devgateway.geocoder.domain;

/**
 * Created by Sebastian Dimunzio on 11/15/2017.
 */
public enum LocationStatus {
    AUTO_CODED,                 // this location was added by the auto geocoder tool

    EXISTING,                   // this location was already in the IATI activity

    NEW,                        // this is a location that was added by the user

    UPDATED,                    // this is a location that was updated by the user

    DELETED                     // thi is a location that was deleted by the user
}
