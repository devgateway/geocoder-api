package org.devgateway.geocoder.domain;

import com.vividsolutions.jts.geom.Point;
import org.devgateway.geocoder.domain.auto.DocQueue;

import javax.persistence.*;
import java.util.List;

@Entity
public class Location {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id

    private Long id;

    @ManyToOne(targetEntity = Activity.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "activity_id", nullable = true)
    private Activity activity;


    @ManyToOne(targetEntity = DocQueue.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "queue_id", nullable = true)
    private DocQueue queue;


    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Narrative> names;

    private Point point;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Narrative> activityDescriptions;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Narrative> descriptions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private List<LocationIdentifier> locationIdentifiers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    List<Administrative> administratives;

    @ManyToOne(targetEntity = GeographicLocationClass.class, cascade = CascadeType.ALL)
    GeographicLocationClass locationClass;

    @ManyToOne(targetEntity = GeographicExactness.class, cascade = CascadeType.ALL)
    GeographicExactness exactness;

    @ManyToOne(targetEntity = GeographicLocationReach.class, cascade = CascadeType.ALL)
    GeographicLocationReach locationReach;

    @ManyToOne(targetEntity = GeographicFeatureDesignation.class, cascade = CascadeType.ALL)
    GeographicFeatureDesignation featuresDesignation;

    @ManyToOne(targetEntity = GeographicalPrecision.class, cascade = CascadeType.ALL)
    GeographicalPrecision precision;


    @ManyToOne(targetEntity = GeographicVocabulary.class, cascade = CascadeType.ALL)
    GeographicVocabulary vocabulary;


    @ManyToOne(targetEntity = GeographicLocationClass.class, cascade = CascadeType.ALL)
    GazetteerAgency gazetteerAgency;

    private LocationStatus locationStatus;


    @Enumerated(EnumType.STRING)
    public LocationStatus getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(LocationStatus locationStatus) {
        this.locationStatus = locationStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Narrative> getNames() {
        return names;
    }

    public void setNames(List<Narrative> names) {
        this.names = names;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<Narrative> getActivityDescriptions() {
        return activityDescriptions;
    }

    public void setActivityDescriptions(List<Narrative> activityDescriptions) {
        this.activityDescriptions = activityDescriptions;
    }

    public List<Narrative> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Narrative> descriptions) {
        this.descriptions = descriptions;
    }

    public List<LocationIdentifier> getLocationIdentifiers() {
        return locationIdentifiers;
    }

    public void setLocationIdentifiers(List<LocationIdentifier> locationIdentifiers) {
        this.locationIdentifiers = locationIdentifiers;
    }

    public List<Administrative> getAdministratives() {
        return administratives;
    }

    public void setAdministratives(List<Administrative> administratives) {
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

    public GeographicFeatureDesignation getFeaturesDesignation() {
        return featuresDesignation;
    }

    public void setFeaturesDesignation(GeographicFeatureDesignation featuresDesignation) {
        this.featuresDesignation = featuresDesignation;
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

    public DocQueue getQueue() {
        return queue;
    }

    public void setQueue(DocQueue queue) {
        this.queue = queue;
    }
}
