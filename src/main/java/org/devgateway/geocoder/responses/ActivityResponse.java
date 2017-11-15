package org.devgateway.geocoder.responses;

import org.devgateway.geocoder.iati.model.Location;

import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
public class ActivityResponse {


    private Long id;
    private String identifier;
    private String title;
    private String description;
    private List<CountryResponse> countries;
    private List<LocationResponse> locations;
    private Date date;

    public List<CountryResponse> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryResponse> countries) {
        this.countries = countries;
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

    public List<LocationResponse> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationResponse> locations) {
        this.locations = locations;
    }
}
