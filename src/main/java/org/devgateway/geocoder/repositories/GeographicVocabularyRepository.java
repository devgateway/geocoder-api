package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.GeographicVocabulary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Transactional
public interface GeographicVocabularyRepository extends CrudRepository<GeographicVocabulary, Long>, IatiCodesRepository<GeographicVocabulary> {
}
