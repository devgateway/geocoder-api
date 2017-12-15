package org.devgateway.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 *
 * Any change to this {@link Activity} Entity will affect the way the autogeocoder tool process the activities
 * (see 'get_activity_by_id' function from autogeocoder tool)
 */
@Entity
// @Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "title"), @Index(columnList = "description")})
@JsonIgnoreProperties({"parent", "new"})
public class Activity extends AbstractAuditableEntity {
    private String identifier;

    @Column(length = 3000)
    private String title;

    @Column(length = 10000)
    private String description;

    private Date date;

    @OneToMany(targetEntity = Location.class, cascade = CascadeType.ALL, mappedBy = "activity")
    List<Location> locations = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Country> countries = new HashSet<>();

    @JsonIgnore
    @Column(length = 32000)
    @org.hibernate.annotations.Type(type = "org.devgateway.geocoder.types.IatiActivityUserType")
    private String xml;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
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
