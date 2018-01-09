package org.devgateway.geocoder.iati;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sebastian Dimunzio
 */

public class ErrorHandler implements org.xml.sax.ErrorHandler {


    List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public ErrorHandler() {
        this.errors = new ArrayList<>();
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {

        errors.add(exception.getMessage());
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {

        errors.add(exception.getMessage());

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {

        errors.add(exception.getMessage());

    }
}
