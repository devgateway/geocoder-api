package org.devgateway.geocoder.iati;

import org.devgateway.geocoder.iati.model.IatiActivity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author idobre
 * @since 18/01/2018
 *
 * Created this class a {@link Service} since getting a new {@link JAXBContext} instance is expensive.
 */
@Service
public class ActivityReader {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private JAXBContext jaxbContext;

    private  Marshaller marshaller;

    private Unmarshaller jaxbUnmarshaller;

    @PostConstruct
    public void init() {
        try {
            jaxbContext = JAXBContext.newInstance(IatiActivity.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "Error generating JAXBContext", e);
        }
    }

    public IatiActivity read(final String xml) {
        try {
            final IatiActivity iatiActivity = (IatiActivity) jaxbUnmarshaller.unmarshal(
                    new ByteArrayInputStream(xml.getBytes()));

            return iatiActivity;
        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "Error when reading the activity", e);
        }

        return null;
    }

    public void toXML(final IatiActivity iatiActivity, final Writer w) {
        try {
            marshaller.marshal(iatiActivity, w);
        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "Error when writing the activity", e);
        }
    }
}
