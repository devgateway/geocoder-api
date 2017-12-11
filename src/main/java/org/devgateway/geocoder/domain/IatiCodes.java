package org.devgateway.geocoder.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 *
 Gazetteer Agency
 Geographic Exactness
 Geographic Location Class
 Geographic Location Reach
 Geographic Vocabulary
 Geographical Precision

 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(      name="type",
        discriminatorType=DiscriminatorType.STRING      )
@DiscriminatorValue("code")
public class IatiCodes {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    private Long id;

    private String code;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private String lan;

    public IatiCodes() {
    }

    public IatiCodes(String code, String name, String description, String lan) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.lan = lan;
    }

    public IatiCodes(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }
}
