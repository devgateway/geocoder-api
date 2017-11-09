package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.iati.model.ActivityDate;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.iati.model.Narrative;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.repositories.ActivitySearchRepository;
import org.devgateway.geocoder.repositories.CountryRepository;
import org.devgateway.geocoder.responses.ActivityResponse;
import org.devgateway.geocoder.responses.CountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public Page<ActivityResponse> findActivities(HashMap<String, Object> params, Integer page, String lan) {
        if (page == null) {
            page = 0;
        }
        Pageable pageRequest = createPageRequest(page);
        Page<Activity> activities = activitySearchRepository.findByXml(params, pageRequest);

        Page<ActivityResponse> activityResponses = activities.map(activity -> toResponse(activity.getId(), fromXML(activity.getXml()), lan));


        return activityResponses;
    }

    public ActivityResponse toResponse(Long id, IatiActivity activity, String lan) {
        ActivityResponse response = new ActivityResponse();
        org.devgateway.geocoder.iati.model.Narrative title = activity.getTitle().getNarrative().stream().filter(narrative -> lan.equalsIgnoreCase(narrative.getLang())).collect(Collectors.toList()).iterator().next();
        response.setId(id);
        response.setTitle(extractors.getTitle(activity.getTitle(), lan));
        response.setIdentifier(activity.getIatiIdentifier().getValue());
        /*
        1	Planned start
        2	Actual start
        3	Planned End
        4	Actual end
        */

        response.setDate(extractors.getDate(activity, "1"));
        response.setCountries(extractors.getCountries(activity, lan));

        /*code	name
        1	General
        2	Objectives
        3	Target Groups
        4	Other
         */
        response.setDescription(extractors.getDescription(activity, "1", lan));


        return response;
    }

    public IatiActivity fromXML(String xml) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivity.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            IatiActivity activity = (IatiActivity) jaxbUnmarshaller.unmarshal(new StringReader(xml));

            return activity;

        } catch (JAXBException e) {
            log.warning("Error when reading activities");
        }
        return null;

    }

    public Activity getActivityById(Long id) {
        return activityRepository.findOne(id);
    }

    private Pageable createPageRequest(Integer page) {
        return new PageRequest(page, 10, Sort.Direction.ASC, "id");
    }
}
