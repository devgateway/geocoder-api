package org.devgateway.geocoder;

import org.devgateway.geocoder.domain.*;
import org.devgateway.geocoder.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

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
        GeographicVocabulary geographicVocabulary=null;
        geographicVocabulary=new GeographicVocabulary();
        geographicVocabulary.setCode("A1");
        geographicVocabulary.setName("Global Admininistrative Unit Layers");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);


        geographicVocabulary=new GeographicVocabulary();
        geographicVocabulary.setCode("A2");
        geographicVocabulary.setName("UN Second Administrative Level Boundary Project");
        geographicVocabulary.setDescription("Note: the unsalb.org website is no longer accessible, and public access to the boundaries resources has been removed http://www.ungiwg.org/content/united-nations-international-and-administrative-boundaries-resources");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);

        geographicVocabulary=new GeographicVocabulary();
        geographicVocabulary.setCode("A3");
        geographicVocabulary.setName("Global Administrative Areas");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);

        geographicVocabulary=new GeographicVocabulary();
        geographicVocabulary.setCode("A4");
        geographicVocabulary.setName("ISO Country (3166-1 alpha-2)");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);

        geographicVocabulary=new GeographicVocabulary();
        geographicVocabulary.setCode("G1");
        geographicVocabulary.setName("Geonames");
        geographicVocabulary.setLan("en");
        geographicVocabularyRepository.save(geographicVocabulary);


        geographicVocabulary=new GeographicVocabulary();
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

        GeographicLocationClass geographicLocationClass=null;

        geographicLocationClass=new GeographicLocationClass();
        geographicLocationClass.setCode("1");
        geographicLocationClass.setName("Administrative Region");
        geographicLocationClass.setDescription("The designated geographic location is an administrative region (state, county, province, district, municipality etc.)");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);

        geographicLocationClass=new GeographicLocationClass();
        geographicLocationClass.setCode("2");
        geographicLocationClass.setName("Populated Place");
        geographicLocationClass.setDescription("The designated geographic location is a populated place (town, village, farm etc.)");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);


        geographicLocationClass=new GeographicLocationClass();
        geographicLocationClass.setCode("3");
        geographicLocationClass.setName("Structure");
        geographicLocationClass.setDescription("The designated geopgraphic location is a structure (such as a school or a clinic)");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);


        geographicLocationClass=new GeographicLocationClass();
        geographicLocationClass.setCode("4");
        geographicLocationClass.setName("Other Topographical Feature");
        geographicLocationClass.setDescription("The designated geographic location is a topographical feature, such as a mountain, a river, a forest");
        geographicLocationClass.setLan("en");
        geographicLocationClassRepository.save(geographicLocationClass);


    }
}
