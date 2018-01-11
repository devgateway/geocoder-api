package org.devgateway.geocoder.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by Sebastian Dimunzio on 11/2/2017.
 */

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "description")})
public class Narrative extends GenericPersistable {
    private String lang;

    @Column(length = 10000)
    private String description;

    public Narrative() {
    }

    public Narrative(String lang, String description) {
        this.lang = lang;
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
