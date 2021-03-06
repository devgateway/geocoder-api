package org.devgateway.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Sebastian Dimunzio on 11/7/2017.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "location_id")})
public class LocationIdentifier extends GenericPersistable {
    private String code;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(targetEntity = GeographicVocabulary.class)
    private GeographicVocabulary vocabulary;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocationIdentifier(GeographicVocabulary vocabulary, String code) {
        this.vocabulary = vocabulary;
        this.code = code;
    }

    public LocationIdentifier() {
    }

    public GeographicVocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(GeographicVocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
