package org.devgateway.geocoder.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 11/01/2018
 */
@Service
public class ActivityService {
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

    @Transactional
    public void updateActivityLocations(final Long id, final Activity activity){
        // fetch the activity that we want to save and delete all it's locations.
        // we will replace the locations with the ones received from the UI.
        final Activity newActivity = activityRepository.findOne(id);
        for (final Location location : newActivity.getLocations()) {
            List<Extract> extract = extractRepository.findByLocationId(location.getId());
            extractRepository.delete(extract);
        }

        final List<Location> newLocations = new ArrayList<>();
        for (final Location location : activity.getLocations()) {
            if (location.getLocationStatus() != LocationStatus.DELETED) {
                // don't update not verified locations
                if (location.getLocationStatus() != LocationStatus.AUTO_CODED) {
                    location.setLocationStatus(LocationStatus.EXISTING);
                }

                location.setActivity(newActivity);

                if (location.getAdministratives() != null && !location.getAdministratives().isEmpty()) {
                    for (final Administrative administrative : location.getAdministratives()) {
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
                    for (final LocationIdentifier locationIdentifier : location.getLocationIdentifiers()) {
                        locationIdentifier.setLocation(location);

                        // find the `IatiCodes` and use what we have in the database instead of creating new entities.
                        if (locationIdentifier.getVocabulary() != null) {
                            final GeographicVocabulary vocabulary = geographicVocabularyRepository
                                    .findOneByCode(locationIdentifier.getVocabulary().getCode());
                            locationIdentifier.setVocabulary(vocabulary);
                        }
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

        // replace old locations with the new ones
        newActivity.getLocations().clear();
        newActivity.getLocations().addAll(newLocations);
        activityRepository.save(newActivity);

        // clear all the caches after we save the activity
        cacheService.clearAllCache();
    }

}
