package util;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

import javax.xml.bind.annotation.*;


import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "VERSION", "href", "items"})
@XmlRootElement(name = "collection")
@JsonRootName("collection")
public class JsonWrapper<T extends HypermediaProvider> {

	@XmlAttribute(name="version")
	@JsonProperty("version")
	private final String VERSION = "1.0";

	@XmlAttribute(name="href")
	@JsonProperty("href")
	private String href;

	@XmlElementWrapper(name = "items")
	@XmlElement(name="item")
	@JsonProperty("items")
	private List<Item> items = new ArrayList<Item>();


	public JsonWrapper(){
		;
	}

	public JsonWrapper(List<T> list, String href) {
		
		this.href = href;
		for (int i = 0; i < list.size(); i ++) {
			items.add(new Item<T>(list.get(i), list.get(i).getLinks(), list.get(i).getHrefResource()));
		}
    }

}