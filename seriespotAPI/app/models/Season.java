package models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
@XmlType(propOrder = { "id", "number", "episodes"})
@JsonInclude(Include.NON_NULL) 
public class Season implements HypermediaProvider{
	
	@XmlElement
    public String id;
	
	@XmlElement
	private Integer number;

	@XmlTransient
	@JsonIgnore
	private Series series;
	
	@XmlElementWrapper(name = "episodes")
	@XmlElement(name = "episode")
	private List<Episode> episodes;
	
	public Season() {
		;
	}
	
	public Season(Series series) {
		
		setSeries(series);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}
	
	public List<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}
	
	@Override
	@JsonIgnore
	@XmlTransient
	public List<Link> getLinks() {
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("/series/" + getSeries().getId(), "series", "GET"));
		
		return links;

	}

	@Override
	@JsonIgnore
	@XmlTransient
	public String getHrefResource() {

		return "/series/" + getSeries().getId() + "/seasons/" + getId();
	}

}
