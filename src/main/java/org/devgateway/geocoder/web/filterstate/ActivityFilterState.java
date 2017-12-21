package org.devgateway.geocoder.web.filterstate;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Activity_;
import org.devgateway.geocoder.domain.Country_;
import org.devgateway.geocoder.domain.LocationStatus;
import org.devgateway.geocoder.domain.Location_;
import org.devgateway.geocoder.domain.Narrative_;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.request.SearchRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 14/12/2017
 */
public class ActivityFilterState implements Serializable {
    private final ActivityRepository activityRepository;

    private final SearchRequest searchRequest;


    public ActivityFilterState(final ActivityRepository activityRepository, final SearchRequest searchRequest) {
        this.activityRepository = activityRepository;
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

            if (searchRequest.getWithNoLocation() != null && searchRequest.getWithNoLocation()) {
                predicates.add(cb.isEmpty(root.get(Activity_.locations)));
            }

            if (searchRequest.getPendingVerification() != null && searchRequest.getPendingVerification()) {
                predicates.add(cb.equal(
                        root.join(Activity_.locations).get(Location_.locationStatus), LocationStatus.AUTO_CODED));
            }

            if (searchRequest.getVerifiedLocation() != null && searchRequest.getVerifiedLocation()) {
                // get the IDs of the activities with "AUTO_CODED" status.
                final SearchRequest searchRequest = new SearchRequest();
                searchRequest.setPendingVerification(true);
                final ActivityFilterState activityFilterState = new ActivityFilterState(activityRepository, searchRequest);
                final List<Long> IDs = activityRepository.findAll(activityFilterState.getSpecification())
                        .stream()
                        .map(activity -> activity.getId())
                        .collect(Collectors.toList());

                predicates.add(root.get(Activity_.id).in(IDs).not());
                predicates.add(cb.isNotEmpty(root.get(Activity_.locations)));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
