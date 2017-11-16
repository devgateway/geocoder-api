package org.devgateway.geocoder.domain.auto;

/**
 * Created by Sebastian Dimunzio on 11/16/2017.
 */

import org.devgateway.geocoder.domain.Activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("ACTIVITY_QUEUE")
public class ActivityQueue extends Queue {

    @ManyToOne(targetEntity = Activity.class)
    Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
