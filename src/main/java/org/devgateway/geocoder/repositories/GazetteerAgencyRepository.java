package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.GazetteerAgency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Repository
@Transactional
public interface GazetteerAgencyRepository extends CrudRepository<GazetteerAgency, Long>, IatiCodesRepository<GazetteerAgency> {
}
