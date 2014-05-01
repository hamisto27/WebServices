package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.xml.bind.annotation.*;

import play.db.ebean.Model;

import java.util.*;

import util.*;


@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "name", "overview"})
@JsonInclude(Include.NON_NULL) 
public class BaseSeries extends Model implements HypermediaProvider {

	private static final long serialVersionUID = 1L;

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
	@JsonIgnore
	@XmlTransient
	public List<Link> getLinks() {

		return null;
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public String getHrefResource() {

		return null;
	}





}