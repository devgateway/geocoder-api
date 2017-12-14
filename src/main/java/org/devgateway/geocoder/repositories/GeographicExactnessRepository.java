package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.GeographicExactness;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Repository
@Transactional
public interface GeographicExactnessRepository extends CrudRepository<GeographicExactness, Long>, IatiCodesRepository<GeographicExactness> {
}
