package org.devgateway.geocoder.iati;

import org.devgateway.geocoder.iati.model.IatiActivities;
import org.devgateway.geocoder.iati.model.IatiActivity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * Created by Sebastian Dimunzio on 10/18/2017.
 */
public class ActivitiesReader {
    Logger log = Logger.getLogger(this.getClass().getName());
    private InputStream in = null;
    private IatiActivities iatiActivities = null;

    public ActivitiesReader(InputStream in) {
        this.in = in;
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
