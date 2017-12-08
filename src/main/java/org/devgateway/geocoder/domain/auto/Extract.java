package org.devgateway.geocoder.domain.auto;


import org.devgateway.geocoder.domain.Location;

import javax.persistence.*;

/**
 * Created by Sebastian Dimunzio on 11/16/2017.
 */
@Entity
public class Extract {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    private Long id;
    @Column(columnDefinition = "text")
    private String text;
    private String entities;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @ManyToOne()
    private Geocoding geocoding;

    @ManyToOne()
    private Location location;

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
}
