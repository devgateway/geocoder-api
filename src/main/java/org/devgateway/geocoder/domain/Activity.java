package org.devgateway.geocoder.domain;

import javax.persistence.*;
import java.util.List;

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


}

)

public class Activity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    Long id;
    String identifier;

    @OneToMany(targetEntity = Location.class, cascade = CascadeType.ALL, mappedBy = "activity")
    List<Location> locations;

    @Column(columnDefinition = "xml")
    @org.hibernate.annotations.Type(type = "org.devgateway.geocoder.types.IatiActivityUserType")
    String xml;


    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
