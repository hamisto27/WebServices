package util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.*;

public interface HypermediaProvider {

	@JsonIgnore
	@XmlTransient
	public List<Link> getLinks();

	@JsonIgnore
	@XmlTransient
	public String getHrefResource();
}
