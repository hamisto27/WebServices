package models;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import util.HypermediaProvider;
import util.Link;

public class Update implements HypermediaProvider{

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
