package org.devgateway.geocoder.repositories;


import com.vividsolutions.jts.geom.Geometry;
import org.devgateway.geocoder.domain.Boundary;
import org.devgateway.geocoder.domain.Boundary_;
import org.devgateway.geocoder.repositories.helpers.BoundaryDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class BoundaryRepository {

    @PersistenceContext
    private EntityManager em;


    public List<BoundaryDao> getBoundaries(List<String> isoCodes, Double simplifyFactor) {

        List<Selection<?>> multiSelect = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery(BoundaryDao.class);
        Root<Boundary> boundaryRoot = criteriaQuery.from(Boundary.class);

        ParameterExpression<Double> factorParam = criteriaBuilder.parameter(Double.class, "factor");

        Expression function = criteriaBuilder.function("simplify", Geometry.class,boundaryRoot.get(Boundary_.geometry),factorParam);

        multiSelect.add(boundaryRoot.get(Boundary_.gid));
        multiSelect.add(function);

        Expression<String> exp = boundaryRoot.get(Boundary_.iso);
        Predicate predicate = exp.in(isoCodes);
        predicates.add(predicate);

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<BoundaryDao> query = em.createQuery(criteriaQuery.multiselect(multiSelect));
        query.setParameter("factor", simplifyFactor);

        return query.getResultList();


    }
}
