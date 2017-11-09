package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.Activity;
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

    public Page<Activity> findByXml(HashMap<String, Object> params, Pageable pageable) {
        String sqlSelect = "select * from activity ";
        String sqlCount = "select count(*) from activity ";
        String sqlWhere = "where 1=1 ";
        String sqlPage = " limit :limit offset :offset";
        String sqlOrder = " order by :order :sort";

        Query selectQuery = null;
        Query countQuery = null;
        if (params.containsKey("text")) {
            sqlWhere = sqlWhere.concat("and cast( xpath('//narrative/text()',xml) as text) ilike :text");
        }

        if (params.containsKey("date")) {
            sqlWhere = sqlWhere.concat("where to_date(cast(((xpath('//activity-date[@type=1]/@iso-date',xml))[1]) as varchar),'YYYY-MM-DD') between :d1 and :d2 ");
        }

        if (params.containsKey("country")) {
            sqlWhere = sqlWhere.concat("(cast(xpath('//recipient-country/@code ',xml) as varchar[])) @> cast( :codes  as varchar[])");
        }

        selectQuery = em.createNativeQuery(sqlSelect + sqlWhere + sqlPage, Activity.class);
        countQuery = em.createNativeQuery(sqlCount + sqlWhere);

        if (params.containsKey("text")) {
            selectQuery.setParameter("text", params.get("text"));
            countQuery.setParameter("text", params.get("text"));
        }
        if (params.containsKey("date")) {
            selectQuery.setParameter("text", params.get("text"));
            countQuery.setParameter("text", params.get("text"));
        }
        if (params.containsKey("country")) {
            selectQuery.setParameter("text", params.get("text"));
            countQuery.setParameter("text", params.get("text"));
        }

        selectQuery.setParameter("limit", pageable.getPageSize());
        selectQuery.setParameter("offset", pageable.getOffset());


        return new PageImpl<Activity>(selectQuery.getResultList(), pageable, ((BigInteger) countQuery.getSingleResult()).longValue());
    }
}
