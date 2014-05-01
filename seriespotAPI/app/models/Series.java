package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import util.HypermediaProvider;

import com.avaje.ebean.annotation.EnumMapping;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "series")
@XmlRootElement(name = "series")
@XmlAccessorType(XmlAccessType.FIELD)
public class Series extends BaseSeries implements HypermediaProvider {

	private static final long serialVersionUID = 1L;
	
	
	@EnumMapping(nameValuePairs = "ENDED=0, CONTINUING=1")
	public enum Status {
		ENDED(0), CONTINUING(1);

		private int statusCode;

		private Status(int statusCode) {
			this.statusCode = statusCode;
		}

		public int getStatusCode() {
			return statusCode;
		}
	}
	
	@JsonProperty("genre")
	@XmlElement(name = "genre")
	@Column(nullable = true)
	private String genre;
	
	@JsonProperty("poster")
	@XmlElement(name = "poster")
	@Column(nullable = true)
	private String poster;
	
	@JsonProperty("status")
	@XmlElement(name = "status")
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private Status status;
	
	@JsonProperty("rating")
	@XmlElement(name = "rating")
	@Column(nullable = true)
	private String rating;

	
	public Series(){
		;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getPoster() {

		if(this.poster == null && this.getId() != null){
			setPoster("http://www.thetvdb.com/banners/_cache/posters/"+ this.getId() + "-1.jpg");
		}
		return poster;
	}
	private void setPoster(String poster) {
		this.poster = poster;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}


}