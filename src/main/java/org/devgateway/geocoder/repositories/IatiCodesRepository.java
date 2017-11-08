package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.IatiCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Generated by Spring Data Generator on 06/11/2017
*/
@NoRepositoryBean
public interface IatiCodesRepository<T> extends JpaRepository<T, Long> {

    public T findOneByCode(String code);

}
