package org.devgateway.geocoder.service;

import org.devgateway.geocoder.iati.ActivitiesReader;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportTest {

    @Autowired
    XmlImport xmlImport;
    @Autowired
    ActivityRepository activityRepository;

    @Test
    public void testFileImport() {
        File in = new File(this.getClass().getClassLoader().getResource("example.xml").getPath());
        try {
            xmlImport.process(new FileInputStream(in), "en");
            Assert.isTrue(activityRepository.count() > 0, "There are no activities");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
