package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;

import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import util.HypermediaProvider;
import util.Link;

public class Rating extends Model implements HypermediaProvider{


	private static final long serialVersionUID = 1L;

	@Id
    @Column(length = 11)
    public Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "series_id", insertable = false, updatable = false, nullable = false)
	private Series series;
	
	@Column
    private int total;

    @Column
    private int votes;
	
    @Column(columnDefinition = "TEXT", nullable = true)
    private List<String> comments;
    
    
    public Rating(){
    	;
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
