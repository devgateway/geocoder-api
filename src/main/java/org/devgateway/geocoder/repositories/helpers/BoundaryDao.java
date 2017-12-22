package org.devgateway.geocoder.repositories.helpers;

import com.vividsolutions.jts.geom.Geometry;

public class BoundaryDao {

    public BoundaryDao(Long id, Geometry geometry) {
        this.id = id;
        this.geometry = geometry;
    }

    public BoundaryDao(Long id) {
        this.id = id;
    }

    public BoundaryDao() {
    }

    Long id;
    Geometry geometry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
