package org.devgateway.geocoder.domain;


import javax.persistence.*;
import java.util.List;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */

@Entity
public class Activity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    Long id;
    String identifier;

    @OneToMany(targetEntity = Location.class,cascade = CascadeType.ALL, mappedBy = "activity")
    List<Location> locations;


    @Column(columnDefinition = "text")
    String xml;

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
