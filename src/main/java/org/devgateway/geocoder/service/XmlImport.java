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
import org.devgateway.geocoder.repositories.GeographicVocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
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
    private GeographicVocabularyRepository geographicVocabularyRepository;


    @Autowired
    private GeographicFeatureDesignationRepository geographicFeatureDesignationRepository;

    @Autowired
    private ActivityQueueRepository activityQueueRepository;

    public void process(final InputStream in, final String lan, final Boolean autocode) {
        final ActivitiesReader reader = new ActivitiesReader(in);
        final IatiActivities activities = reader.read();

        for (IatiActivity activity : activities.getIatiActivity()) {
            final Activity newActivity = new Activity();
            newActivity.setIdentifier(activity.getIatiIdentifier().getValue());

            if (activity.getLocation().size() > 0) {
                List<Location> activityLocations = activity.getLocation()
                        .stream().map(location -> toActivityLocation(location, lan))
                        .collect(Collectors.toList());

                activityLocations.forEach(location -> location.setActivity(newActivity));
                newActivity.setLocations(activityLocations);
            }

            final StringWriter writer = new StringWriter();
            reader.toXML(activity, writer);
            newActivity.setXml(writer.toString());
            activityRepository.save(newActivity);

            if (autocode || (newActivity.getLocations() != null && !newActivity.getLocations().isEmpty())) {
                final ActivityQueue activityQueue = new ActivityQueue();
                activityQueue.setActivity(newActivity);
                activityQueue.setCreateDate(new Date());
                activityQueue.setState(Constants.ST_PENDING);
                activityQueueRepository.save(activityQueue);
            }
        }
    }


    public Location toActivityLocation(org.devgateway.geocoder.iati.model.Location location, String lan) {


        Location acLocation = new Location();

        //"name",
        acLocation.setNames(extractors.getTexts(location.getName()));

        //"locationId",

        List<LocationIdentifier> identifiers = extractors.getIdentifier(location.getLocationId());
        identifiers.forEach(locationIdentifier -> locationIdentifier.setLocation(acLocation));
        acLocation.setLocationIdentifiers(identifiers);

        //"administrative",
        List<Administrative> administratives = extractors.getAdministratives(location.getAdministrative());
        administratives.forEach(administrative -> administrative.setLocation(acLocation));
        acLocation.setAdministratives(administratives);


        //"point",
        String[] latLong = location.getPoint().getPos().split(" ");
        Point pos = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(Double.parseDouble(latLong[1]), Double.parseDouble(latLong[0])));
        acLocation.setPoint(pos);


        //"activityDescription",
        acLocation.setActivityDescriptions(extractors.getTexts(location.getActivityDescription()));

        //"description",
        acLocation.setDescriptions(extractors.getTexts(location.getDescription()));

        //"locationClass",
        acLocation.setLocationClass(this.geographicLocationClassRepository.findOneByCode(location.getLocationClass().getCode()));

        //"exactness",
        acLocation.setExactness(this.geographicExactnessRepository.findOneByCode(location.getExactness().getCode()));

        //"locationReach",
        acLocation.setLocationReach(this.geographicLocationReachRepository.findOneByCode(location.getLocationClass().getCode()));

        acLocation.setLocationStatus(LocationStatus.EXISTING);

        //In some cases they put the name instead of code
        String fDesignation = location.getFeatureDesignation().getCode();
        if (fDesignation != null && fDesignation.length() > 6) {
            acLocation.setFeaturesDesignation(this.geographicFeatureDesignationRepository.findOneByNameIgnoreCase(fDesignation));
        } else {
            acLocation.setFeaturesDesignation(this.geographicFeatureDesignationRepository.findOneByCode(fDesignation));
        }


        return acLocation;
    }


}

