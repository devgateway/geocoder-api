package org.devgateway.geocoder.domain;

import javax.persistence.*;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Entity
public class Administrative {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    Long id;

    @ManyToOne()
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    Integer level;

    String code;

    @ManyToOne(targetEntity = GeographicVocabulary.class)
    GeographicVocabulary vocabulary;

    public Administrative() {
    }

    public Administrative(Integer level, String code, GeographicVocabulary vocabulary) {
        this.level = level;
        this.code = code;
        this.vocabulary = vocabulary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GeographicVocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(GeographicVocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }
}
