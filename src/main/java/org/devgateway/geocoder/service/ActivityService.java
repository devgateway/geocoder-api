package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.repositories.ActivitySearchRepository;
import org.devgateway.geocoder.request.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.logging.Logger;

/**
 * Created by Sebastian Dimunzio on 10/23/2017.
 */
@Component
public class ActivityService {
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivitySearchRepository activitySearchRepository;

    @Autowired
    IatiExtractors extractors;

    Unmarshaller jaxbUnmarshaller;

    public ActivityService() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivity.class);
            this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            log.warning("Error creating jaxbContext");
        }
    }

    public Page<Activity> findActivities(final SearchRequest request) {
        final Pageable pageRequest = new PageRequest(request.getPage(), 10, Sort.Direction.ASC, "id");
        final Page<Activity> activities = activitySearchRepository.findByXml(request, pageRequest);

        return activities;
    }

    public Activity getActivityById(final Long id, String lan) {
        if (lan == null) {
            lan = "en";
        }

        return activityRepository.findOne(id);
    }
}
