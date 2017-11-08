package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sebastian Dimunzio on 10/23/2017.
 */
@Component
public class ActivityService {
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ActivityRepository activityRepository;


    public IatiActivity fromXMl(String xml) {
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
        return new PageRequest(page, 10, Sort.Direction.ASC, "title");
    }
}
