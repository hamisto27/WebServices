package util;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

import javax.xml.bind.annotation.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "hrefResource", "e", "links" })
@XmlRootElement(name = "item")
@JsonRootName("item")
public class Item<E> {

	@XmlAttribute(name = "href")
	@JsonProperty("href")
	private String hrefResource;

	@XmlElement(name = "data")
	@JsonProperty("data")
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

}