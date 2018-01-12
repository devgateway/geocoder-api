package org.devgateway.geocoder.domain.auto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Sebastian Dimunzio on 11/16/2017.
 */
@Entity
public class Geocoding {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    private Long id;

    private Integer geonameId;

    private String toponymName;

    private String name;

    private Double lat;

    private Double lng;

    private String countryCode;

    private String countryName;

    private String fcl;

    private String fcode;

    private String fclname;

    private String fcodename;

    private String population;

    private String continentcode;

    private String adminCode1;

    private String adminName1;

    private String adminCode2;

    private String adminName2;

    private String adminCode3;

    private String adminName3;

    private String adminCode4;

    private String adminName4;

    @JsonIgnore
    @ManyToOne(targetEntity = Queue.class)
    @JoinColumn(name = "queue_id")
    private Queue queue;

    private Long activity_id;

    @JsonIgnore
    @OneToMany(mappedBy = "geocoding")
    List<Extract> extracts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGeonameId() {
        return geonameId;
    }

    public void setGeonameId(Integer geonameId) {
        this.geonameId = geonameId;
    }

    public String getToponymName() {
        return toponymName;
    }

    public void setToponymName(String toponymName) {
        this.toponymName = toponymName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getFcl() {
        return fcl;
    }

    public void setFcl(String fcl) {
        this.fcl = fcl;
    }

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    public String getFclname() {
        return fclname;
    }

    public void setFclname(String fclname) {
        this.fclname = fclname;
    }

    public String getFcodename() {
        return fcodename;
    }

    public void setFcodename(String fcodename) {
        this.fcodename = fcodename;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getContinentcode() {
        return continentcode;
    }

    public void setContinentcode(String continentcode) {
        this.continentcode = continentcode;
    }

    public String getAdminCode1() {
        return adminCode1;
    }

    public void setAdminCode1(String adminCode1) {
        this.adminCode1 = adminCode1;
    }

    public String getAdminName1() {
        return adminName1;
    }

    public void setAdminName1(String adminName1) {
        this.adminName1 = adminName1;
    }

    public String getAdminCode2() {
        return adminCode2;
    }

    public void setAdminCode2(String adminCode2) {
        this.adminCode2 = adminCode2;
    }

    public String getAdminName2() {
        return adminName2;
    }

    public void setAdminName2(String adminName2) {
        this.adminName2 = adminName2;
    }

    public String getAdminCode3() {
        return adminCode3;
    }

    public void setAdminCode3(String adminCode3) {
        this.adminCode3 = adminCode3;
    }

    public String getAdminName3() {
        return adminName3;
    }

    public void setAdminName3(String adminName3) {
        this.adminName3 = adminName3;
    }

    public String getAdminCode4() {
        return adminCode4;
    }

    public void setAdminCode4(String adminCode4) {
        this.adminCode4 = adminCode4;
    }

    public String getAdminName4() {
        return adminName4;
    }

    public void setAdminName4(String adminName4) {
        this.adminName4 = adminName4;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Long getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Long activity_id) {
        this.activity_id = activity_id;
    }

    public List<Extract> getExtracts() {
        return extracts;
    }

    public void setExtracts(List<Extract> extracts) {
        this.extracts = extracts;
    }


}
