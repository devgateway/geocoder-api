package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Created by Sebastian Dimunzio on 11/8/2017.
 */
@Component
public class ActivitySearchRepositoryImp implements ActivitySearchRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<Activity> findByXml(SearchRequest request, Pageable pageable) {
        String sqlSelect = "select * from activity ";
        String sqlCount = "select count(*) from activity ";
        String sqlWhere = "where 1=1 ";
        String sqlPage = " limit :limit offset :offset";
        String sqlOrder = " order by :order :sort";

        Query selectQuery = null;
        Query countQuery = null;
        if (request.getText() != null && !request.getText().isEmpty()) {
            sqlWhere = sqlWhere.concat("and cast( xpath('//narrative/text()',xml) as text) ilike :text");
        }

        if (request.getDate() != null) {
            sqlWhere = sqlWhere.concat("where to_date(cast(((xpath('//activity-date[@type=1]/@iso-date',xml))[1]) as varchar),'YYYY-MM-DD') between :d1 and :d2 ");
        }

        if (request.getCountries() != null && request.getCountries().size() > 0) {
            sqlWhere = sqlWhere.concat("(cast(xpath('//recipient-country/@code ',xml) as varchar[])) @> cast( :codes  as varchar[])");
        }
        if (request.hasLocation() != null && request.hasLocation() == true) {
            sqlWhere = sqlWhere.concat(" and activity.id in ( select activity_id from location l where l.activity_id=activity.id) ");
        }
        if (request.hasLocation() != null && request.hasLocation() == false) {
            sqlWhere = sqlWhere.concat(" and activity.id not in ( select activity_id from location l where l.activity_id=activity.id) ");
        }

        selectQuery = em.createNativeQuery(sqlSelect + sqlWhere + sqlPage, Activity.class);
        countQuery = em.createNativeQuery(sqlCount + sqlWhere);

        if (request.getText() != null && !request.getText().isEmpty()) {
            selectQuery.setParameter("text", request.getText());
            countQuery.setParameter("text", request.getText());
        }
        if (request.getDate() != null) {
            selectQuery.setParameter("text", request.getText());
            countQuery.setParameter("text", request.getText());
        }
        if (request.getCountries() != null && request.getCountries().size() > 0) {
            selectQuery.setParameter("text", request.getText());
            countQuery.setParameter("text", request.getText());
        }


        selectQuery.setParameter("limit", pageable.getPageSize());
        selectQuery.setParameter("offset", pageable.getOffset());


        return new PageImpl<Activity>(selectQuery.getResultList(), pageable, ((BigInteger) countQuery.getSingleResult()).longValue());
    }
}
