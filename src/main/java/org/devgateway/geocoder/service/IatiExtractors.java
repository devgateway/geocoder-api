package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.iati.model.ActivityDate;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.iati.model.Narrative;
import org.devgateway.geocoder.iati.model.TextRequiredType;
import org.devgateway.geocoder.repositories.CountryRepository;
import org.devgateway.geocoder.responses.CountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Sebastian Dimunzio on 11/9/2017.
 */
@Component
public class IatiExtractors {
    static Logger log = Logger.getLogger(IatiExtractors.class.getName());

    @Autowired
    CountryRepository countryRepository;


    public String getTitle(TextRequiredType textRequiredType, String lan) {
        String retValue = null;
        Optional<Narrative> narrativeTitle = null;
        try {
            narrativeTitle = textRequiredType.getNarrative().stream().filter(narrative -> lan.equalsIgnoreCase(narrative.getLang())).findFirst();
            if (!narrativeTitle.isPresent() || narrativeTitle.get().getValue().isEmpty()) {
                log.info("Can't find title for language " + lan + " or it was empty, getting first one");
                narrativeTitle = textRequiredType.getNarrative().stream().findFirst();
            }

            if (narrativeTitle.isPresent()) {
                retValue = narrativeTitle.get().getValue();
            }

        } catch (Exception ex) {
            log.log(Level.WARNING, "Error when extracting description from activity", ex);
        }
        return retValue;
    }


    public String getDescription(IatiActivity activity, String type, String lan) {
        String retValue = null;

        try {
            Optional<IatiActivity.Description> acDescription = activity.getDescription().stream().filter(d -> type.equalsIgnoreCase(d.getType())).findFirst();
            if (!acDescription.isPresent()) {
                log.info("Can't find description type " + type);
            } else {

                IatiActivity.Description desc = acDescription.get();
                Optional<Narrative> descNarrative = acDescription.get().getNarrative().stream().filter(narrative -> lan.equalsIgnoreCase(narrative.getLang())).findFirst();

                if (!descNarrative.isPresent() || descNarrative.get().getValue().isEmpty()) {
                    log.info("Can't find narrative with language " + lan + " getting first one");
                    descNarrative = acDescription.get().getNarrative().stream().findFirst();
                }

                if (descNarrative.isPresent()) {
                    retValue = descNarrative.get().getValue();
                }
            }
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error when extracting description from activity", ex);
        }
        return retValue;
    }

    public Date getDate(IatiActivity iatiActivity, String type) {
        List<ActivityDate> activityDates = iatiActivity.getActivityDate().stream().filter(activityDate -> type.equalsIgnoreCase(activityDate.getType())).collect(Collectors.toList());
        ActivityDate isoDate = activityDates.iterator().hasNext() ? activityDates.iterator().next() : null;

        Date date = (isoDate != null) ? isoDate.getIsoDate().toGregorianCalendar().getTime() : null;
        return date;
    }


    public List<CountryResponse> getCountries(IatiActivity iatiActivity, String lan) {
        return iatiActivity.getRecipientCountry()
                .stream()
                .map(recipientCountry -> new CountryResponse(recipientCountry.getCode(), countryRepository.findOneByIso2(recipientCountry.getCode()).getName()))
                .collect(Collectors.toList());

    }

}
