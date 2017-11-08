package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.GeographicLocationClass;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
public interface GeographicLocationClassRepository extends CrudRepository<GeographicLocationClass, Long> , IatiCodesRepository<GeographicLocationClass>{
}
