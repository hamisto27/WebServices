package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import util.HypermediaProvider;
import util.Link;

@Entity
@Table(name = "rating")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ratings")
@XmlType(propOrder = {"id", "series", "votes"})
@JsonInclude(Include.NON_NULL)
public class Rating extends Model implements HypermediaProvider{


	private static final long serialVersionUID = 1L;

	@Id
    @Column(length = 11)
	@XmlElement
    public Integer id;

	@OneToOne
	@JoinTable(name="series")
	@XmlElement
	@XmlJavaTypeAdapter(SeriesAdapter.class)
	private Series series;
	
	@Column
	@XmlTransient
    private int total;

    @Column
    @XmlElement
    private int votes;
    
    public Rating(){
    	;
    }
    
    
    public Rating(Series series, int total, int votes){
    	
    	this.series = series;
    	this.total = total;
    	this.votes = votes;
    }
    
    
	public Series getSeries() {
		return series;
	}
	public void setSeries(Series series) {
		this.series = series;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
	
    public static Finder<Integer, Rating> find = new Finder<Integer, Rating>(Integer.class, Rating.class);
    
    public static List<Rating> findAll(){
        return find.all();
    }
    
    public static Rating findById(Integer id){
        return find.byId(id);
    }
    
    public static Rating findBySeriesId(String id){
    	
        return find.where().eq("series_id", id).findUnique();
    }

    public static void create(Rating rating) {
        rating.save();
       
    }

    public static void deleteById(Integer id){

        find.ref(id).delete();
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
	
	private static class SeriesAdapter extends XmlAdapter<Series, Series>
	{

		@Override
		public Series marshal(Series series) throws Exception {
			
			Series seriesDb = Series.find.byId(series.getId());
			seriesDb.setStatus(null);
			seriesDb.setOverview(null);
			seriesDb.setGenre(null);
			seriesDb.setPoster(null);
			return seriesDb;
		}

		@Override
		public Series unmarshal(Series series) throws Exception {

			return series;
		}


	}


}
