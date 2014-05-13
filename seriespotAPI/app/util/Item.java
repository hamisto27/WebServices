package util;

import java.util.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.xml.bind.annotation.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "hrefResource", "e", "links" })
@XmlRootElement(name = "item")
@JsonRootName("item")
@JsonInclude(Include.NON_NULL) 
public class Item<E> {

	@XmlAttribute(name = "href")
	@JsonProperty("href")
	private String hrefResource;

	@XmlElement(name = "data")
	@JsonProperty("data")
	@JsonSerialize
	private E e;

	@XmlElementWrapper(name = "links")
	@XmlElement(name = "link")
	@JsonProperty("links")
	private List<Link> links;

	public Item() {
		;
	}

	
	public Item(E e, List<Link> links, String hrefResource) {

		this.e = e;
		this.links = links;
		this.hrefResource = hrefResource;

	}

	public String getHrefResource() {
		return hrefResource;
	}

	public void setHrefResource(String hrefResource) {
		this.hrefResource = hrefResource;
	}

	public E getE() {
		return e;
	}

	public void setE(E e) {
		this.e = e;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	

}