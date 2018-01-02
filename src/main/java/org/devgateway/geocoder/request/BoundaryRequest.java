package org.devgateway.geocoder.request;

import java.util.List;

public class BoundaryRequest {


    private Integer zoom;
    private List<String> codes;

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }


}
