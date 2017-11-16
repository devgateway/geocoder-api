package org.devgateway.geocoder.repositories.auto;

import org.devgateway.geocoder.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Sebastian Dimunzio on 10/23/2017.
 */
public interface ActivityQueue extends JpaRepository<ActivityQueue, Long>, CrudRepository<ActivityQueue, Long> {


}
