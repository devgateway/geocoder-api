package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Administrative;
import org.devgateway.geocoder.domain.Country;
import org.devgateway.geocoder.domain.LocationIdentifier;
import org.devgateway.geocoder.iati.model.ActivityDate;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.iati.model.Narrative;
import org.devgateway.geocoder.iati.model.TextRequiredType;
import org.devgateway.geocoder.repositories.CountryRepository;
import org.devgateway.geocoder.repositories.GeographicVocabularyRepository;
import org.geonames.Toponym;
import org.geonames.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private CountryRepository countryRepository;

    @Autowired
    private GeographicVocabularyRepository geographicVocabularyRepository;

    public List<org.devgateway.geocoder.domain.Narrative> getTitles(TextRequiredType textRequiredType) {
        List<org.devgateway.geocoder.domain.Narrative> titles = null;
        try {
            titles = textRequiredType.getNarrative()
                    .stream()
                    .map(narrative -> new org.devgateway.geocoder.domain.Narrative(narrative.getLang(), narrative.getValue()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error when extracting title from activity", ex);
        }

        return titles;
    }


    public List<org.devgateway.geocoder.domain.Narrative> getDescriptions(IatiActivity activity) {
        List<org.devgateway.geocoder.domain.Narrative> descriptions = new ArrayList<>();

        try {
            List<IatiActivity.Description> acDescriptions = activity.getDescription();

            for(IatiActivity.Description description : acDescriptions) {
                description.getNarrative()
                        .stream()
                        .forEach(narrative -> descriptions.add(
                                new org.devgateway.geocoder.domain.Narrative(
                                        narrative.getLang(), narrative.getValue())));

            }
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error when extracting description from activity", ex);
        }

        return descriptions;
    }

    public Date getDate(IatiActivity iatiActivity, String type) {
        List<ActivityDate> activityDates = iatiActivity.getActivityDate()
                .stream()
                .filter(activityDate -> type.equalsIgnoreCase(activityDate.getType())).collect(Collectors.toList());
        ActivityDate isoDate = activityDates.iterator().hasNext() ? activityDates.iterator().next() : null;

        Date date = (isoDate != null) ? isoDate.getIsoDate().toGregorianCalendar().getTime() : null;
        return date;
    }


    public List<Country> getCountries(final IatiActivity iatiActivity) {
        return iatiActivity.getRecipientCountry()
                .stream()
                .map(recipientCountry -> countryRepository.findOneByIso2(recipientCountry.getCode()))
                .collect(Collectors.toList());
    }

    /**
     * Extracts all location identifier
     */
    public List<LocationIdentifier> getIdentifier(List<org.devgateway.geocoder.iati.model.Location.LocationId> iatiIdentifiers) {
        List<LocationIdentifier> list = null;
        if (iatiIdentifiers != null && iatiIdentifiers.size() > 0) {
            list = iatiIdentifiers.stream().map(locationId -> new LocationIdentifier(geographicVocabularyRepository
                    .findOneByCode(locationId.getVocabulary()), locationId.getCode())).collect(Collectors.toList());
        }

        return list;
    }

    /**
     * Extracts all location names
     */
    public List<org.devgateway.geocoder.domain.Narrative> getTexts(TextRequiredType textRequiredType) {
        List<org.devgateway.geocoder.domain.Narrative> value = null;
        if (textRequiredType != null) {
            value = textRequiredType.getNarrative()
                    .stream()
                    .map(narrative -> new org.devgateway.geocoder.domain.Narrative(narrative.getLang(), narrative.getValue()))
                    .collect(Collectors.toList());
        }

        return value;
    }

    /**
     * Extracts all location admin levels
     */
    public List<Administrative> getAdministratives(List<org.devgateway.geocoder.iati.model.Location.Administrative> iatiAdministratives) {
        List<Administrative> value = null;

        WebService.setUserName("sdimunzio"); // add your username here

        if (iatiAdministratives != null && iatiAdministratives.size() > 0) {
            value = iatiAdministratives.stream().map(administrative -> {
                        String adminName = "";
                        try {
                            if (!administrative.getCode().equalsIgnoreCase("0.0") &&
                                    administrative.getVocabulary().equalsIgnoreCase("G1")) {
                                Double code = Double.parseDouble(administrative.getCode());
                                Toponym toponym = WebService.get(code.intValue(), "en", "full");
                                adminName = toponym.getName();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return new Administrative(administrative.getLevel().intValue(), administrative.getCode(), adminName,
                                geographicVocabularyRepository.findOneByCode(administrative.getVocabulary()));

                    }


            ).collect(Collectors.toList());
        }

        return value;
    }


}
