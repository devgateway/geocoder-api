package org.devgateway.geocoder.domain;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "boundaries", indexes = {@Index(columnList = "iso")})
public class Boundary {
    @Id
    @Column(name = "gid")
    private Long gid;

    @Column(name = "objectid")
    private Long objectid;

    @Column(name = "iso")
    private String iso;

    @Column(name = "id_0")
    private Long id_0;

    @Column(name = "name_0")
    private String name_0;

    @Column(name = "id_1")
    private Long id_1;

    @Column(name = "name_1")
    private String name_1;

    @Column(name = "id_2")
    private Long id_2;

    @Column(name = "name_2")
    private String name_2;

    @Column(name = "hasc_2")
    private String hasc_2;

    @Column(name = "ccn_2")
    private String ccn_2;

    @Column(name = "cca_2")
    private String cca_2;

    @Column(name = "type_2")
    private String type_2;

    @Column(name = "engtype_2")
    private String engtype_2;

    @Column(name = "nl_name_2")
    private String nl_name_2;

    @Column(name = "varname_2")
    private String varname_2;

    @Column(name = "shape_leng")
    private Double shape_leng;

    @Column(name = "shape_area")
    private Double shape_area;

    @Column(name = "geom")
    private Geometry geometry;

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getObjectid() {
        return objectid;
    }

    public void setObjectid(Long objectid) {
        this.objectid = objectid;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Long getId_0() {
        return id_0;
    }

    public void setId_0(Long id_0) {
        this.id_0 = id_0;
    }

    public String getName_0() {
        return name_0;
    }

    public void setName_0(String name_0) {
        this.name_0 = name_0;
    }

    public Long getId_1() {
        return id_1;
    }

    public void setId_1(Long id_1) {
        this.id_1 = id_1;
    }

    public String getName_1() {
        return name_1;
    }

    public void setName_1(String name_1) {
        this.name_1 = name_1;
    }

    public Long getId_2() {
        return id_2;
    }

    public void setId_2(Long id_2) {
        this.id_2 = id_2;
    }

    public String getName_2() {
        return name_2;
    }

    public void setName_2(String name_2) {
        this.name_2 = name_2;
    }

    public String getHasc_2() {
        return hasc_2;
    }

    public void setHasc_2(String hasc_2) {
        this.hasc_2 = hasc_2;
    }

    public String getCcn_2() {
        return ccn_2;
    }

    public void setCcn_2(String ccn_2) {
        this.ccn_2 = ccn_2;
    }

    public String getCca_2() {
        return cca_2;
    }

    public void setCca_2(String cca_2) {
        this.cca_2 = cca_2;
    }

    public String getType_2() {
        return type_2;
    }

    public void setType_2(String type_2) {
        this.type_2 = type_2;
    }

    public String getEngtype_2() {
        return engtype_2;
    }

    public void setEngtype_2(String engtype_2) {
        this.engtype_2 = engtype_2;
    }

    public String getNl_name_2() {
        return nl_name_2;
    }

    public void setNl_name_2(String nl_name_2) {
        this.nl_name_2 = nl_name_2;
    }

    public String getVarname_2() {
        return varname_2;
    }

    public void setVarname_2(String varname_2) {
        this.varname_2 = varname_2;
    }

    public Double getShape_leng() {
        return shape_leng;
    }

    public void setShape_leng(Double shape_leng) {
        this.shape_leng = shape_leng;
    }

    public Double getShape_area() {
        return shape_area;
    }

    public void setShape_area(Double shape_area) {
        this.shape_area = shape_area;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
