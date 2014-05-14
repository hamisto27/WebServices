package models;

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


@XmlRootElement(name = "update")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "series", "episodes"})
@JsonInclude(Include.NON_NULL)
public class Update implements HypermediaProvider{

	
	@XmlElement
	private Series series;
	

	@XmlElement(name = "episodes")
	@XmlElementWrapper(name = "episode")
	private List<Episode> episodes;
	
	@XmlTransient
	private List<String> time;
	
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
	
	public List<String> getTime() {
		return time;
	}

	public void setTime(List<String> time) {
		this.time = time;
	}
	
	public void addTime(String time){
		this.time.add(time);
	}
	public Update() {
		;
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public List<Link> getLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public String getHrefResource() {
		// TODO Auto-generated method stub
		return null;
	}

}
