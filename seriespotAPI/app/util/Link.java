package util;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "href", "rel"})
public class Link {

	@XmlAttribute(name="href")
	@JsonProperty("href")	
	public String href;

	@XmlAttribute(name="rel")
	@JsonProperty("rel")
	public String rel;

	public Link(){
		;
	}
	public Link(String href, String rel) {

	  	this.href = href;
		this.rel = rel;
	}

}