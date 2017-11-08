package org.devgateway.geocoder.service;

import org.devgateway.geocoder.iati.ActivitiesReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class Reader {

    @Autowired XmlImport xmlImport;

    @Test
    public void testReader( ){


        File in =new File("C:\\projects\\spring-boot-sample-data-jpa\\src\\main\\resources\\example.xml");

        ActivitiesReader reader= null;
        try {
            //reader = new ActivitiesReader();
            xmlImport.process(new FileInputStream(in),"en");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
