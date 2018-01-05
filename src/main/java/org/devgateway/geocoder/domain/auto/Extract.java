package org.devgateway.geocoder.domain.auto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.geocoder.domain.Location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Sebastian Dimunzio on 11/16/2017.
 */
@Entity
@Table(indexes = {@Index(columnList = "geocoding_id"), @Index(columnList = "location_id")})
public class Extract {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    private Long id;

    @Column(columnDefinition = "text")
    private String text;

    private String entities;

    private String fileName;

    @ManyToOne()
    private Geocoding geocoding;

    @JsonIgnore
    @ManyToOne()
    private Location location;

    @JsonIgnore
    @ManyToOne()
    private Queue queue;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEntities() {
        return entities;
    }

    public void setEntities(String entities) {
        this.entities = entities;
    }

    public Geocoding getGeocoding() {
        return geocoding;
    }

    public void setGeocoding(Geocoding geocoding) {
        this.geocoding = geocoding;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
