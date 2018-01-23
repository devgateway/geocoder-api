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
import org.devgateway.geocoder.request.SearchRequest;
import org.devgateway.geocoder.web.filterstate.ActivityFilterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(keyGenerator = "genericKeyGenerator", cacheNames = "activityService")
public class ActivityService {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String SRSNAME = "http://www.opengis.net/def/crs/EPSG/0/4326";

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
    private ActivityReader activityReader;

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

    /**
     * Generates a XML String representation of the activities received as parameter.
     */
    @Cacheable
    public String generateXML(final SearchRequest searchRequest) {
        final ActivityFilterState activityFilterState = new ActivityFilterState(activityRepository, searchRequest);
        final List<Activity> activities = activityRepository.findAll(activityFilterState.getSpecification());

        final ActivitiesReader activitiesReader = new ActivitiesReader(null);

        final IatiActivities iatiActivities = new IatiActivities();
        iatiActivities.setVersion(Constants.IatiVersion.VERSION_202);
        try {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            final DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            iatiActivities.setGeneratedDatetime(datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
        } catch (DatatypeConfigurationException e) {
            logger.log(Level.SEVERE, "Error generating date", e);
        }

        activities.stream().forEach(activity -> {
            final IatiActivity iatiActivity = activityReader.read(activity.getXml());

            // replace locations
            final List<org.devgateway.geocoder.iati.model.Location> iatiLocations = activity.getLocations()
                    .stream()
                    .filter(location -> location.getLocationStatus() != LocationStatus.AUTO_CODED)
                    .map(location -> toIatiActivityLocation(location))
                    .collect(Collectors.toList());
            iatiActivity.getLocation().clear();
            iatiActivity.getLocation().addAll(iatiLocations);

            iatiActivities.getIatiActivity().add(iatiActivity);
        });

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
            iatiLocation.setDescription(extractors.extractTexts(location.getDescriptions()));
            iatiLocation.setActivityDescription(extractors.extractTexts(location.getActivityDescriptions()));

            final List<org.devgateway.geocoder.iati.model.Location.LocationId> identifier = extractors.extractIdentifier(location.getLocationIdentifiers());
            if (identifier != null) {
                iatiLocation.getLocationId().clear();
                iatiLocation.getLocationId().addAll(identifier);
            }

            final List<org.devgateway.geocoder.iati.model.Location.Administrative> administratives = extractors.extractAdministratives(location.getAdministratives());
            if (administratives != null) {
                iatiLocation.getAdministrative().clear();
                iatiLocation.getAdministrative().addAll(administratives);
            }

            final org.devgateway.geocoder.iati.model.Location.Point iatiPoint =
                    new org.devgateway.geocoder.iati.model.Location.Point();
            iatiPoint.setPos(location.getPoint().getY() + " " + location.getPoint().getX());
            iatiPoint.setSrsName(SRSNAME);
            iatiLocation.setPoint(iatiPoint);

            if (location.getLocationClass() != null) {
                final org.devgateway.geocoder.iati.model.Location.LocationClass iatiLocationClass =
                        new org.devgateway.geocoder.iati.model.Location.LocationClass();
                iatiLocationClass.setCode(location.getLocationClass().getCode());
                iatiLocation.setLocationClass(iatiLocationClass);
            }

            if (location.getExactness() != null) {
                final org.devgateway.geocoder.iati.model.Location.Exactness iatiExactness =
                        new org.devgateway.geocoder.iati.model.Location.Exactness();
                iatiExactness.setCode(location.getExactness().getCode());
                iatiLocation.setExactness(iatiExactness);
            }

            if (location.getLocationReach() != null) {
                final org.devgateway.geocoder.iati.model.Location.LocationReach iatiLocationReach =
                        new org.devgateway.geocoder.iati.model.Location.LocationReach();
                iatiLocationReach.setCode(location.getLocationReach().getCode());
                iatiLocation.setLocationReach(iatiLocationReach);
            }

            if (location.getFeaturesDesignation() != null) {
                final org.devgateway.geocoder.iati.model.Location.FeatureDesignation iatiFeatureDesignation =
                        new org.devgateway.geocoder.iati.model.Location.FeatureDesignation();
                iatiFeatureDesignation.setCode(location.getFeaturesDesignation().getCode());
                iatiLocation.setFeatureDesignation(iatiFeatureDesignation);
            }
        }

        return iatiLocation;
    }
}
