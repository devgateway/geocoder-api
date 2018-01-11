package org.devgateway.geocoder.iati;

import org.devgateway.geocoder.iati.model.IatiActivities;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */
public class ActivitiesReader {
    Logger log = Logger.getLogger(this.getClass().getName());

    private File in;
    private HashMap<String, List<String>> validationErrors;
    private IatiActivities iatiActivities;

    public ActivitiesReader(File in) {
        this.validationErrors=new HashMap<>();
        this.in = in;

    }

    public List<String> getValidationErrors(String version) {
        return validationErrors.get(version);
    }

    public Boolean validate(String version) {
        try {
            String xsdpah = "xsd" + version + "/iati-activities-schema.xsd";

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = null;

            final Boolean is202;
            schema = sf.newSchema(this.getClass().getClassLoader().getResource(xsdpah));

            Validator validator = schema.newValidator();
            org.devgateway.geocoder.iati.ErrorHandler errorHandler = new org.devgateway.geocoder.iati.ErrorHandler();
            validator.setErrorHandler(errorHandler);
            validator.validate(new StreamSource(new FileInputStream(this.in)));

            if (errorHandler.getErrors().size() > 0) {
                this.validationErrors.put(version, errorHandler.getErrors());
                return false;
            } else {
                return true;
            }


        } catch (SAXException e) {
            log.warning("Error when validating");
            return false;

        } catch (IOException e) {
            log.warning("Error when validating");
            return false;

        }
    }

    public IatiActivities read() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivities.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            iatiActivities = (IatiActivities) jaxbUnmarshaller.unmarshal(this.in);
            return iatiActivities;

        } catch (JAXBException e) {
            log.warning("Error when reading activities");
        }
        return null;
    }

    public void toXML(IatiActivity iatiActivity, Writer w) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(IatiActivity.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(iatiActivity, w);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


}
