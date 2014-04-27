package util;

import play.*;
import play.mvc.*;
import play.libs.F.*;

import java.util.*;
import java.util.List;

import javax.xml.bind.JAXBContext;
import play.libs.Json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;

import models.*;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;

public class ObjectResponseFormatter {


    @SuppressWarnings("unchecked")
    public static <T extends HypermediaProvider> String objectResponse(T object) throws JAXBException, JsonProcessingException{

        String contentType = (String) play.mvc.Http.Context.current().args.get("ContentTypeResponse");   

        if(contentType.equals("Application/json")){


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            ObjectWriter writer = objectMapper.writerWithType(Item.class);
            return writer.writeValueAsString(new Item(object, object.getLinks(), object.getHrefResource()));
        }
        else if(contentType.equals("Application/xml")){

            Item item = new Item(object, object.getLinks(), object.getHrefResource());
            JAXBContext context = JAXBContext.newInstance(Item.class, object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(item, stringWriter);

            return stringWriter.toString();

        }

        return null;

    }

    @SuppressWarnings("unchecked")
    public static <T> String objectListResponse(List<T> objects, Class<?> clazz, String href) throws JAXBException, JsonProcessingException{

        String contentType = (String) play.mvc.Http.Context.current().args.get("ContentTypeResponse");   

        if(contentType.equals("Application/json")){

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            ObjectWriter writer = objectMapper.writerWithType(JsonWrapper.class);
            
            return writer.writeValueAsString(new JsonWrapper(objects, href));
        }
        else if(contentType.equals("Application/xml")){

            JAXBContext context = JAXBContext.newInstance(JsonWrapper.class, clazz);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");


            StringWriter stringWriter = new StringWriter();

            JAXBElement<JsonWrapper> jaxbElement = new JAXBElement<JsonWrapper>(new QName("Collection"),
            JsonWrapper.class, new JsonWrapper(objects, href));
            marshaller.marshal(jaxbElement, stringWriter);

            return stringWriter.toString();
        }

        return null;

    }


}