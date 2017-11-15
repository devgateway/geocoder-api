package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.GeographicFeatureDesignation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Repository
public interface GeographicFeatureDesignationRepository extends CrudRepository<GeographicFeatureDesignation, Long>, IatiCodesRepository<GeographicFeatureDesignation> {
}
