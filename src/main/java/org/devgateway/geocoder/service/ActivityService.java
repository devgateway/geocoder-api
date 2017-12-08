package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.repositories.ActivitySearchRepository;
import org.devgateway.geocoder.request.SearchRequest;
import org.devgateway.geocoder.responses.ActivityResponse;
import org.devgateway.geocoder.responses.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
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

    Unmarshaller jaxbUnmarshaller;

    public ActivityService() {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivity.class);
            this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            log.warning("Error creating jaxbContext");
        }
    }


    public Page<ActivityResponse> findActivities(SearchRequest request) {
        final Pageable pageRequest = new PageRequest(request.getPage(), 10, Sort.Direction.ASC, "id");
        final Page<Activity> activities = activitySearchRepository.findByXml(request, pageRequest);
        final Page<ActivityResponse> activityResponses = activities
                .map(activity -> toResponse(activity, fromXML(activity.getXml()), request.getLan()));

        return activityResponses;
    }

    public ActivityResponse toResponse(final Activity activity, final IatiActivity IatiActivity, final String lan) {
        ActivityResponse response = new ActivityResponse();

        response.setId(activity.getId());
        response.setTitle(extractors.getTitle(IatiActivity.getTitle(), lan));
        response.setIdentifier(IatiActivity.getIatiIdentifier().getValue());
        /*
        1	Planned start
        2	Actual start
        3	Planned End
        4	Actual end
        */

        response.setDate(extractors.getDate(IatiActivity, "1"));
        response.setCountries(extractors.getCountries(IatiActivity, lan));

        /*code	name
        1 General
        2 Objectives
        3 Target Groups
        4 Other
         */
        response.setDescription(extractors.getDescription(IatiActivity, "1", lan));
        if (response.getDescription() == null) {
            response.setDescription(extractors.getDescription(IatiActivity, "2", lan));

        }
        if (response.getDescription() == null) {
            response.setDescription(extractors.getDescription(IatiActivity, "3", lan));

        }
        if (response.getDescription() == null) {
            response.setDescription(extractors.getDescription(IatiActivity, "4", lan));

        }

        response.setLocations(activity.getLocations().stream().map(location -> new LocationResponse(location, lan)).collect(Collectors.toList()));

        return response;
    }


    public IatiActivity fromXML(String xml) {
        try {

            IatiActivity activity = (IatiActivity) jaxbUnmarshaller.unmarshal(new StringReader(xml));

            return activity;

        } catch (JAXBException e) {
            log.warning("Error when reading activities");
        }
        return null;

    }

    public ActivityResponse getActivityById(Long id, String lan) {
        if (lan == null) {
            lan = "en";
        }
        Activity activity = activityRepository.findOne(id);
        return toResponse(activity, fromXML(activity.getXml()), lan);

    }
}
