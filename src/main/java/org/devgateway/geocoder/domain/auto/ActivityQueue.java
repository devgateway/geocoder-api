package org.devgateway.geocoder.domain.auto;

/**
 * Created by Sebastian Dimunzio on 11/16/2017.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.geocoder.domain.Activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("ACTIVITY_QUEUE")
@Table(indexes = {@Index(columnList = "activity_id")})
public class ActivityQueue extends Queue {
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "activity_id")
    Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
