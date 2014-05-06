package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import play.db.ebean.Model;

import util.HypermediaProvider;
import util.Link;

import com.avaje.ebean.annotation.EnumMapping;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "series")
@XmlRootElement(name = "series")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "name", "overview","genre", "rating", "status", "poster"})

public class Series extends Model implements HypermediaProvider{

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

	@XmlElement(name="id")
    @JsonProperty("id")
	@Id
	@Column(length = 11, nullable = false)
	public String id;

	@XmlElement(name="name", required = true)
    @JsonProperty("name")
	@Column(length = 255)
	private String name;

	@XmlElement(name="overview", required = true)
    @JsonProperty("overview")
	@Column(columnDefinition = "TEXT")
	private String overview;

	@XmlElement(name = "genre")
	@JsonProperty("genre")
	@Column(nullable = true)
	private String genre;
	
	@XmlElement(name = "poster")
	@JsonProperty("poster")
	@Column(length = 150, nullable = true)
	private String poster;
	
	@JsonProperty("status")
	@XmlElement(name = "status")
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private Status status;
	
	@JsonProperty("rating")
	@XmlElement(name = "rating")
	@Column(length = 11, nullable = true)
	private String rating;

	
	public Series(){
		;
	}
    
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

	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getPoster() {

		/*if(this.poster == null && this.getId() != null){
			setPoster("http://www.thetvdb.com/banners/_cache/posters/"+ this.getId() + "-1.jpg");
		}*/
		return poster;
	}
	public void setPoster(String poster) {
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


    public static Finder<String, Series> find = new Finder<String, Series>(String.class, Series.class);
    

    public static List<Series> findAll(){
        return find.all();
    }

    public static Series findById(String id){
        return find.byId(id);
    }

    public static void create(Series series) {
        series.save();
    }

    public static void deleteById(String id){
    	
    	UserSeries.deleteSeries(id);
    	
        find.ref(id).delete();
    }

    public static void update(Series series){
        series.update();
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