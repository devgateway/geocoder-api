package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.Activity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by Sebastian Dimunzio on 10/23/2017.
 */
public class ActivitySpecifications {

    public static Specification<Activity> fieldContainsText(String field, String text) {
        return new Specification<Activity>() {
            public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                if (text != null && !text.isEmpty()) {
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), text.toLowerCase());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(Boolean.TRUE));
                }
            }
        };
    }

    public static Specification<Activity> descriptionContainsText(String text) {
        return fieldContainsText("description", text);
    }


    public static Specification<Activity> titleContainsText(String text) {
        return fieldContainsText("title", text);
    }

    public static Specification<Activity> xmlContainsText(String text) {
        return fieldContainsText("xml", text);
    }
}
