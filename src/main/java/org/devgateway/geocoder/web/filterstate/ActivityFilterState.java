package org.devgateway.geocoder.web.filterstate;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Activity_;
import org.devgateway.geocoder.domain.Country_;
import org.devgateway.geocoder.domain.LocationStatus;
import org.devgateway.geocoder.domain.Location_;
import org.devgateway.geocoder.domain.Narrative_;
import org.devgateway.geocoder.request.SearchRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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

            if (searchRequest.getText() != null && !searchRequest.getText().isEmpty()) {
                final String text = searchRequest.getText().toLowerCase();
                predicates.add(cb.or(cb.like(cb.lower(root.join(Activity_.titles).get(Narrative_.description)), "%" + text + "%"),
                        cb.like(cb.lower(root.join(Activity_.descriptions).get(Narrative_.description)), "%" + text + "%")));
            }

            if (searchRequest.getCountries() != null && !searchRequest.getCountries().isEmpty()) {
                predicates.add(root.join(Activity_.countries).get(Country_.iso2).in(searchRequest.getCountries()));
            }

            if (searchRequest.getYears() != null && !searchRequest.getYears().isEmpty()) {
                List<Predicate> yearPredicates = new ArrayList<>();
                for (int year : searchRequest.getYears()) {
                    Date dateAfter = new GregorianCalendar(year, 0, 1).getTime();
                    Date dateBefore = new GregorianCalendar(year, 11, 31).getTime();

                    yearPredicates.add(cb.and(
                            cb.greaterThanOrEqualTo(root.get(Activity_.date), dateAfter),
                            cb.lessThan(root.get(Activity_.date), dateBefore))
                    );
                }
                predicates.add(cb.or(yearPredicates.toArray(new Predicate[yearPredicates.size()])));
            }

            if (searchRequest.getPendingVerification() != null && searchRequest.getPendingVerification()) {
                predicates.add(cb.equal(
                        root.join(Activity_.locations).get(Location_.locationStatus), LocationStatus.AUTO_CODED));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
