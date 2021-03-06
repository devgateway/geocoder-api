package org.devgateway.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import org.devgateway.geocoder.domain.auto.Extract;
import org.devgateway.geocoder.domain.auto.Queue;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(indexes = {@Index(columnList = "activity_id"), @Index(columnList = "locationStatus")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location extends GenericPersistable {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "queue_id")
    private Queue queue;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "location")
    private Set<Extract> extracts = new HashSet<>();

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Narrative> names;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Narrative> activityDescriptions;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Narrative> descriptions;

    private Point point;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true)
    private Set<LocationIdentifier> locationIdentifiers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true)
    Set<Administrative> administratives;

    @ManyToOne(targetEntity = GeographicLocationClass.class)
    GeographicLocationClass locationClass;

    @ManyToOne(targetEntity = GeographicExactness.class)
    GeographicExactness exactness;

    @ManyToOne(targetEntity = GeographicLocationReach.class)
    GeographicLocationReach locationReach;

    @ManyToOne(targetEntity = GeographicFeatureDesignation.class)
    GeographicFeatureDesignation featuresDesignation;

    @ManyToOne(targetEntity = GeographicalPrecision.class)
    GeographicalPrecision precision;

    @ManyToOne(targetEntity = GazetteerAgency.class)
    GazetteerAgency gazetteerAgency;

    private LocationStatus locationStatus;

    @Enumerated(EnumType.STRING)
    public LocationStatus getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(LocationStatus locationStatus) {
        this.locationStatus = locationStatus;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Set<Extract> getExtracts() {
        return extracts;
    }

    public void setExtracts(Set<Extract> extracts) {
        this.extracts = extracts;
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

    public Set<LocationIdentifier> getLocationIdentifiers() {
        return locationIdentifiers;
    }

    public void setLocationIdentifiers(Set<LocationIdentifier> locationIdentifiers) {
        this.locationIdentifiers = locationIdentifiers;
    }

    public Set<Administrative> getAdministratives() {
        return administratives;
    }

    public void setAdministratives(Set<Administrative> administratives) {
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

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
