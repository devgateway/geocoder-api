package org.devgateway.geocoder.repositories.auto;

import org.devgateway.geocoder.domain.auto.Extract;
import org.devgateway.geocoder.repositories.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 05/01/2018
 */
@Repository
@Transactional
public interface ExtractRepository extends BaseJpaRepository<Extract, Long> {

    List<Extract> findByLocationId(Long id);
}
