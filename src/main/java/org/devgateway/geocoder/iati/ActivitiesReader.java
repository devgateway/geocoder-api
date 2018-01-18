package org.devgateway.geocoder.iati;

import org.devgateway.geocoder.iati.model.IatiActivities;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */
public class ActivitiesReader {
    Logger log = Logger.getLogger(this.getClass().getName());

    private final File in;

    public ActivitiesReader(final File in) {
        this.in = in;
    }

    public Float getVersion() {
        Float version = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(this.in);


            String value = (String) xpath.evaluate("/iati-activities/@version", doc, XPathConstants.STRING);
            if (value != null) {
                version = Float.parseFloat(value);
            }

        } catch (Exception e) {
            log.warning("Error when parsing xml");
        }
        return version;
    }

    public ArrayList<String> validate() {
        ArrayList<String> validationErrors = new ArrayList<>();
        try {
            Float version = this.getVersion();

            if (version < 2.00) {
                validationErrors.add("Version " + version.toString() + " is not supported");
            } else {

                String xsdpah;
                if (version == 2.02) {
                    xsdpah = "xsd202/iati-activities-schema.xsd";

                } else {
                    xsdpah = "xsd201/iati-activities-schema.xsd";
                }
                SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema;

                schema = sf.newSchema(this.getClass().getClassLoader().getResource(xsdpah));

                Validator validator = schema.newValidator();
                org.devgateway.geocoder.iati.ErrorHandler errorHandler = new org.devgateway.geocoder.iati.ErrorHandler();
                validator.setErrorHandler(errorHandler);
                validator.validate(new StreamSource(new FileInputStream(this.in)));

                if (errorHandler.getErrors().size() > 0) {
                    validationErrors.addAll(errorHandler.getErrors());
                }
            }
        } catch (SAXException e) {
            log.warning("Error when validating");
            return null;

        } catch (IOException e) {
            log.warning("Error when validating");
            return null;
        }

        return validationErrors;
    }

    public IatiActivities read() {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivities.class);
            final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            final IatiActivities iatiActivities = (IatiActivities) jaxbUnmarshaller.unmarshal(this.in);
            return iatiActivities;
        } catch (JAXBException e) {
            log.warning("Error when reading activities");
        }
        return null;
    }

    public void toXML(final IatiActivity iatiActivity, final Writer w) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivity.class);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(iatiActivity, w);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
