package org.devgateway.geocoder.request;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.devgateway.geocoder.constants.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sebastian Dimunzio on 11/10/2017.
 */
public class SearchRequest {
    private String lan = "en";
    private List<String> countries;
    private String withLoc = null;
    private String text;
    private Date date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }


    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public String getWithLoc() {
        return withLoc;
    }

    public void setWithLoc(String withLoc) {
        this.withLoc = withLoc;
    }

    public Boolean hasLocation() {
        if (this.withLoc == null || this.withLoc.equalsIgnoreCase("none")) {
            return null;
        } else if (this.withLoc != null && this.withLoc.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }


    }
}


