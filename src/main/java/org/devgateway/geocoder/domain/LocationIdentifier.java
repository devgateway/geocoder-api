package org.devgateway.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Sebastian Dimunzio on 11/7/2017.
 */
@Entity
public class LocationIdentifier {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    private Long id;

    @ManyToOne(targetEntity = GeographicVocabulary.class,cascade = CascadeType.ALL)
    private GeographicVocabulary vocabulary;

    private String code;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="location_id", nullable=false)
    private Location location;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
