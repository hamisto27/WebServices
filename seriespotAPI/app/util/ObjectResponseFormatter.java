package util;

import java.util.List;

import javax.xml.bind.JAXBContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import play.Logger;

import models.Series;

public class ObjectResponseFormatter {


    public static <T extends HypermediaProvider> String objectResponse(T object) throws JAXBException, JsonProcessingException{

        String contentType = (String) play.mvc.Http.Context.current().args.get("ContentTypeResponse");   

        if(contentType.equals("Application/json")){


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            ObjectWriter writer = objectMapper.writerWithType(Item.class);
            return writer.writeValueAsString(new Item<T>(object, object.getLinks(), object.getHrefResource()));
        }
        else if(contentType.equals("Application/xml") || contentType.equals("Application/xhtml+xml")){

            Item<T> item = new Item<T>(object, object.getLinks(), object.getHrefResource());
            JAXBContext context = JAXBContext.newInstance(Item.class, object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(item, stringWriter);
            
            if(contentType.equals("Application/xhtml+xml")){
            	return HtmlGenerator.generator(stringWriter.toString());
            }
            
            
            return stringWriter.toString();

        }

        return null;

    }

    @SuppressWarnings("rawtypes")
	public static <T extends HypermediaProvider> String objectListResponse(List<T> objects, Class<?> clazz, String href) throws JAXBException, JsonProcessingException{

        String contentType = (String) play.mvc.Http.Context.current().args.get("ContentTypeResponse");   

        if(contentType.equals("Application/json")){

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            ObjectWriter writer = objectMapper.writerWithType(CollectionWrapper.class);
            
            return writer.writeValueAsString(new CollectionWrapper<T>(objects, href));
        }
        else if(contentType.equals("Application/xml") || contentType.equals("Application/xhtml+xml")){

            JAXBContext context = JAXBContext.newInstance(CollectionWrapper.class, clazz);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");


            StringWriter stringWriter = new StringWriter();

            JAXBElement<CollectionWrapper> jaxbElement = new JAXBElement<CollectionWrapper>(new QName("collection"),
            CollectionWrapper.class, new CollectionWrapper<T>(objects, href));
            marshaller.marshal(jaxbElement, stringWriter);
            
            if(contentType.equals("Application/xhtml+xml")){
            	return HtmlGenerator.generator(stringWriter.toString());
            }
            
            return stringWriter.toString();
        }

        return null;

    }


}