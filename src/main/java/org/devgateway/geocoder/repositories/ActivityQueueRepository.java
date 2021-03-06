package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.auto.ActivityQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sebastian Dimunzio on 10/23/2017.
 */
@Repository
@Transactional
public interface ActivityQueueRepository extends JpaRepository<ActivityQueue, Long>, CrudRepository<ActivityQueue, Long> {


}
