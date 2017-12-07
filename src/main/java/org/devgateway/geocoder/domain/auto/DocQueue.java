package org.devgateway.geocoder.domain.auto;

import org.devgateway.geocoder.domain.Country;
import org.devgateway.geocoder.domain.Location;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Sebastian Dimunzio on 11/16/2017.
 */
@Entity
@DiscriminatorValue("DOC_QUEUE")
public class DocQueue extends Queue {
    private String fileName;
    private String fileType;
    private String countryIso;


    @OneToMany(targetEntity = Location.class, cascade = CascadeType.ALL, mappedBy = "queue")
    List<Location> locations;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
