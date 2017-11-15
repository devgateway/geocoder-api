package org.devgateway.geocoder.responses;

import org.devgateway.geocoder.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Sebastian Dimunzio on 11/13/2017.
 */
public class LocationResponse {

    private Long id;

    private String name;

    private Double x;

    private Double y;

    private String activityDescriptions;

    private String description;

    private String locationIdentifiers;

    private List<AdministrativeResponse> administratives;

    private GeographicLocationClass locationClass;

    private GeographicFeatureDesignation featuresDesignation;

    private GeographicExactness exactness;

    private GeographicLocationReach locationReach;

    private GeographicalPrecision precision;

    private GeographicVocabulary vocabulary;

    private GazetteerAgency gazetteerAgency;

    private String status;

    public LocationResponse(org.devgateway.geocoder.domain.Location location, String lan) {
        this.id = location.getId();

        this.name = this.getNarrativeIfPrenset(location.getNames(), lan);

        this.description = this.getNarrativeIfPrenset(location.getDescriptions(), lan);

        this.activityDescriptions = this.getNarrativeIfPrenset(location.getActivityDescriptions(), lan);

        this.locationIdentifiers = location.getLocationIdentifiers().iterator().next().getCode();

        this.administratives = location.getAdministratives().stream().map(administrative -> new AdministrativeResponse(administrative.getLevel(), administrative.getCode())).collect(Collectors.toList());

        this.locationClass = location.getLocationClass();

        this.locationClass = location.getLocationClass();

        this.exactness = location.getExactness();

        this.locationReach = location.getLocationReach();

        this.precision = location.getPrecision();

        this.vocabulary = location.getVocabulary();

        this.gazetteerAgency = location.getGazetteerAgency();

        this.featuresDesignation = location.getFeaturesDesignation();

        this.status = location.getLocationStatus().name();

        this.x = location.getPoint().getCoordinate().x;
        this.y = location.getPoint().getCoordinate().y;


    }

    private String getNarrativeIfPrenset(final List<Narrative> narratives, final String lan) {
        Optional<Narrative> value = narratives.stream().filter(narrative -> lan.equalsIgnoreCase(narrative.getLan()) || narrative.getLan() == null).findFirst();
        return value.isPresent() ? value.get().getDescription() : null;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getActivityDescriptions() {
        return activityDescriptions;
    }

    public void setActivityDescriptions(String activityDescriptions) {
        this.activityDescriptions = activityDescriptions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationIdentifiers() {
        return locationIdentifiers;
    }

    public void setLocationIdentifiers(String locationIdentifiers) {
        this.locationIdentifiers = locationIdentifiers;
    }

    public List<AdministrativeResponse> getAdministratives() {
        return administratives;
    }

    public void setAdministratives(List<AdministrativeResponse> administratives) {
        this.administratives = administratives;
    }

    public GeographicLocationClass getLocationClass() {
        return locationClass;
    }

    public void setLocationClass(GeographicLocationClass locationClass) {
        this.locationClass = locationClass;
    }

    public GeographicExactness getExactness() {
        return exactness;
    }

    public void setExactness(GeographicExactness exactness) {
        this.exactness = exactness;
    }

    public GeographicLocationReach getLocationReach() {
        return locationReach;
    }

    public void setLocationReach(GeographicLocationReach locationReach) {
        this.locationReach = locationReach;
    }

    public GeographicalPrecision getPrecision() {
        return precision;
    }

    public void setPrecision(GeographicalPrecision precision) {
        this.precision = precision;
    }

    public GeographicVocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(GeographicVocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public GazetteerAgency getGazetteerAgency() {
        return gazetteerAgency;
    }

    public void setGazetteerAgency(GazetteerAgency gazetteerAgency) {
        this.gazetteerAgency = gazetteerAgency;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public GeographicFeatureDesignation getFeaturesDesignation() {
        return featuresDesignation;
    }

    public void setFeaturesDesignation(GeographicFeatureDesignation featuresDesignation) {
        this.featuresDesignation = featuresDesignation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
