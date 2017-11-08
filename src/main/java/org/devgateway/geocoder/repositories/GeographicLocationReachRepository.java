package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.GeographicLocationReach;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
public interface GeographicLocationReachRepository extends CrudRepository<GeographicLocationReach, Long>, IatiCodesRepository<GeographicLocationReach> {
}
