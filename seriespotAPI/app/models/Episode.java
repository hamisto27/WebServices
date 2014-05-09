package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;
import util.HypermediaProvider;
import util.Link;

@Entity
@Table(name = "episode")
public class Episode extends Model implements HypermediaProvider{

	private static final long serialVersionUID = 1L;
	
	
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
