package org.devgateway.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Point;
import org.devgateway.geocoder.domain.auto.DocQueue;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(indexes = {@Index(columnList = "activity_id"), @Index(columnList = "locationStatus")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id

    private Long id;

    @JsonIgnore
    @ManyToOne(targetEntity = Activity.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "activity_id", nullable = true)
    private Activity activity;

    @JsonIgnore
    @ManyToOne(targetEntity = DocQueue.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "queue_id", nullable = true)
    private DocQueue queue;


    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Narrative> names;

    @JsonIgnore
    private Point point;


    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Narrative> activityDescriptions;


    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Narrative> descriptions;


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


    @ManyToOne(targetEntity = GazetteerAgency.class, cascade = CascadeType.ALL)
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

    public Set<Narrative> getNames() {
        return names;
    }

    public void setNames(Set<Narrative> names) {
        this.names = names;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Set<Narrative> getActivityDescriptions() {
        return activityDescriptions;
    }

    public void setActivityDescriptions(Set<Narrative> activityDescriptions) {
        this.activityDescriptions = activityDescriptions;
    }

    public Set<Narrative> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Set<Narrative> descriptions) {
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

    @JsonProperty("x")
    public Double getPointXForAPI() {
        return point.getCoordinate().x;
    }

    @JsonProperty("y")
    public Double getPointYForAPI() {
        return point.getCoordinate().y;
    }

}
