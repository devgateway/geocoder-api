package org.devgateway.geocoder.domain.auto;

import org.devgateway.geocoder.domain.Country;

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
    @OneToMany(targetEntity = Country.class)
    private List<Country> countries;

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

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
