package org.devgateway.geocoder;

import org.devgateway.geocoder.domain.*;
import org.devgateway.geocoder.repositories.*;
import org.devgateway.geocoder.service.XmlImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Sebastian Dimunzio on 11/6/2017.
 */
@Component
public class Runner implements ApplicationRunner {

    @Autowired
    GazetteerAgencyRepository gazetteerAgencyRepository;

    @Autowired
    GeographicalPrecisionRepository geographicalPrecisionRepository;

    @Autowired
    GeographicLocationReachRepository geographicLocationReachRepository;

    @Autowired
    GeographicExactnessRepository geographicExactnessRepository;

    @Autowired
    GeographicLocationClassRepository geographicLocationClassRepository;

    @Autowired
    GeographicVocabularyRepository geographicVocabularyRepository;

    @Autowired
    CountryRepository countryRepository;


    @Autowired
    XmlImport xmlImport;

    @Autowired
    ActivityRepository activityRepository;


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        GazetteerAgency gazetteerAgency;
        gazetteerAgency = new GazetteerAgency();
        gazetteerAgency.setCode("1");
        gazetteerAgency.setName("Geonames.org");
        gazetteerAgency.setLan("en");
        gazetteerAgencyRepository.save(gazetteerAgency);

        gazetteerAgency = new GazetteerAgency();
        gazetteerAgency.setCode("2");
        gazetteerAgency.setName("National Geospatial-Intelligence Agency");
        gazetteerAgency.setLan("en");
        gazetteerAgencyRepository.save(gazetteerAgency);

        gazetteerAgency = new GazetteerAgency();
        gazetteerAgency.setCode("3");
        gazetteerAgency.setName("Open Street Map");
        gazetteerAgency.setLan("en");
        gazetteerAgencyRepository.save(gazetteerAgency);

        /***/
        GeographicVocabulary geographicVocabulary = null;
        geographicVocabulary = new GeographicVocabulary();
        geographicVocabulary.setCode("A1");
        geographicVocabulary.setName("Global Admininistrative Unit Layers");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);


        geographicVocabulary = new GeographicVocabulary();
        geographicVocabulary.setCode("A2");
        geographicVocabulary.setName("UN Second Administrative Level Boundary Project");
        geographicVocabulary.setDescription("Note: the unsalb.org website is no longer accessible, and public access to the boundaries resources has been removed http://www.ungiwg.org/content/united-nations-international-and-administrative-boundaries-resources");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);

        geographicVocabulary = new GeographicVocabulary();
        geographicVocabulary.setCode("A3");
        geographicVocabulary.setName("Global Administrative Areas");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);

        geographicVocabulary = new GeographicVocabulary();
        geographicVocabulary.setCode("A4");
        geographicVocabulary.setName("ISO Country (3166-1 alpha-2)");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);

        geographicVocabulary = new GeographicVocabulary();
        geographicVocabulary.setCode("G1");
        geographicVocabulary.setName("Geonames");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);


        geographicVocabulary = new GeographicVocabulary();
        geographicVocabulary.setCode("G2");
        geographicVocabulary.setName("OpenStreetMap");
        geographicVocabulary.setDescription("Note: the code should be formed by prefixing the relevant OpenStreetMap ID with node/ way/ or relation/ as appropriate, e.g. node/1234567");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);

        /***/


        GeographicalPrecision geographicalPrecision = null;
        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("1");
        geographicalPrecision.setName("Exact location");
        geographicalPrecision.setDescription("The coordinates corresponds to an exact location, such as a populated place or a hill. The code is also used for locations that join a location which is a line (such as a road or railroad). Lines are not coded only the points that connect lines. All points that are mentioned in the source are coded.");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("2");
        geographicalPrecision.setName("Near exact location");
        geographicalPrecision.setDescription("The location is mentioned in the source as being near, in the area of, or up to 25 km away from an exact location. The coordinates refer to that adjacent, exact, location.");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("3");
        geographicalPrecision.setName("Second order administrative division");
        geographicalPrecision.setDescription("The location is, or lies in, a second order administrative division  (ADM2), such as a district, municipality or commune");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("4");
        geographicalPrecision.setName("First order administrative division");
        geographicalPrecision.setDescription("The location is, or lies in, a first order administrative division (ADM1), such as a province, state or governorate");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("5");
        geographicalPrecision.setName("Estimated coordinates");
        geographicalPrecision.setDescription("The location can only be related to estimated coordinates, such as when a location  lies between populated places; along rivers, roads and borders; more than 25 km away from a specific location; or when sources refer to parts of a country greater than ADM1 (e.g. northern Uganda).");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("6");
        geographicalPrecision.setName("Independent political entity");
        geographicalPrecision.setDescription("The location can only be related to an independent political entity, meaning the pair of coordinates that represent a country.");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("7");
        geographicalPrecision.setName("Unclear - capital Unclear.");
        geographicalPrecision.setDescription("The capital is assumed to be one of two possible locations. (The other option is the country level, with precision 9.)");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("8");
        geographicalPrecision.setName("Local or national capital");
        geographicalPrecision.setDescription("The location is estimated to be a seat of an administrative division (local capital) or the national capital.If aid goes to Luanda without further specification on the location, and there is an ADM1 and a capital called Luanda, then code the coordinates of the capital with precision 8. If it is not  spelled out that aid goes to the capital;  but if it is clear that it goes to a government ministry or to government financial institutions; and if those institutions are most likely located in the capital; then the coordinates of the capital are coded with precision 8. (However,if it can be verified that the recipient institution is located in the capital then precision 1 is used.)");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);

        geographicalPrecision = new GeographicalPrecision();
        geographicalPrecision.setCode("9");
        geographicalPrecision.setName("Unclear - country Unclear.");
        geographicalPrecision.setDescription("The locations is estimated to be the country  level(often paired with the capital, with precision 7)");
        geographicalPrecision.setLan("en");
        geographicalPrecisionRepository.save(geographicalPrecision);


        GeographicLocationReach geographicLocationReach = null;

        geographicLocationReach = new GeographicLocationReach();

        geographicLocationReach.setCode("1");
        geographicLocationReach.setName("Activity");
        geographicLocationReach.setDescription("The location specifies where the activity is carried out");
        geographicLocationReach.setLan("en");
        geographicLocationReachRepository.save(geographicLocationReach);

        geographicLocationReach = new GeographicLocationReach();
        geographicLocationReach.setCode("2");
        geographicLocationReach.setName("Intended Beneficiaries");
        geographicLocationReach.setDescription("The location specifies where the intended beneficiaries of the activity live");
        geographicLocationReach.setLan("en");
        geographicLocationReachRepository.save(geographicLocationReach);


        GeographicExactness geographicExactness = null;

        geographicExactness = new GeographicExactness();
        geographicExactness.setCode("1");
        geographicExactness.setName("Exact");
        geographicExactness.setDescription("The designated geographic location is exact");
        geographicExactness.setLan("en");
        geographicExactnessRepository.save(geographicExactness);

        geographicExactness = new GeographicExactness();
        geographicExactness.setCode("2");
        geographicExactness.setName("Approximate");
        geographicExactness.setDescription("The designated geographic location is approximate");
        geographicExactness.setLan("en");
        geographicExactnessRepository.save(geographicExactness);

        GeographicLocationClass geographicLocationClass = null;

        geographicLocationClass = new GeographicLocationClass();
        geographicLocationClass.setCode("1");
        geographicLocationClass.setName("Administrative Region");
        geographicLocationClass.setDescription("The designated geographic location is an administrative region (state, county, province, district, municipality etc.)");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);

        geographicLocationClass = new GeographicLocationClass();
        geographicLocationClass.setCode("2");
        geographicLocationClass.setName("Populated Place");
        geographicLocationClass.setDescription("The designated geographic location is a populated place (town, village, farm etc.)");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);


        geographicLocationClass = new GeographicLocationClass();
        geographicLocationClass.setCode("3");
        geographicLocationClass.setName("Structure");
        geographicLocationClass.setDescription("The designated geopgraphic location is a structure (such as a school or a clinic)");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);


        geographicLocationClass = new GeographicLocationClass();
        geographicLocationClass.setCode("4");
        geographicLocationClass.setName("Other Topographical Feature");
        geographicLocationClass.setDescription("The designated geographic location is a topographical feature, such as a mountain, a river, a forest");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);


        Country country = null;
        country = new Country("AF", "AFGHANISTAN", "en");
        countryRepository.save(country);
        country = new Country("AX", "ÅLAND ISLANDS", "en");
        countryRepository.save(country);
        country = new Country("AL", "ALBANIA", "en");
        countryRepository.save(country);
        country = new Country("DZ", "ALGERIA", "en");
        countryRepository.save(country);
        country = new Country("AS", "AMERICAN SAMOA", "en");
        countryRepository.save(country);
        country = new Country("AD", "ANDORRA", "en");
        countryRepository.save(country);
        country = new Country("AO", "ANGOLA", "en");
        countryRepository.save(country);
        country = new Country("AI", "ANGUILLA", "en");
        countryRepository.save(country);
        country = new Country("AQ", "ANTARCTICA", "en");
        countryRepository.save(country);
        country = new Country("AG", "ANTIGUA AND BARBUDA", "en");
        countryRepository.save(country);
        country = new Country("AR", "ARGENTINA", "en");
        countryRepository.save(country);
        country = new Country("AM", "ARMENIA", "en");
        countryRepository.save(country);
        country = new Country("AW", "ARUBA", "en");
        countryRepository.save(country);
        country = new Country("AU", "AUSTRALIA", "en");
        countryRepository.save(country);
        country = new Country("AT", "AUSTRIA", "en");
        countryRepository.save(country);
        country = new Country("AZ", "AZERBAIJAN", "en");
        countryRepository.save(country);
        country = new Country("BS", "BAHAMAS (THE)", "en");
        countryRepository.save(country);
        country = new Country("BH", "BAHRAIN", "en");
        countryRepository.save(country);
        country = new Country("BD", "BANGLADESH", "en");
        countryRepository.save(country);
        country = new Country("BB", "BARBADOS", "en");
        countryRepository.save(country);
        country = new Country("BY", "BELARUS", "en");
        countryRepository.save(country);
        country = new Country("BE", "BELGIUM", "en");
        countryRepository.save(country);
        country = new Country("BZ", "BELIZE", "en");
        countryRepository.save(country);
        country = new Country("BJ", "BENIN", "en");
        countryRepository.save(country);
        country = new Country("BM", "BERMUDA", "en");
        countryRepository.save(country);
        country = new Country("BT", "BHUTAN", "en");
        countryRepository.save(country);
        country = new Country("BO", "BOLIVIA (PLURINATIONAL STATE OF)", "en");
        countryRepository.save(country);
        country = new Country("BQ", "BONAIRE, SAINT EUSTATIUS AND SABA", "en");
        countryRepository.save(country);
        country = new Country("BA", "BOSNIA AND HERZEGOVINA", "en");
        countryRepository.save(country);
        country = new Country("BW", "BOTSWANA", "en");
        countryRepository.save(country);
        country = new Country("BV", "BOUVET ISLAND", "en");
        countryRepository.save(country);
        country = new Country("BR", "BRAZIL", "en");
        countryRepository.save(country);
        country = new Country("IO", "BRITISH INDIAN OCEAN TERRITORY (THE)", "en");
        countryRepository.save(country);
        country = new Country("BN", "BRUNEI DARUSSALAM", "en");
        countryRepository.save(country);
        country = new Country("BG", "BULGARIA", "en");
        countryRepository.save(country);
        country = new Country("BF", "BURKINA FASO", "en");
        countryRepository.save(country);
        country = new Country("BI", "BURUNDI", "en");
        countryRepository.save(country);
        country = new Country("KH", "CAMBODIA", "en");
        countryRepository.save(country);
        country = new Country("CM", "CAMEROON", "en");
        countryRepository.save(country);
        country = new Country("CA", "CANADA", "en");
        countryRepository.save(country);
        country = new Country("CV", "CAPE VERDE", "en");
        countryRepository.save(country);
        country = new Country("KY", "CAYMAN ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("CF", "CENTRAL AFRICAN REPUBLIC (THE)", "en");
        countryRepository.save(country);
        country = new Country("TD", "CHAD", "en");
        countryRepository.save(country);
        country = new Country("CL", "CHILE", "en");
        countryRepository.save(country);
        country = new Country("CN", "CHINA", "en");
        countryRepository.save(country);
        country = new Country("CX", "CHRISTMAS ISLAND", "en");
        countryRepository.save(country);
        country = new Country("CC", "COCOS (KEELING) ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("CO", "COLOMBIA", "en");
        countryRepository.save(country);
        country = new Country("KM", "COMOROS (THE)", "en");
        countryRepository.save(country);
        country = new Country("CG", "CONGO (THE)", "en");
        countryRepository.save(country);
        country = new Country("CD", "CONGO, THE DEMOCRATIC REPUBLIC OF THE", "en");
        countryRepository.save(country);
        country = new Country("CK", "COOK ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("CR", "COSTA RICA", "en");
        countryRepository.save(country);
        country = new Country("CI", "CÃ”TE D'IVOIRE", "en");
        countryRepository.save(country);
        country = new Country("HR", "CROATIA", "en");
        countryRepository.save(country);
        country = new Country("CU", "CUBA", "en");
        countryRepository.save(country);
        country = new Country("CW", "CURAÃ‡AO", "en");
        countryRepository.save(country);
        country = new Country("CY", "CYPRUS", "en");
        countryRepository.save(country);
        country = new Country("CZ", "CZECH REPUBLIC (THE)", "en");
        countryRepository.save(country);
        country = new Country("DK", "DENMARK", "en");
        countryRepository.save(country);
        country = new Country("DJ", "DJIBOUTI", "en");
        countryRepository.save(country);
        country = new Country("DM", "DOMINICA", "en");
        countryRepository.save(country);
        country = new Country("DO", "DOMINICAN REPUBLIC (THE)", "en");
        countryRepository.save(country);
        country = new Country("EC", "ECUADOR", "en");
        countryRepository.save(country);
        country = new Country("EG", "EGYPT", "en");
        countryRepository.save(country);
        country = new Country("SV", "EL SALVADOR", "en");
        countryRepository.save(country);
        country = new Country("GQ", "EQUATORIAL GUINEA", "en");
        countryRepository.save(country);
        country = new Country("ER", "ERITREA", "en");
        countryRepository.save(country);
        country = new Country("EE", "ESTONIA", "en");
        countryRepository.save(country);
        country = new Country("ET", "ETHIOPIA", "en");
        countryRepository.save(country);
        country = new Country("FK", "FALKLAND ISLANDS (THE) [MALVINAS]", "en");
        countryRepository.save(country);
        country = new Country("FO", "FAROE ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("FJ", "FIJI", "en");
        countryRepository.save(country);
        country = new Country("FI", "FINLAND", "en");
        countryRepository.save(country);
        country = new Country("FR", "FRANCE", "en");
        countryRepository.save(country);
        country = new Country("GF", "FRENCH GUIANA", "en");
        countryRepository.save(country);
        country = new Country("PF", "FRENCH POLYNESIA", "en");
        countryRepository.save(country);
        country = new Country("TF", "FRENCH SOUTHERN TERRITORIES (THE)", "en");
        countryRepository.save(country);
        country = new Country("GA", "GABON", "en");
        countryRepository.save(country);
        country = new Country("GM", "GAMBIA (THE)", "en");
        countryRepository.save(country);
        country = new Country("GE", "GEORGIA", "en");
        countryRepository.save(country);
        country = new Country("DE", "GERMANY", "en");
        countryRepository.save(country);
        country = new Country("GH", "GHANA", "en");
        countryRepository.save(country);
        country = new Country("GI", "GIBRALTAR", "en");
        countryRepository.save(country);
        country = new Country("GR", "GREECE", "en");
        countryRepository.save(country);
        country = new Country("GL", "GREENLAND", "en");
        countryRepository.save(country);
        country = new Country("GD", "GRENADA", "en");
        countryRepository.save(country);
        country = new Country("GP", "GUADELOUPE", "en");
        countryRepository.save(country);
        country = new Country("GU", "GUAM", "en");
        countryRepository.save(country);
        country = new Country("GT", "GUATEMALA", "en");
        countryRepository.save(country);
        country = new Country("GG", "GUERNSEY", "en");
        countryRepository.save(country);
        country = new Country("GN", "GUINEA", "en");
        countryRepository.save(country);
        country = new Country("GW", "GUINEA-BISSAU", "en");
        countryRepository.save(country);
        country = new Country("GY", "GUYANA", "en");
        countryRepository.save(country);
        country = new Country("HT", "HAITI", "en");
        countryRepository.save(country);
        country = new Country("HM", "HEARD ISLAND AND MCDONALD ISLANDS", "en");
        countryRepository.save(country);
        country = new Country("VA", "HOLY SEE (THE)", "en");
        countryRepository.save(country);
        country = new Country("HN", "HONDURAS", "en");
        countryRepository.save(country);
        country = new Country("HK", "HONG KONG", "en");
        countryRepository.save(country);
        country = new Country("HU", "HUNGARY", "en");
        countryRepository.save(country);
        country = new Country("IS", "ICELAND", "en");
        countryRepository.save(country);
        country = new Country("IN", "INDIA", "en");
        countryRepository.save(country);
        country = new Country("ID", "INDONESIA", "en");
        countryRepository.save(country);
        country = new Country("IR", "IRAN (ISLAMIC REPUBLIC OF)", "en");
        countryRepository.save(country);
        country = new Country("IQ", "IRAQ", "en");
        countryRepository.save(country);
        country = new Country("IE", "IRELAND", "en");
        countryRepository.save(country);
        country = new Country("IM", "ISLE OF MAN", "en");
        countryRepository.save(country);
        country = new Country("IL", "ISRAEL", "en");
        countryRepository.save(country);
        country = new Country("IT", "ITALY", "en");
        countryRepository.save(country);
        country = new Country("JM", "JAMAICA", "en");
        countryRepository.save(country);
        country = new Country("JP", "JAPAN", "en");
        countryRepository.save(country);
        country = new Country("JE", "JERSEY", "en");
        countryRepository.save(country);
        country = new Country("JO", "JORDAN", "en");
        countryRepository.save(country);
        country = new Country("KZ", "KAZAKHSTAN", "en");
        countryRepository.save(country);
        country = new Country("KE", "KENYA", "en");
        countryRepository.save(country);
        country = new Country("KI", "KIRIBATI", "en");
        countryRepository.save(country);
        country = new Country("KP", "KOREA (DEMOCRATIC PEOPLE'S REPUBLIC OF)", "en");
        countryRepository.save(country);
        country = new Country("KR", "KOREA (REPUBLIC OF)", "en");
        countryRepository.save(country);
        country = new Country("XK", "KOSOVO", "en");
        countryRepository.save(country);
        country = new Country("KW", "KUWAIT", "en");
        countryRepository.save(country);
        country = new Country("KG", "KYRGYZSTAN", "en");
        countryRepository.save(country);
        country = new Country("LA", "LAO PEOPLE'S DEMOCRATIC REPUBLIC (THE)", "en");
        countryRepository.save(country);
        country = new Country("LV", "LATVIA", "en");
        countryRepository.save(country);
        country = new Country("LB", "LEBANON", "en");
        countryRepository.save(country);
        country = new Country("LS", "LESOTHO", "en");
        countryRepository.save(country);
        country = new Country("LR", "LIBERIA", "en");
        countryRepository.save(country);
        country = new Country("LY", "LIBYAN ARAB JAMAHIRIYA", "en");
        countryRepository.save(country);
        country = new Country("LI", "LIECHTENSTEIN", "en");
        countryRepository.save(country);
        country = new Country("LT", "LITHUANIA", "en");
        countryRepository.save(country);
        country = new Country("LU", "LUXEMBOURG", "en");
        countryRepository.save(country);
        country = new Country("MO", "MACAO", "en");
        countryRepository.save(country);
        country = new Country("MK", "MACEDONIA (THE FORMER YUGOSLAV REPUBLIC OF)", "en");
        countryRepository.save(country);
        country = new Country("MG", "MADAGASCAR", "en");
        countryRepository.save(country);
        country = new Country("MW", "MALAWI", "en");
        countryRepository.save(country);
        country = new Country("MY", "MALAYSIA", "en");
        countryRepository.save(country);
        country = new Country("MV", "MALDIVES", "en");
        countryRepository.save(country);
        country = new Country("ML", "MALI", "en");
        countryRepository.save(country);
        country = new Country("MT", "MALTA", "en");
        countryRepository.save(country);
        country = new Country("MH", "MARSHALL ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("MQ", "MARTINIQUE", "en");
        countryRepository.save(country);
        country = new Country("MR", "MAURITANIA", "en");
        countryRepository.save(country);
        country = new Country("MU", "MAURITIUS", "en");
        countryRepository.save(country);
        country = new Country("YT", "MAYOTTE", "en");
        countryRepository.save(country);
        country = new Country("MX", "MEXICO", "en");
        countryRepository.save(country);
        country = new Country("FM", "MICRONESIA (FEDERATED STATES OF)", "en");
        countryRepository.save(country);
        country = new Country("MD", "MOLDOVA (REPUBLIC OF)", "en");
        countryRepository.save(country);
        country = new Country("MC", "MONACO", "en");
        countryRepository.save(country);
        country = new Country("MN", "MONGOLIA", "en");
        countryRepository.save(country);
        country = new Country("ME", "MONTENEGRO", "en");
        countryRepository.save(country);
        country = new Country("MS", "MONTSERRAT", "en");
        countryRepository.save(country);
        country = new Country("MA", "MOROCCO", "en");
        countryRepository.save(country);
        country = new Country("MZ", "MOZAMBIQUE", "en");
        countryRepository.save(country);
        country = new Country("MM", "MYANMAR", "en");
        countryRepository.save(country);
        country = new Country("NA", "NAMIBIA", "en");
        countryRepository.save(country);
        country = new Country("NR", "NAURU", "en");
        countryRepository.save(country);
        country = new Country("NP", "NEPAL", "en");
        countryRepository.save(country);
        country = new Country("NL", "NETHERLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("AN", "NETHERLAND ANTILLES", "en");
        countryRepository.save(country);
        country = new Country("NC", "NEW CALEDONIA", "en");
        countryRepository.save(country);
        country = new Country("NZ", "NEW ZEALAND", "en");
        countryRepository.save(country);
        country = new Country("NI", "NICARAGUA", "en");
        countryRepository.save(country);
        country = new Country("NE", "NIGER (THE)", "en");
        countryRepository.save(country);
        country = new Country("NG", "NIGERIA", "en");
        countryRepository.save(country);
        country = new Country("NU", "NIUE", "en");
        countryRepository.save(country);
        country = new Country("NF", "NORFOLK ISLAND", "en");
        countryRepository.save(country);
        country = new Country("MP", "NORTHERN MARIANA ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("NO", "NORWAY", "en");
        countryRepository.save(country);
        country = new Country("OM", "OMAN", "en");
        countryRepository.save(country);
        country = new Country("PK", "PAKISTAN", "en");
        countryRepository.save(country);
        country = new Country("PW", "PALAU", "en");
        countryRepository.save(country);
        country = new Country("PS", "PALESTINIAN TERRITORY, OCCUPIED", "en");
        countryRepository.save(country);
        country = new Country("PA", "PANAMA", "en");
        countryRepository.save(country);
        country = new Country("PG", "PAPUA NEW GUINEA", "en");
        countryRepository.save(country);
        country = new Country("PY", "PARAGUAY", "en");
        countryRepository.save(country);
        country = new Country("PE", "PERU", "en");
        countryRepository.save(country);
        country = new Country("PH", "PHILIPPINES (THE)", "en");
        countryRepository.save(country);
        country = new Country("PN", "PITCAIRN", "en");
        countryRepository.save(country);
        country = new Country("PL", "POLAND", "en");
        countryRepository.save(country);
        country = new Country("PT", "PORTUGAL", "en");
        countryRepository.save(country);
        country = new Country("PR", "PUERTO RICO", "en");
        countryRepository.save(country);
        country = new Country("QA", "QATAR", "en");
        countryRepository.save(country);
        country = new Country("RE", "REUNION", "en");
        countryRepository.save(country);
        country = new Country("RO", "ROMANIA", "en");
        countryRepository.save(country);
        country = new Country("RU", "RUSSIAN FEDERATION (THE)", "en");
        countryRepository.save(country);
        country = new Country("RW", "RWANDA", "en");
        countryRepository.save(country);
        country = new Country("BL", "SAINT BARTHÃ‰LEMY", "en");
        countryRepository.save(country);
        country = new Country("SH", "SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA", "en");
        countryRepository.save(country);
        country = new Country("KN", "SAINT KITTS AND NEVIS", "en");
        countryRepository.save(country);
        country = new Country("LC", "SAINT LUCIA", "en");
        countryRepository.save(country);
        country = new Country("MF", "SAINT MARTIN (FRENCH PART)", "en");
        countryRepository.save(country);
        country = new Country("PM", "SAINT PIERRE AND MIQUELON", "en");
        countryRepository.save(country);
        country = new Country("VC", "SAINT VINCENT AND THE GRENADINES", "en");
        countryRepository.save(country);
        country = new Country("WS", "SAMOA", "en");
        countryRepository.save(country);
        country = new Country("SM", "SAN MARINO", "en");
        countryRepository.save(country);
        country = new Country("ST", "SAO TOME AND PRINCIPE", "en");
        countryRepository.save(country);
        country = new Country("SA", "SAUDI ARABIA", "en");
        countryRepository.save(country);
        country = new Country("SN", "SENEGAL", "en");
        countryRepository.save(country);
        country = new Country("RS", "SERBIA", "en");
        countryRepository.save(country);
        country = new Country("SC", "SEYCHELLES", "en");
        countryRepository.save(country);
        country = new Country("SL", "SIERRA LEONE", "en");
        countryRepository.save(country);
        country = new Country("SG", "SINGAPORE", "en");
        countryRepository.save(country);
        country = new Country("SX", "SINT MAARTEN (DUTCH PART)", "en");
        countryRepository.save(country);
        country = new Country("SK", "SLOVAKIA", "en");
        countryRepository.save(country);
        country = new Country("SI", "SLOVENIA", "en");
        countryRepository.save(country);
        country = new Country("SB", "SOLOMON ISLANDS", "en");
        countryRepository.save(country);
        country = new Country("SO", "SOMALIA", "en");
        countryRepository.save(country);
        country = new Country("ZA", "SOUTH AFRICA", "en");
        countryRepository.save(country);
        country = new Country("GS", "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "en");
        countryRepository.save(country);
        country = new Country("SS", "SOUTH SUDAN", "en");
        countryRepository.save(country);
        country = new Country("ES", "SPAIN", "en");
        countryRepository.save(country);
        country = new Country("LK", "SRI LANKA", "en");
        countryRepository.save(country);
        country = new Country("SD", "SUDAN (THE)", "en");
        countryRepository.save(country);
        country = new Country("SR", "SURINAME", "en");
        countryRepository.save(country);
        country = new Country("SJ", "SVALBARD AND JAN MAYEN", "en");
        countryRepository.save(country);
        country = new Country("SZ", "SWAZILAND", "en");
        countryRepository.save(country);
        country = new Country("SE", "SWEDEN", "en");
        countryRepository.save(country);
        country = new Country("CH", "SWITZERLAND", "en");
        countryRepository.save(country);
        country = new Country("SY", "SYRIAN ARAB REPUBLIC", "en");
        countryRepository.save(country);
        country = new Country("TW", "TAIWAN (PROVINCE OF CHINA)", "en");
        countryRepository.save(country);
        country = new Country("TJ", "TAJIKISTAN", "en");
        countryRepository.save(country);
        country = new Country("TZ", "TANZANIA, UNITED REPUBLIC OF", "en");
        countryRepository.save(country);
        country = new Country("TH", "THAILAND", "en");
        countryRepository.save(country);
        country = new Country("TL", "TIMOR-LESTE", "en");
        countryRepository.save(country);
        country = new Country("TG", "TOGO", "en");
        countryRepository.save(country);
        country = new Country("TK", "TOKELAU", "en");
        countryRepository.save(country);
        country = new Country("TO", "TONGA", "en");
        countryRepository.save(country);
        country = new Country("TT", "TRINIDAD AND TOBAGO", "en");
        countryRepository.save(country);
        country = new Country("TN", "TUNISIA", "en");
        countryRepository.save(country);
        country = new Country("TR", "TURKEY", "en");
        countryRepository.save(country);
        country = new Country("TM", "TURKMENISTAN", "en");
        countryRepository.save(country);
        country = new Country("TC", "TURKS AND CAICOS ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("TV", "TUVALU", "en");
        countryRepository.save(country);
        country = new Country("UG", "UGANDA", "en");
        countryRepository.save(country);
        country = new Country("UA", "UKRAINE", "en");
        countryRepository.save(country);
        country = new Country("AE", "UNITED ARAB EMIRATES (THE)", "en");
        countryRepository.save(country);
        country = new Country("GB", "UNITED KINGDOM OF GREAT BRITAIN AND NORTHERN IRELAND (THE)", "en");
        countryRepository.save(country);
        country = new Country("US", "UNITED STATES OF AMERICA (THE)", "en");
        countryRepository.save(country);
        country = new Country("UM", "UNITED STATES MINOR OUTLYING ISLANDS (THE)", "en");
        countryRepository.save(country);
        country = new Country("UY", "URUGUAY", "en");
        countryRepository.save(country);
        country = new Country("UZ", "UZBEKISTAN", "en");
        countryRepository.save(country);
        country = new Country("VU", "VANUATU", "en");
        countryRepository.save(country);
        country = new Country("VE", "VENEZUELA (BOLIVARIAN REPUBLIC OF)", "en");
        countryRepository.save(country);
        country = new Country("VN", "VIET NAM", "en");
        countryRepository.save(country);
        country = new Country("VG", "VIRGIN ISLANDS (BRITISH)", "en");
        countryRepository.save(country);
        country = new Country("VI", "VIRGIN ISLANDS (U.S.)", "en");
        countryRepository.save(country);
        country = new Country("WF", "WALLIS AND FUTUNA", "en");
        countryRepository.save(country);
        country = new Country("EH", "WESTERN SAHARA", "en");
        countryRepository.save(country);
        country = new Country("YE", "YEMEN", "en");
        countryRepository.save(country);
        country = new Country("ZM", "ZAMBIA", "en");
        countryRepository.save(country);
        country = new Country("ZW", "ZIMBABWE", "en");
        countryRepository.save(country);


        File in = new File(this.getClass().getClassLoader().getResource("example_afdb.xml").getPath());
        try {
            xmlImport.process(new FileInputStream(in), "en");
            Assert.isTrue(activityRepository.count() > 0, "There are no activities");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
