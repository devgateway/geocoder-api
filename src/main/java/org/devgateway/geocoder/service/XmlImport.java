package org.devgateway.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.devgateway.geocoder.domain.*;
import org.devgateway.geocoder.iati.ActivitiesReader;
import org.devgateway.geocoder.iati.model.IatiActivities;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.iati.model.TextRequiredType;
import org.devgateway.geocoder.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */

@Component("XmlImport")
@Transactional
public class XmlImport {
    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    GazetteerAgencyRepository gazetteerAgencyRepository;
    @Autowired
    GeographicalPrecisionRepository geographicalPrecisionRepository;
    @Autowired
    GeographicExactnessRepository geographicExactnessRepository;
    @Autowired
    GeographicLocationClassRepository geographicLocationClassRepository;
    @Autowired
    GeographicLocationReachRepository geographicLocationReachRepository;
    @Autowired
    GeographicVocabularyRepository geographicVocabularyRepository;


    public void process(final InputStream in, final String lan) {
        ActivitiesReader reader = new ActivitiesReader(in);
        IatiActivities activities = reader.read();

        for (IatiActivity activity : activities.getIatiActivity()) {
            Activity a = new Activity();
            a.setIdentifier(activity.getIatiIdentifier().getValue());

            if (activity.getLocation().size() > 0) {
                List<Location> activityLocations = activity.getLocation().stream().map(location -> toActivityLocation(location, lan)).collect(Collectors.toList());
                activityLocations.forEach(location -> location.setActivity(a));
                a.setLocations(activityLocations);
            }

            StringWriter writer = new StringWriter();
            reader.toXML(activity, writer);
            a.setXml(writer.toString());
            activityRepository.save(a);
            System.out.print("...............");
        }
    }


    public Location toActivityLocation(org.devgateway.geocoder.iati.model.Location location, String lan) {


        Location acLocation = new Location();

        //"name",
        acLocation.setNames(getTexts(location.getName()));


        //"locationId",

        List<LocationIdentifier> identifiers = getIdentifier(location.getLocationId());
        identifiers.forEach(locationIdentifier -> locationIdentifier.setLocation(acLocation));
        acLocation.setLocationIdentifiers(identifiers);

        //"administrative",
        List<Administrative> administratives = getAdministratives(location.getAdministrative());
        administratives.forEach(administrative -> administrative.setLocation(acLocation));
        acLocation.setAdministratives(administratives);



        //"featureDesignation",
        //"any"


        //"point",
        String[] latLong = location.getPoint().getPos().split(" ");
        Point pos = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(Double.parseDouble(latLong[0]), Double.parseDouble(latLong[1])));
        acLocation.setPoint(pos);


        //"activityDescription",
        acLocation.setActivityDescriptions(getTexts(location.getActivityDescription()));

        //"description",
        acLocation.setDescriptions(getTexts(location.getDescription()));

        //"locationClass",
        acLocation.setGeographicLocationClass(geographicLocationClassRepository.findOneByCode(location.getLocationClass().getCode()));

        //"exactness",
        acLocation.setGeographicExactness(geographicExactnessRepository.findOneByCode(location.getExactness().getCode()));

        //"locationReach",
        acLocation.setGeographicLocationReach(geographicLocationReachRepository.findOneByCode(location.getLocationClass().getCode()));

        return acLocation;
    }

    public List<LocationIdentifier> getIdentifier(List<org.devgateway.geocoder.iati.model.Location.LocationId> iatiIdentifiers) {
        List<LocationIdentifier> list = null;
        if (iatiIdentifiers != null && iatiIdentifiers.size() > 0) {
            list = iatiIdentifiers.stream().map(locationId -> new LocationIdentifier(geographicVocabularyRepository.findOneByCode(locationId.getVocabulary()), locationId.getCode())).collect(Collectors.toList());
        }

        return list;
    }

    public List<Narrative> getTexts(TextRequiredType textRequiredType) {
        List<Narrative> value = null;
        if (textRequiredType != null) {
            value = textRequiredType.getNarrative().stream().map(narrative -> new Narrative(narrative.getLang(), narrative.getValue())).collect(Collectors.toList());

        }
        return value;
    }

    public List<Administrative> getAdministratives(List<org.devgateway.geocoder.iati.model.Location.Administrative> iatiAdministratives) {
        List<Administrative> value = null;

        if (iatiAdministratives != null && iatiAdministratives.size() > 0) {
            value = iatiAdministratives.stream().map(administrative -> new Administrative(administrative.getLevel().intValue(), administrative.getCode(), geographicVocabularyRepository.findOneByCode(administrative.getVocabulary()))).collect(Collectors.toList());

        }

        return value;
    }


}

