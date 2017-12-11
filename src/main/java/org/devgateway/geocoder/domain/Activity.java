package org.devgateway.geocoder.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */

@Entity
@NamedNativeQueries({@NamedNativeQuery(
        resultClass = Activity.class,
        name = "Activity.findByText",
        query = "select *  " +
                "  from activity where " +
                " cast( xpath('//narrative/text()',xml) as text) ilike :text"),
        @NamedNativeQuery(
                resultClass = Activity.class,
                name = "Activity.findByDate",
                query = "select  * from activity  where  to_date(cast(((xpath('//activity-date[@type=1]/@iso-date',xml))[1]) as varchar),'YYYY-MM-DD') between :d1 and :d2"),
        @NamedNativeQuery(
                resultClass = Activity.class,
                name = "Activity.findByCountries",
                query = "select  * from activity where " +
                        " (cast(xpath('//recipient-country/@code ',xml) as varchar[])) @> cast( :codes  as varchar[])")
})
public class Activity extends AbstractAuditableEntity {
    private String identifier;

    @Column(length = 3000)
    private String title;

    @Column(length = 3000)
    private String description;

    private Date date;

    @OneToMany(targetEntity = Location.class, cascade = CascadeType.ALL, mappedBy = "activity")
    List<Location> locations = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Country> countries = new HashSet<>();;

    @Column(columnDefinition = "xml")
    @org.hibernate.annotations.Type(type = "org.devgateway.geocoder.types.IatiActivityUserType")
    String xml;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }
}
