package org.devgateway.geocoder.iati;

import org.devgateway.geocoder.iati.model.IatiActivities;
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

    private File in;

    private IatiActivities iatiActivities;

    public ActivitiesReader(File in) {
        this.in = in;
    }


    public Float getVersion() {
        Float version = null;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final XPathFactory xpathFactory = XPathFactory.newInstance();
            final XPath xpath = xpathFactory.newXPath();

            final DocumentBuilder builder = factory.newDocumentBuilder();
            final org.w3c.dom.Document doc = builder.parse(this.in);

            final String value = (String) xpath.evaluate("/iati-activities/@version", doc, XPathConstants.STRING);
            if (value != null) {
                version = Float.parseFloat(value);
            }
        } catch (Exception e) {
            log.warning("Error when parsing xml");
        }

        return version;
    }

    public ArrayList<String> validate() {
        final ArrayList<String> validationErrors = new ArrayList<>();

        try {
            Float version = this.getVersion();

            if (version < 2.00f) {
                validationErrors.add("Version " + version.toString() + " is not supported");
            } else {
                String xsdpah = null;
                if (version == 2.02f) {
                    xsdpah = "xsd202/iati-activities-schema.xsd";
                } else if (version == 2.01f) {
                    xsdpah = "xsd201/iati-activities-schema.xsd";
                }

                if (xsdpah != null) {
                    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Schema schema;

                    schema = sf.newSchema(this.getClass().getClassLoader().getResource(xsdpah));

                    final Validator validator = schema.newValidator();
                    final org.devgateway.geocoder.iati.ErrorHandler errorHandler = new org.devgateway.geocoder.iati.ErrorHandler();
                    validator.setErrorHandler(errorHandler);
                    validator.validate(new StreamSource(new FileInputStream(this.in)));

                    if (errorHandler.getErrors().size() > 0) {
                        validationErrors.addAll(errorHandler.getErrors());
                    }
                } else {
                    validationErrors.add("Can't validate this version");
                }
            }
        } catch (SAXException | IOException e) {
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

    public void toXML(final IatiActivities iatiActivities, final Writer w) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivities.class);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(iatiActivities, w);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
