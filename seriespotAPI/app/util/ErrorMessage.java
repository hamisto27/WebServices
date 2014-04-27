package util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


import javax.xml.bind.JAXBContext;
import play.libs.Json;

import javax.xml.bind.*;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;


@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "title", "code", "messagge"})
@JsonRootName("error")
public class ErrorMessage{

	@XmlElement(name="title")
	@JsonProperty("title")	
	public String title;

	@XmlElement(name="code")
	@JsonProperty("code")
	public Integer code;

	@XmlElement(name="messagge")
	@JsonProperty("messagge")	
	public String messagge;

	public ErrorMessage(){
		;
	}
	public ErrorMessage(String title, Integer code, String messagge) {

	  	this.title = title;
	  	this.code = code;
		this.messagge = messagge;

	}


	public String marshalError()  throws javax.xml.bind.JAXBException, JsonProcessingException{

		String contentType = (String) play.mvc.Http.Context.current().args.get("ContentTypeResponse");   
        if(contentType.equals("Application/json")){

        	return marshalConfig();
        }
        else if(contentType.equals("Application/xml")){

        	JAXBContext context = JAXBContext.newInstance(this.getClass());

        	Marshaller marshaller = context.createMarshaller();
        	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        	marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        	StringWriter stringWriter = new StringWriter();
        	marshaller.marshal(this, stringWriter);

        	return stringWriter.toString();
		}

		return null;
	}

	private String marshalConfig() throws JsonProcessingException{

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		ObjectWriter writer = objectMapper.writerWithType(ErrorMessage.class);
       	return writer.writeValueAsString(this);
	}

	

}