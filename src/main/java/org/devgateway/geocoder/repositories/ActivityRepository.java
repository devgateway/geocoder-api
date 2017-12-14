package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian Dimunzio on 10/23/2017.
 */
@Transactional
public interface ActivityRepository extends JpaRepository<Activity, Long>, CrudRepository<Activity, Long> {

    @Query("SELECT DISTINCT countries FROM #{#entityName}")
    List<Country> findDistinctCountries();

    @Query("SELECT DISTINCT EXTRACT(YEAR FROM date) FROM #{#entityName} WHERE date IS NOT NULL")
    List<Integer> findDistinctYears();
}
