package org.devgateway.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.devgateway.geocoder.domain.auto.ActivityQueue;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 *
 * Any change to this {@link Activity} Entity will affect the way the autogeocoder tool process the activities
 * (see 'get_activity_by_id' function from autogeocoder tool)
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "date"), @Index(columnList = "identifier")})
@JsonIgnoreProperties({"parent", "new"})
public class Activity extends AbstractAuditableEntity {
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private ActivityQueue queue;

    private String identifier;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Narrative> titles;

    @OneToMany(targetEntity = Narrative.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Narrative> descriptions;

    private Date date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "activity")
    private Set<Location> locations = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Country> countries = new HashSet<>();

    @JsonIgnore
    @Column(columnDefinition = "text")
    @org.hibernate.annotations.Type(type = "org.devgateway.geocoder.types.IatiActivityUserType")
    private String xml;

    public ActivityQueue getQueue() {
        return queue;
    }

    public void setQueue(ActivityQueue queue) {
        this.queue = queue;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<Narrative> getTitles() {
        return titles;
    }

    public void setTitles(Set<Narrative> titles) {
        this.titles = titles;
    }

    public Set<Narrative> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Set<Narrative> descriptions) {
        this.descriptions = descriptions;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }
}
