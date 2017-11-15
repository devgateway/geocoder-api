package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * Created by Sebastian Dimunzio on 11/8/2017.
 */
@Repository
public interface ActivitySearchRepository {

    Page<Activity> findByXml(SearchRequest params, Pageable pageable);
}
