package org.devgateway.geocoder.responses;

/**
 * Created by Sebastian Dimunzio on 11/14/2017.
 */
public class AdministrativeResponse {
    private Integer level;
    private String name;

    public AdministrativeResponse(Integer level, String name) {
        this.level = level;
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
