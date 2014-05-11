package util;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "href", "rel", "method"})
public class Link {

	@XmlAttribute(name="href")
	@JsonProperty("href")	
	public String href;

	@XmlAttribute(name="rel")
	@JsonProperty("rel")
	public String rel;
	
	@XmlAttribute(name="method")
	@JsonProperty("method")
	public String method;

	public Link(){
		;
	}
	
	public Link(String href, String rel) {

	  	this.href = href;
		this.rel = rel;
	}
	
	public Link(String href, String rel, String method) {

	  	this.href = href;
		this.rel = rel;
		this.method = method;
	}
	

}