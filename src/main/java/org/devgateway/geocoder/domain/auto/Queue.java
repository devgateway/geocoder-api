package org.devgateway.geocoder.domain.auto;

/**
 * Created by Sebastian Dimunzio on 11/16/2017.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "queue_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("queueType")
@Table(indexes = {@Index(columnList = "queue_type"), @Index(columnList = "state")})
public class Queue {
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    private Long id;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "queue")
    private Set<Geocoding> geocodings = new HashSet<>();

    private String state;

    private Date createDate;

    private Date processedDate;

    @JsonIgnore
    @Column(length = 500)
    private String message;

    public Set<Geocoding> getGeocodings() {
        return geocodings;
    }

    public void setGeocodings(Set<Geocoding> geocodings) {
        this.geocodings = geocodings;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }
}
