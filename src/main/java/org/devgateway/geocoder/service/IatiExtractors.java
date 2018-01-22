package org.devgateway.geocoder.service;

import org.devgateway.geocoder.domain.Administrative;
import org.devgateway.geocoder.domain.Country;
import org.devgateway.geocoder.domain.LocationIdentifier;
import org.devgateway.geocoder.iati.model.ActivityDate;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.iati.model.Location;
import org.devgateway.geocoder.iati.model.Narrative;
import org.devgateway.geocoder.iati.model.TextRequiredType;
import org.devgateway.geocoder.repositories.CountryRepository;
import org.devgateway.geocoder.repositories.GeographicVocabularyRepository;
import org.geonames.Toponym;
import org.geonames.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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

            for (IatiActivity.Description description : acDescriptions) {
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
                .map(recipientCountry -> {
                    Country country = countryRepository.findOneByIso2(recipientCountry.getCode());
                    if (country != null) {
                        return country;
                    } else {
                        log.warning("Wasn't able to find country code " + recipientCountry.getCode());
                        return null;
                    }
                }).filter(country -> country != null)
                .collect(Collectors.toList());
    }

    /**
     * Extracts all location identifier
     */
    public Set<LocationIdentifier> getIdentifier(List<org.devgateway.geocoder.iati.model.Location.LocationId> iatiIdentifiers) {
        Set<LocationIdentifier> list = null;
        if (iatiIdentifiers != null && iatiIdentifiers.size() > 0) {
            list = iatiIdentifiers.stream().map(locationId -> new LocationIdentifier(geographicVocabularyRepository
                    .findOneByCode(locationId.getVocabulary()), locationId.getCode())).collect(Collectors.toSet());
        }

        return list;
    }

    /**
     * Extracts {@link LocationIdentifier} and converts them to {@link Location.LocationId}.
     */
    public List<Location.LocationId> extractIdentifier(Set<LocationIdentifier> locationIdentifiers) {
        List<org.devgateway.geocoder.iati.model.Location.LocationId> iatiIdentifiers = null;
        if (locationIdentifiers != null && !locationIdentifiers.isEmpty()) {
            iatiIdentifiers = locationIdentifiers.stream()
                    .map(locationIdentifier -> {
                        final Location.LocationId locationId = new Location.LocationId();
                        locationId.setCode(locationIdentifier.getCode());
                        locationId.setVocabulary(locationIdentifier.getVocabulary().getCode());
                        return locationId;
                    })
                    .collect(Collectors.toList());
        }

        return iatiIdentifiers;
    }

    /**
     * Extracts all location names
     */
    public List<org.devgateway.geocoder.domain.Narrative> getTexts(TextRequiredType textRequiredType) {
        List<org.devgateway.geocoder.domain.Narrative> value = null;
        if (textRequiredType != null) {
            value = textRequiredType.getNarrative()
                    .stream()
                    .map(narrative -> new org.devgateway.geocoder.domain.Narrative(narrative.getLang() != null ? narrative.getLang() : "en", narrative.getValue()))
                    .collect(Collectors.toList());
        }

        return value;
    }

    /**
     * Extracts {@link org.devgateway.geocoder.domain.Narrative} and converts them to {@link TextRequiredType}.
     */
    public TextRequiredType extractTexts(Set<org.devgateway.geocoder.domain.Narrative> narratives) {
        final TextRequiredType textRequiredType = new TextRequiredType();
        List<Narrative> value = null;
        if (narratives != null && !narratives.isEmpty()) {
            value = narratives.stream()
                    .map(narrative -> {
                        final Narrative nar = new Narrative();
                        nar.setLang(narrative.getLang() != null ? narrative.getLang() : "en");
                        nar.setValue(narrative.getDescription());
                        return nar;
                    })
                    .collect(Collectors.toList());
        }

        textRequiredType.getNarrative().clear();
        if (value != null) {
            textRequiredType.getNarrative().addAll(value);
        }

        return textRequiredType;
    }

    /**
     * Extracts all location admin levels
     */
    public Set<Administrative> getAdministratives(List<org.devgateway.geocoder.iati.model.Location.Administrative> iatiAdministratives) {
        Set<Administrative> value = null;

        WebService.setUserName("sdimunzio"); // add your username here

        if (iatiAdministratives != null && iatiAdministratives.size() > 0) {
            value = iatiAdministratives.stream().map(administrative -> {
                        String adminName = "";
                        if (administrative.getCode() != null) {
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
                        return null;
                    }
            ).collect(Collectors.toSet()).stream().filter(administrative -> {
                return administrative != null;
            }).collect(Collectors.toSet());
        }

        return value;
    }

    public List<Location.Administrative> extractAdministratives(Set<Administrative> administratives) {
        List<Location.Administrative> iatiAdministratives = null;
        if (administratives != null && !administratives.isEmpty()) {
            iatiAdministratives = administratives.stream()
                    .map(administrative -> {
                        final Location.Administrative iatiAdministrative = new Location.Administrative();
                        iatiAdministrative.setCode(administrative.getCode());
                        iatiAdministrative.setLevel(BigInteger.valueOf(administrative.getLevel()));
                        iatiAdministrative.setVocabulary(administrative.getVocabulary().getCode());

                        return iatiAdministrative;
                    })
                    .collect(Collectors.toList());
        }

        return iatiAdministratives;
    }
}
