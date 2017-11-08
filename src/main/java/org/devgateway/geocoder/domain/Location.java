package org.devgateway.geocoder.domain;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.List;

@Entity
public class Location {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id

    private Long id;

    @ManyToOne(targetEntity = Activity.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name="activity_id", nullable=false)
    private Activity activity;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL)
    private List<Narrative> names;

    private Point point;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL)
    private List<Narrative> activityDescriptions;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL)
    private List<Narrative> descriptions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private List<LocationIdentifier> locationIdentifiers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    List<Administrative> administratives;

    @ManyToOne(targetEntity = GeographicLocationClass.class, cascade = CascadeType.ALL)
    GeographicLocationClass geographicLocationClass;

    @ManyToOne(targetEntity = GeographicExactness.class, cascade = CascadeType.ALL)
    GeographicExactness geographicExactness;

    @ManyToOne(targetEntity = GeographicLocationReach.class, cascade = CascadeType.ALL)
    GeographicLocationReach geographicLocationReach;


    @ManyToOne(targetEntity = GeographicalPrecision.class, cascade = CascadeType.ALL)
    GeographicalPrecision geographicalPrecision;


    @ManyToOne(targetEntity = GeographicVocabulary.class, cascade = CascadeType.ALL)
    GeographicVocabulary geographicVocabulary;


    @ManyToOne(targetEntity = GeographicLocationClass.class, cascade = CascadeType.ALL)
    GazetteerAgency gazetteerAgency;


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

    public GeographicLocationClass getGeographicLocationClass() {
        return geographicLocationClass;
    }

    public void setGeographicLocationClass(GeographicLocationClass geographicLocationClass) {
        this.geographicLocationClass = geographicLocationClass;
    }

    public GeographicExactness getGeographicExactness() {
        return geographicExactness;
    }

    public void setGeographicExactness(GeographicExactness geographicExactness) {
        this.geographicExactness = geographicExactness;
    }

    public GeographicLocationReach getGeographicLocationReach() {
        return geographicLocationReach;
    }

    public void setGeographicLocationReach(GeographicLocationReach geographicLocationReach) {
        this.geographicLocationReach = geographicLocationReach;
    }

    public GeographicalPrecision getGeographicalPrecision() {
        return geographicalPrecision;
    }

    public void setGeographicalPrecision(GeographicalPrecision geographicalPrecision) {
        this.geographicalPrecision = geographicalPrecision;
    }

    public GeographicVocabulary getGeographicVocabulary() {
        return geographicVocabulary;
    }

    public void setGeographicVocabulary(GeographicVocabulary geographicVocabulary) {
        this.geographicVocabulary = geographicVocabulary;
    }

    public GazetteerAgency getGazetteerAgency() {
        return gazetteerAgency;
    }

    public void setGazetteerAgency(GazetteerAgency gazetteerAgency) {
        this.gazetteerAgency = gazetteerAgency;
    }
}
