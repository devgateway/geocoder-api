/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devgateway.geocoder.web;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Administrative;
import org.devgateway.geocoder.domain.GeographicFeatureDesignation;
import org.devgateway.geocoder.domain.GeographicLocationClass;
import org.devgateway.geocoder.domain.GeographicVocabulary;
import org.devgateway.geocoder.domain.Location;
import org.devgateway.geocoder.domain.LocationIdentifier;
import org.devgateway.geocoder.domain.LocationStatus;
import org.devgateway.geocoder.domain.auto.Extract;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.repositories.GeographicFeatureDesignationRepository;
import org.devgateway.geocoder.repositories.GeographicLocationClassRepository;
import org.devgateway.geocoder.repositories.GeographicVocabularyRepository;
import org.devgateway.geocoder.repositories.LocationRepository;
import org.devgateway.geocoder.repositories.auto.ExtractRepository;
import org.devgateway.geocoder.request.SearchRequest;
import org.devgateway.geocoder.service.CacheService;
import org.devgateway.geocoder.service.XmlImport;
import org.devgateway.geocoder.web.filterstate.ActivityFilterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@CacheConfig(keyGenerator = "genericKeyGenerator", cacheNames = "activityController")
public class ActivityController {
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    XmlImport xmlImport;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ExtractRepository extractRepository;

    @Autowired
    GeographicFeatureDesignationRepository geographicFeatureDesignationRepository;

    @Autowired
    GeographicLocationClassRepository geographicLocationClassRepository;

    @Autowired
    GeographicVocabularyRepository geographicVocabularyRepository;

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/import", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity importXmlFile(@RequestPart("file") final MultipartFile uploadfile,
                                        @RequestPart(name = "autoGeocode", required = false) final String autoGeocode) {
        final Boolean auto = Boolean.valueOf(autoGeocode);

        try {
            xmlImport.process(uploadfile.getInputStream(), "en", auto);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while importing file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while importing file");
        }
    }

    @Cacheable
    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public Page<Activity> getActivityLists(SearchRequest searchRequest) {
        final Pageable pageRequest = new PageRequest(searchRequest.getPage(), 10, Sort.Direction.ASC, "id");
        final ActivityFilterState activityFilterState = new ActivityFilterState(activityRepository, searchRequest);

        return activityRepository.findAll(activityFilterState.getSpecification(), pageRequest);
    }

    @Cacheable
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public Activity getActivityById(@PathVariable Long id) {
        return activityRepository.findOne(id);
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
    @Transactional
    public void saveActivity(@PathVariable Long id, @RequestBody Activity activity) {
        // fetch the activity that we want to save and delete all it's locations.
        // we will replace the locations with the ones received from the UI.
        final Activity newActivity = activityRepository.findOne(id);
        for(final Location location : newActivity.getLocations()) {
            List<Extract> extract = extractRepository.findByLocationId(location.getId());
            extractRepository.delete(extract);
        }
        locationRepository.delete(newActivity.getLocations());

        final List<Location> newLocations = new ArrayList<>();
        for(final Location location : activity.getLocations()) {
            if (location.getLocationStatus() != LocationStatus.DELETED) {
                location.setLocationStatus(LocationStatus.EXISTING);
                location.setActivity(newActivity);

                if (location.getAdministratives() != null && !location.getAdministratives().isEmpty()) {
                    for(final Administrative administrative : location.getAdministratives()) {
                        administrative.setLocation(location);

                        // find the `IatiCodes` and use what we have in the database instead of creating new entities.
                        if (administrative.getVocabulary() != null) {
                            final GeographicVocabulary vocabulary = geographicVocabularyRepository
                                    .findOneByCode(administrative.getVocabulary().getCode());
                            administrative.setVocabulary(vocabulary);
                        }
                    }
                }

                if (location.getLocationIdentifiers() != null && !location.getLocationIdentifiers().isEmpty()) {
                    for(final LocationIdentifier locationIdentifier : location.getLocationIdentifiers()) {
                        locationIdentifier.setLocation(location);
                    }
                }

                // find the `IatiCodes` and use what we have in the database instead of creating new entities.
                if (location.getFeaturesDesignation() != null) {
                    final GeographicFeatureDesignation featuresDesignation = geographicFeatureDesignationRepository
                            .findOneByCode(location.getFeaturesDesignation().getCode());
                    location.setFeaturesDesignation(featuresDesignation);
                }

                if (location.getLocationClass() != null) {
                    final GeographicLocationClass locationClass = geographicLocationClassRepository
                            .findOneByCode(location.getLocationClass().getCode());
                    location.setLocationClass(locationClass);
                }

                newLocations.add(location);
            }
        }

        newActivity.setLocations(newLocations);
        activityRepository.save(newActivity);

        // clear all the caches after we save the activity
        cacheService.clearAllCache();
    }
}
