package org.devgateway.geocoder.request;

import java.util.List;

/**
 * Created by Sebastian Dimunzio on 11/10/2017.
 */
public class SearchRequest {
    private String lan = "en";

    private String text;

    private List<String> countries;

    private List<Integer> years;

    private Boolean withNoLocation;

    private Boolean pendingVerification;

    private Boolean verifiedLocation;

    private Integer page = 0;

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public Boolean getWithNoLocation() {
        return withNoLocation;
    }

    public void setWithNoLocation(Boolean withNoLocation) {
        this.withNoLocation = withNoLocation;
    }

    public Boolean getPendingVerification() {
        return pendingVerification;
    }

    public void setPendingVerification(Boolean pendingVerification) {
        this.pendingVerification = pendingVerification;
    }

    public Boolean getVerifiedLocation() {
        return verifiedLocation;
    }

    public void setVerifiedLocation(Boolean verifiedLocation) {
        this.verifiedLocation = verifiedLocation;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
