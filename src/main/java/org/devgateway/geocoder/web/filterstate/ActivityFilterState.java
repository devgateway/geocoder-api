package org.devgateway.geocoder.web.filterstate;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Activity_;
import org.devgateway.geocoder.request.SearchRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 14/12/2017
 */
public class ActivityFilterState implements Serializable {
    private final SearchRequest searchRequest;


    public ActivityFilterState(final SearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }


    public Specification<Activity> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(searchRequest.getText() != null && !searchRequest.getText().isEmpty()) {
                final String text = searchRequest.getText().toLowerCase();
                // predicates.add(cb.like(root.get(Activity_.title), "%" + searchRequest.getText() + "%"));
                predicates.add(cb.or(cb.like(cb.lower(root.get(Activity_.title)), "%" + text + "%"),
                        cb.like(cb.lower(root.get(Activity_.description)), "%" + text + "%")));
            }


            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
