package org.devgateway.geocoder.web.filterstate;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Activity_;
import org.devgateway.geocoder.domain.LocationStatus;
import org.devgateway.geocoder.domain.Location_;
import org.devgateway.geocoder.domain.Narrative_;
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
                predicates.add(cb.or(cb.like(cb.lower(root.join(Activity_.titles).get(Narrative_.description)), "%" + text + "%"),
                        cb.like(cb.lower(root.join(Activity_.descriptions).get(Narrative_.description)), "%" + text + "%")));
            }

            if(searchRequest.getPendingVerification() != null && searchRequest.getPendingVerification()) {
                predicates.add(cb.equal(
                        root.join(Activity_.locations).get(Location_.locationStatus), LocationStatus.AUTO_CODED));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
