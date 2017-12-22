package org.devgateway.geocoder.repositories.helpers;

import com.vividsolutions.jts.geom.Geometry;

public class BoundaryWrapper {


    private Long gid;
    private String iso;
    private Long admin0Code;
    private String admin0Name;

    private Long admin1Code;
    private String admin1Name;

    private Long admin2Code;
    private String admin2Name;

    private Geometry geometry;

    public BoundaryWrapper(Long gid, String iso, Long admin0Code, String admin0Name, Long admin1Code, String admin1Name, Long admin2Code, String admin2Name, Geometry geometry) {
        this.gid = gid;
        this.iso = iso;
        this.admin0Code = admin0Code;
        this.admin0Name = admin0Name;
        this.admin1Code = admin1Code;
        this.admin1Name = admin1Name;
        this.admin2Code = admin2Code;
        this.admin2Name = admin2Name;
        this.geometry = geometry;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Long getAdmin0Code() {
        return admin0Code;
    }

    public void setAdmin0Code(Long admin0Code) {
        this.admin0Code = admin0Code;
    }

    public String getAdmin0Name() {
        return admin0Name;
    }

    public void setAdmin0Name(String admin0Name) {
        this.admin0Name = admin0Name;
    }

    public Long getAdmin1Code() {
        return admin1Code;
    }

    public void setAdmin1Code(Long admin1Code) {
        this.admin1Code = admin1Code;
    }

    public String getAdmin1Name() {
        return admin1Name;
    }

    public void setAdmin1Name(String admin1Name) {
        this.admin1Name = admin1Name;
    }

    public Long getAdmin2Code() {
        return admin2Code;
    }

    public void setAdmin2Code(Long admin2Code) {
        this.admin2Code = admin2Code;
    }

    public String getAdmin2Name() {
        return admin2Name;
    }

    public void setAdmin2Name(String admin2Name) {
        this.admin2Name = admin2Name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
