package org.devgateway.geocoder.service;

import org.devgateway.geocoder.constants.Constants;
import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Administrative;
import org.devgateway.geocoder.domain.GeographicFeatureDesignation;
import org.devgateway.geocoder.domain.GeographicLocationClass;
import org.devgateway.geocoder.domain.GeographicVocabulary;
import org.devgateway.geocoder.domain.Location;
import org.devgateway.geocoder.domain.LocationIdentifier;
import org.devgateway.geocoder.domain.LocationStatus;
import org.devgateway.geocoder.iati.ActivitiesReader;
import org.devgateway.geocoder.iati.ActivityReader;
import org.devgateway.geocoder.iati.model.IatiActivities;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.repositories.GeographicFeatureDesignationRepository;
import org.devgateway.geocoder.repositories.GeographicLocationClassRepository;
import org.devgateway.geocoder.repositories.GeographicVocabularyRepository;
import org.devgateway.geocoder.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.StringWriter;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 11/01/2018
 */
@Service
public class ActivityService {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    GeographicFeatureDesignationRepository geographicFeatureDesignationRepository;

    @Autowired
    GeographicLocationClassRepository geographicLocationClassRepository;

    @Autowired
    GeographicVocabularyRepository geographicVocabularyRepository;

    @Autowired
    private IatiExtractors extractors;

    @Autowired
    private CacheService cacheService;

    @Transactional
    public void updateActivityLocations(final Long id, final Activity activity){
        // fetch the activity that we want to save and delete all it's locations.
        // we will replace the locations with the ones received from the UI.
        final Activity newActivity = activityRepository.findOne(id);

        final Set<Location> newLocations = new HashSet<>();
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


    @Transactional
    public void deleteActivity(final Long id){
        activityRepository.delete(id);

        // clear all the caches after we save the activity
        cacheService.clearAllCache();
    }

    public String generateXML(final List<Activity> activities) {
        final Activity activity = activityRepository.findByIdentifier("46002-P-SS-AA0-002").get(0);

        final ActivityReader activityReader = new ActivityReader(activity.getXml());
        final ActivitiesReader activitiesReader = new ActivitiesReader(null);

        final IatiActivities iatiActivities = new IatiActivities();
        iatiActivities.setVersion(Constants.IatiVersion.VERSION_202);

        final IatiActivity iatiActivity = activityReader.read();
        // replace locations
        final List<org.devgateway.geocoder.iati.model.Location> iatiLocations = activity.getLocations()
                .stream().map(location -> toIatiActivityLocation(location))
                .collect(Collectors.toList());
        iatiActivity.getLocation().clear();
        iatiActivity.getLocation().addAll(iatiLocations);

        iatiActivities.getIatiActivity().add(iatiActivity);

        try {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            final DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            iatiActivities.setGeneratedDatetime(datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
        } catch (DatatypeConfigurationException e) {
            logger.log(Level.SEVERE, "Error generating date", e);
        }


        final StringWriter writer = new StringWriter();
        activitiesReader.toXML(iatiActivities, writer);

        return writer.toString();
    }

    /**
     * Transform an {@link Location} into a {@link org.devgateway.geocoder.iati.model.Location}.
     */
    private org.devgateway.geocoder.iati.model.Location toIatiActivityLocation(final Location location) {
        final org.devgateway.geocoder.iati.model.Location iatiLocation = new org.devgateway.geocoder.iati.model.Location();

        if (location.getPoint() != null) {
            iatiLocation.setName(extractors.extractTexts(location.getNames()));

            iatiLocation.getLocationId().clear();
            iatiLocation.getLocationId().addAll(extractors.extractIdentifier(location.getLocationIdentifiers()));


            /*
            final Set<Administrative> administratives = extractors.getAdministratives(iatiLocation.getAdministrative());
            if (administratives != null) {
                administratives.forEach(administrative -> administrative.setLocation(location));
                location.setAdministratives(administratives);
            }
            final String[] latLong = iatiLocation.getPoint().getPos().split(" ");
            final Point pos = new GeometryFactory(new PrecisionModel(), 4326)
                    .createPoint(new Coordinate(Double.parseDouble(latLong[1]), Double.parseDouble(latLong[0])));
            location.setPoint(pos);


            location.setActivityDescriptions(narrativeToSet(extractors.getTexts(iatiLocation.getActivityDescription())));
            location.setDescriptions(narrativeToSet(extractors.getTexts(iatiLocation.getDescription())));
            if (iatiLocation.getLocationClass() != null && iatiLocation.getLocationClass().getCode() != null) {
                location.setLocationClass(this.geographicLocationClassRepository.findOneByCode(iatiLocation.getLocationClass().getCode()));
            }

            if (iatiLocation.getExactness() != null && iatiLocation.getExactness().getCode() != null) {
                location.setExactness(this.geographicExactnessRepository.findOneByCode(iatiLocation.getExactness().getCode()));
            }

            if (iatiLocation.getLocationClass() != null) {
                location.setLocationReach(this.geographicLocationReachRepository.findOneByCode(iatiLocation.getLocationClass().getCode()));
            }

            location.setLocationStatus(LocationStatus.EXISTING);

            // In some cases they put the name instead of code
            if (iatiLocation.getFeatureDesignation() != null && iatiLocation.getFeatureDesignation().getCode() != null) {
                final String fDesignation = iatiLocation.getFeatureDesignation().getCode();
                if (fDesignation != null && fDesignation.length() > 6) {
                    location.setFeaturesDesignation(this.geographicFeatureDesignationRepository.findOneByNameIgnoreCase(fDesignation));
                } else {
                    location.setFeaturesDesignation(this.geographicFeatureDesignationRepository.findOneByCode(fDesignation));
                }
            }*/
        }

        return iatiLocation;
    }
}
