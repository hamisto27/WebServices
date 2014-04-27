package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.*;
import util.*;


@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "name", "overview"})
@JsonInclude(Include.NON_NULL) 
public class BaseSeries implements HypermediaProvider {

	@XmlElement(name="id", required = true)
    @JsonProperty("id")
	private String id;

	@XmlElement(name="name", required = true)
    @JsonProperty("name")
	private String name;

	@XmlElement(name="overview", required = true)
    @JsonProperty("overview")
	private String overview;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getSeriesLink(){
		return null;
	}


	@Override
    public List<Link> getLinks(){

        return null;
    }

    @Override
    public String getHrefResource(){

        return null;
    }

}