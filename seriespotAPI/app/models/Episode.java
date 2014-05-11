package models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import util.HypermediaProvider;
import util.Link;

@XmlRootElement(name = "episode")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "name", "number","overview", "firstAired"})
@JsonInclude(Include.NON_NULL) 
public class Episode implements HypermediaProvider{

	@XmlElement
    public String id;

	@XmlTransient
	@JsonIgnore
	private Season season;

	@XmlElement
	private Integer number;

	@XmlElement
    private String name;
	
	@XmlElement
    private String overview;

	@XmlElement
    private String firstAired;
    
    public Episode() {
		;
	}
    
    public Episode(Season season) {
		
    	setSeason(season);
	}
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public List<Link> getLinks() {
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("/series/" + getSeason().getSeries().getId() + "/seasons/" + getSeason().getId(), "season", "GET"));

		return links;
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public String getHrefResource() {
		
		return "/series/" + getSeason().getSeries().getId() + "/seasons/" + getSeason().getId() + "/episodes/" + getId();
	}

}
