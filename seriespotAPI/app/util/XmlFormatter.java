package util;
import util.Wrapper;

import java.util.List;
import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import org.w3c.dom.Document;

public class XmlFormatter {


    /**
     * Wrap List in Wrapper, then leverage JAXBElement to supply root element 
     * information.
     */
      
    @SuppressWarnings("unchecked")
    public static String marshalList(JAXBContext context, List<?> list, String name)
            throws JAXBException {

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        QName qName = new QName(name);
        Wrapper wrapper = new Wrapper(list);
        StringWriter stringWriter = new StringWriter();
        JAXBElement<Wrapper> jaxbElement = new JAXBElement<Wrapper>(qName,
                Wrapper.class, wrapper);
        marshaller.marshal(jaxbElement, stringWriter);
        return stringWriter.toString();

    }


    @SuppressWarnings("unchecked")
    public static <T> String marshalObject(JAXBContext context, T object)
            throws JAXBException {

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(object, stringWriter);

        return stringWriter.toString();

    }

    
    // Unmarshal XML to Wrapper and return List value.


    @SuppressWarnings("unchecked")
    public static <T> List<T> unmarshalList(JAXBContext context, Document doc) throws JAXBException {
        
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(doc,
                Wrapper.class).getValue();
        return wrapper.getItems();
    }

    @SuppressWarnings("unchecked")
    public static Object unmarshalObject(Class<?> clazz, Document doc) throws JAXBException {
        
        JAXBContext context = JAXBContext.newInstance(clazz); 
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(doc);
    }



}
