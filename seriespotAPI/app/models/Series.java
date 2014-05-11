package models;

import java.util.ArrayList;
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
import play.mvc.Http;

import util.HypermediaProvider;
import util.Link;

import com.avaje.ebean.annotation.EnumMapping;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "series")
@XmlRootElement(name = "series")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "name", "overview","genre", "status", "poster", "ratingTvdb", "rating"})
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
	private String id;

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
	
	
	@JsonProperty("ratingTvdb")
	@XmlElement(name = "ratingTvdb")
	@Column(length = 11, nullable = true)
	private String ratingTvdb;
	
	@JsonProperty("rating")
	@XmlElement(name = "rating")
	@Column(length = 11, nullable = true)
	private Float rating;
	

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
	public String getRatingTvdb() {
		return ratingTvdb;
	}
	public void setRatingTvdb(String ratingTvdb) {
		this.ratingTvdb = ratingTvdb;
	}
	
	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

    public static Finder<String, Series> find = new Finder<String, Series>(String.class, Series.class);
    

    public static List<Series> findAll(){
        return find.all();
    }

    public static Series findById(String id){
        return find.byId(id);
    }
    
    public static List<Series> findByName(String name, Integer limit) {
    	
    	List<Series> series = find.where().ieq("name", name).findList();
    	if(limit != 0){
    		return limit > series.size() ? series : series.subList(0, limit);	
    	}
    	
    	return find.where().ieq("name", name).findList();
 	
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
		
		List<Link> links = new ArrayList<Link>();
		
		links.add(new Link("/users/me/series", "add series", "POST"));
		links.add(new Link("/series/" + getId() + "/seasons", "seasons", "GET"));
		
		return links;
	}


	@Override
	@JsonIgnore
	@XmlTransient
	public String getHrefResource() {
		
		if(!Http.Context.current().request().queryString().isEmpty() && Http.Context.current().request().getQueryString("location") != null){
			if(Http.Context.current().request().getQueryString("location").equalsIgnoreCase("TVDB"))
				return "/series/" + this.id + "?location=TVDB";
			
			else if(Http.Context.current().request().getQueryString("location").equalsIgnoreCase("SERIESPOT"))
				return "/series/" + this.id + "?location=SERIESPOT";
			}
		return "/series/" + this.id + "?location=SERIESPOT";
	}
	
	
	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}
	
}