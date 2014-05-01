package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import util.HypermediaProvider;
import util.Link;

@Entity
@Table(name = "updates")
@XmlRootElement(name = "series")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSeries extends Model implements HypermediaProvider{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@XmlTransient
	public UserSeriesPK userSeriesPK;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
	public User user;

	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "series_id", insertable = false, updatable = false, nullable = false)
	public Series series;

	
	@XmlElement(name="created")
    @JsonProperty("created")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssXXX", timezone="CET")
    @Column(nullable = false)
    private Date creationDate;
	
	
	public UserSeriesPK getUserSeriesPK() {
		return userSeriesPK;
	}

	public void setUserSeriesPK(UserSeriesPK userSeriesPK) {
		this.userSeriesPK = userSeriesPK;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	

	@Embeddable
	public static class UserSeriesPK {

		@Column
		@Constraints.Required
		public Integer userId;

		@Column
		@Constraints.Required
		public Integer seriesId;

		public UserSeriesPK(Integer userId, Integer seriesId) {
			this.userId = userId;
			this.seriesId = seriesId;
		}

		@XmlTransient
		public Integer getUserId() {
			return userId;
		}

		@XmlTransient
		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		@XmlTransient
		public Integer getSeriesId() {
			return seriesId;
		}

		@XmlTransient
		public void setSeriesId(Integer seriesId) {
			this.seriesId = seriesId;
		}


		@Override
		public boolean equals(Object object) {
			if (this == object)
				return true;

			if (object == null || (this.getClass() != object.getClass())) {
				return false;
			}

			UserSeriesPK other = (UserSeriesPK) object;

			return (this.userId != null && this.userId == other.userId)
					&& (this.seriesId != null && this.seriesId == other.seriesId);
		}

		@Override
		public int hashCode() {
			int hash = 0;
			hash = 31 * hash
					+ (this.userId != null ? this.userId.hashCode() : 0);
			hash = 31 * hash
					+ (this.seriesId != null ? this.seriesId.hashCode() : 0);

			return hash;
		}
	}

}
