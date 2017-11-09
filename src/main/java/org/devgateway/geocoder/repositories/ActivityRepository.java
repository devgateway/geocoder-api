package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian Dimunzio on 10/23/2017.
 */
public interface ActivityRepository extends JpaRepository<Activity, Long>, CrudRepository<Activity, Long> {


}
