package org.devgateway.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.devgateway.geocoder.constants.Constants;
import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Administrative;
import org.devgateway.geocoder.domain.Location;
import org.devgateway.geocoder.domain.LocationIdentifier;
import org.devgateway.geocoder.domain.LocationStatus;
import org.devgateway.geocoder.domain.auto.ActivityQueue;
import org.devgateway.geocoder.iati.ActivitiesReader;
import org.devgateway.geocoder.iati.model.IatiActivities;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.repositories.ActivityQueueRepository;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.repositories.GeographicExactnessRepository;
import org.devgateway.geocoder.repositories.GeographicFeatureDesignationRepository;
import org.devgateway.geocoder.repositories.GeographicLocationClassRepository;
import org.devgateway.geocoder.repositories.GeographicLocationReachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */

@Component("XmlImport")
@Transactional
public class XmlImport {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private IatiExtractors extractors;

    @Autowired
    private GeographicExactnessRepository geographicExactnessRepository;

    @Autowired
    private GeographicLocationClassRepository geographicLocationClassRepository;

    @Autowired
    private GeographicLocationReachRepository geographicLocationReachRepository;

    @Autowired
    private GeographicFeatureDesignationRepository geographicFeatureDesignationRepository;

    @Autowired
    private ActivityQueueRepository activityQueueRepository;

    @Autowired
    private CacheService cacheService;


    public List<String> process(final File in, final Boolean autoGeocode, final Boolean autoGeocodeWithoutLoc) {
        final ActivitiesReader reader = new ActivitiesReader(in);

        ArrayList<String> validations = reader.validate();


        if (validations.size() == 0) {

            final IatiActivities activities = reader.read();

            activities.getIatiActivity().stream().forEach(iatiActivity -> {
                final Activity activity = iatiActivityToActivityEntity(iatiActivity, reader);
                activityRepository.save(activity);

                if (autoGeocode) {
                    addQueue(activity);
                } else {
                    if (autoGeocodeWithoutLoc && (activity.getLocations() != null && activity.getLocations().isEmpty())) {
                        addQueue(activity);
                    }
                }
            });
            // clear all the caches after we finish the import
            cacheService.clearAllCache();
            return new ArrayList<>();
        } else {
            //if not valid return validation error of most recent version
            return validations;
        }

    }

    /**
     * Add an activity to {@link org.devgateway.geocoder.domain.auto.Queue}.
     *
     * @param activity
     */
    private void addQueue(final Activity activity) {
        final ActivityQueue activityQueue = new ActivityQueue();

        activityQueue.setActivity(activity);
        activityQueue.setCreateDate(new Date());
        activityQueue.setState(Constants.ST_PENDING);
        activityQueueRepository.save(activityQueue);
    }

    /**
     * Transform an {@link IatiActivity} into a {@link Activity}.
     */
    private Activity iatiActivityToActivityEntity(final IatiActivity iatiActivity, final ActivitiesReader reader) {
        final Activity activity = new Activity();

        activity.setIdentifier(iatiActivity.getIatiIdentifier().getValue());

        activity.setTitles(narrativeToSet(extractors.getTitles(iatiActivity.getTitle())));

        activity.setDescriptions(narrativeToSet(extractors.getDescriptions(iatiActivity)));

        activity.setDate(extractors.getDate(iatiActivity, "1"));

        if (iatiActivity.getLocation().size() > 0) {
            final Set<Location> activityLocations = iatiActivity.getLocation()
                    .stream().map(location -> toActivityLocation(location)).filter(location -> location != null)
                    .collect(Collectors.toSet());

            activityLocations.forEach(location -> location.setActivity(activity));
            activity.setLocations(activityLocations);
        }

        activity.setCountries(new HashSet<>(extractors.getCountries(iatiActivity)));


        // also keep the original XML
        final StringWriter writer = new StringWriter();
        reader.toXML(iatiActivity, writer);
        activity.setXml(writer.toString());

        return activity;
    }


    /**
     * Transform an {@link org.devgateway.geocoder.iati.model.Location} into a {@link Location}.
     */
    private Location toActivityLocation(final org.devgateway.geocoder.iati.model.Location iatiLocation) {
        if (iatiLocation.getPoint() != null) {
            final Location location = new Location();

            location.setNames(narrativeToSet(extractors.getTexts(iatiLocation.getName())));

            final List<LocationIdentifier> identifiers = extractors.getIdentifier(iatiLocation.getLocationId());

            if (identifiers != null) {
                identifiers.forEach(locationIdentifier -> locationIdentifier.setLocation(location));
                location.setLocationIdentifiers(identifiers);
            }

            final List<Administrative> administratives = extractors.getAdministratives(iatiLocation.getAdministrative());
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
            }

            return location;
        } else {
            return null;
        }
    }

    protected HashSet<org.devgateway.geocoder.domain.Narrative> narrativeToSet(
            final List<org.devgateway.geocoder.domain.Narrative> list) {
        if (list == null) {
            return null;
        }

        return new HashSet<>(list);
    }
}
