package org.devgateway.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.apache.commons.io.IOUtils;
import org.devgateway.geocoder.constants.Constants;
import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.domain.Administrative;
import org.devgateway.geocoder.domain.FileContent;
import org.devgateway.geocoder.domain.FileMetadata;
import org.devgateway.geocoder.domain.Location;
import org.devgateway.geocoder.domain.LocationIdentifier;
import org.devgateway.geocoder.domain.LocationStatus;
import org.devgateway.geocoder.domain.auto.ActivityQueue;
import org.devgateway.geocoder.iati.ActivitiesReader;
import org.devgateway.geocoder.iati.model.IatiActivities;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.repositories.ActivityQueueRepository;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.repositories.FileMetadataRepository;
import org.devgateway.geocoder.repositories.GeographicExactnessRepository;
import org.devgateway.geocoder.repositories.GeographicFeatureDesignationRepository;
import org.devgateway.geocoder.repositories.GeographicLocationClassRepository;
import org.devgateway.geocoder.repositories.GeographicLocationReachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */

@Component("XmlImport")
@Transactional
public class XmlImport {
    Logger logger = Logger.getLogger(this.getClass().getName());

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
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private CacheService cacheService;

    @Transactional
    public Map<Boolean, List<String>> process(final File file,
                                              final MultipartFile uploadFile,
                                              final Boolean autoGeocode,
                                              final Boolean autoGeocodeWithoutLoc,
                                              final Boolean overwriteProjects) {
        final ActivitiesReader reader = new ActivitiesReader(file);
        final List<String> validations = reader.validate();
        final List<String> statistics = new ArrayList<>();

        if (validations.size() == 0) {
            final IatiActivities activities = reader.read();

            final int statisticCount = activities.getIatiActivity().size();
            final AtomicInteger statisticSaved = new AtomicInteger();
            final AtomicInteger statisticIgnored = new AtomicInteger();
            final AtomicInteger statisticOverwritten = new AtomicInteger();

            logger.log(Level.WARNING, "-------------------------------------------------------------------------------");
            logger.log(Level.WARNING, ">>>>> Import started for " + activities.getIatiActivity().size() + " activities >>>>>");
            logger.log(Level.WARNING, ">>>>> autoGeocode: " + autoGeocode);
            logger.log(Level.WARNING, ">>>>> autoGeocodeWithoutLoc: " + autoGeocodeWithoutLoc);
            logger.log(Level.WARNING, ">>>>> overwriteProjects: " + overwriteProjects);
            long startTime = System.nanoTime();

            final FileMetadata fileMetadata = createFileMetadata(file, uploadFile);

            activities.getIatiActivity().stream().forEach(iatiActivity -> {
                boolean activityShouldBeAdded = false;
                final Activity activity = iatiActivityToActivityEntity(iatiActivity, reader);
                final boolean activityExists = activityRepository.existsByIdentifier(activity.getIdentifier());

                if (overwriteProjects) {
                    if (activityExists) {
                        activityRepository.delete(activityRepository.findByIdentifier(activity.getIdentifier()));
                        statisticOverwritten.getAndIncrement();
                    }
                    activityShouldBeAdded = true;
                } else {
                    if (!activityExists) {
                        activityShouldBeAdded = true;
                    }
                }

                if (activityShouldBeAdded) {
                    activity.setFileId(fileMetadata.getId());       // also save the file ID
                    activityRepository.save(activity);
                    statisticSaved.getAndIncrement();

                    logger.log(Level.WARNING, ">>>>> Activity with identifier: " + activity.getIdentifier() + " was saved");

                    if (autoGeocode) {
                        addQueue(activity);
                    } else {
                        if (autoGeocodeWithoutLoc && (activity.getLocations() != null && activity.getLocations().isEmpty())) {
                            addQueue(activity);
                        }
                    }
                } else {
                    statisticIgnored.getAndIncrement();
                    logger.log(Level.WARNING, ">>>>> Activity with identifier: " + activity.getIdentifier() + " was ignored");
                }
            });

            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000000.0;
            logger.log(Level.WARNING, ">>>>> Import finished in: " + duration + " seconds >>>>>");

            // clear all the caches after we finish the import
            cacheService.clearAllCache();

            // create some statistics about this import
            statistics.add(statisticCount + " Project(s) found in file");
            statistics.add(statisticSaved.get() + " Project(s) saved");
            statistics.add(statisticOverwritten.get() + " Project(s) overwritten");
            statistics.add(statisticIgnored.get() + " Project(s) ignored");
            statistics.add("Import duration: " + duration + " seconds");

            return new HashMap<Boolean, List<String>>() {{
                put(Boolean.TRUE, statistics);
            }};
        } else {
            // if not valid return validation error of most recent version
            return new HashMap<Boolean, List<String>>() {{
                put(Boolean.FALSE, validations);
            }};
        }
    }

    private FileMetadata createFileMetadata(final File file, final MultipartFile uploadFile) {
        try {
            final FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setName(uploadFile.getOriginalFilename());
            fileMetadata.setContentType(uploadFile.getContentType());
            fileMetadata.setSize(uploadFile.getSize());

            final FileContent fileContent = new FileContent();
            fileContent.setBytes(IOUtils.toByteArray(new FileInputStream(file)));
            fileMetadata.setContent(fileContent);

            fileMetadataRepository.saveAndFlush(fileMetadata);

            return fileMetadata;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error creating FileMetadata ", e);
        }

        return null;
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

            final Set<LocationIdentifier> identifiers = extractors.getIdentifier(iatiLocation.getLocationId());

            if (identifiers != null) {
                identifiers.forEach(locationIdentifier -> locationIdentifier.setLocation(location));
                location.setLocationIdentifiers(identifiers);
            }

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

            if (iatiLocation.getLocationReach() != null) {
                location.setLocationReach(this.geographicLocationReachRepository.findOneByCode(iatiLocation.getLocationReach().getCode()));
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
