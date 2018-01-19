package org.devgateway.geocoder.iati;

import org.devgateway.geocoder.iati.model.IatiActivity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

/**
 * @author idobre
 * @since 18/01/2018
 */
public class ActivityReader {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final String xml;


    public ActivityReader(final String xml) {
        this.xml = xml;
    }

    public IatiActivity read() {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(IatiActivity.class);
            final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            final IatiActivity iatiActivity = (IatiActivity) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

            return iatiActivity;
        } catch (JAXBException e) {
            logger.warning("Error when reading the activity");
        }

        return null;
    }
}
