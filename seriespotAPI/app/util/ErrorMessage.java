package util;

import com.fasterxml.jackson.annotation.*;

import javax.xml.bind.annotation.*;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.*;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;


@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "title", "code", "message"})
@JsonRootName("error")
public class ErrorMessage{

	@XmlElement(name="title")
	@JsonProperty("title")	
	public String title;

	@XmlElement(name="code")
	@JsonProperty("code")
	public Integer code;

	@XmlElement(name="message")
	@JsonProperty("message")	
	public String message;

	public ErrorMessage(){
		;
	}
	public ErrorMessage(String title, Integer code, String message) {

	  	this.title = title;
	  	this.code = code;
		this.message = message;

	}


	public String marshalError()  throws javax.xml.bind.JAXBException, JsonProcessingException{

		String contentType = (String) play.mvc.Http.Context.current().args.get("ContentTypeResponse");   
        if(contentType.equals("Application/json")){

        	return marshalConfig();
        }
        else if(contentType.equals("Application/xml") || contentType.equals("Application/xhtml+xml")){

        	JAXBContext context = JAXBContext.newInstance(this.getClass());

        	Marshaller marshaller = context.createMarshaller();
        	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        	marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        	StringWriter stringWriter = new StringWriter();
        	marshaller.marshal(this, stringWriter);
        	
        	 if(contentType.equals("Application/xhtml+xml")){
        		 System.out.println("fuck!!!!!");
             	return HtmlGenerator.generator(stringWriter.toString());
             }
        	 
        	return stringWriter.toString();
		}

		return null;
	}

	private String marshalConfig() throws JsonProcessingException{

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		ObjectWriter writer = objectMapper.writerWithType(ErrorMessage.class);
       	return writer.writeValueAsString(this);
	}

	

}