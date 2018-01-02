package org.devgateway.geocoder.repositories;


import com.vividsolutions.jts.geom.Geometry;
import org.devgateway.geocoder.domain.Boundary;
import org.devgateway.geocoder.domain.Boundary_;
import org.devgateway.geocoder.repositories.helpers.BoundaryWrapper;
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


    public List<BoundaryWrapper> getBoundaries(List<String> isoCodes, Double simplifyFactor) {

        List<Selection<?>> multiSelect = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery(BoundaryWrapper.class);
        Root<Boundary> boundaryRoot = criteriaQuery.from(Boundary.class);

        ParameterExpression<Double> factorParam = criteriaBuilder.parameter(Double.class, "factor");

        Expression function = criteriaBuilder.function("simplifyPreserve", Geometry.class, boundaryRoot.get(Boundary_.geometry), factorParam);

        multiSelect.add(boundaryRoot.get(Boundary_.gid));
        multiSelect.add(boundaryRoot.get(Boundary_.iso));
        multiSelect.add(boundaryRoot.get(Boundary_.id_0));
        multiSelect.add(boundaryRoot.get(Boundary_.name_0));
        multiSelect.add(boundaryRoot.get(Boundary_.id_1));
        multiSelect.add(boundaryRoot.get(Boundary_.name_1));
        multiSelect.add(boundaryRoot.get(Boundary_.id_2));
        multiSelect.add(boundaryRoot.get(Boundary_.name_2));
        multiSelect.add(function);


        Expression<String> exp = boundaryRoot.get(Boundary_.iso);
        Predicate predicate = exp.in(isoCodes);
        predicates.add(predicate);

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<BoundaryWrapper> query = em.createQuery(criteriaQuery.multiselect(multiSelect));
        query.setParameter("factor", simplifyFactor);

        return query.getResultList();


    }
}
