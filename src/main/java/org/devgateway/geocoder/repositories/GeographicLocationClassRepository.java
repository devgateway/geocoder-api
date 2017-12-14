package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.GeographicLocationClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Transactional
public interface GeographicLocationClassRepository extends CrudRepository<GeographicLocationClass, Long> , IatiCodesRepository<GeographicLocationClass>{
}
