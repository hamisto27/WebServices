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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import util.HypermediaProvider;
import util.Link;

@Entity
@Table(name = "user_series")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "user_series")
@XmlType(propOrder = {"series", "creationDate"})
@JsonInclude(Include.NON_NULL) 
public class UserSeries extends Model implements HypermediaProvider{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@JsonIgnore
	@EmbeddedId
	public UserSeriesPK userSeriesPK;
	
	@XmlTransient
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
	private User user;
	
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "series_id", insertable = false, updatable = false, nullable = false)
	@XmlElement
	@XmlJavaTypeAdapter(SeriesAdapter.class)
	private Series series;

	@XmlElement(name="created")
    @JsonProperty("created")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssXXX", timezone="CET")
    private Date creationDate;
	
	
	public UserSeries(){
		;
	}
	public UserSeries(Integer userId, String seriesId) {
		this.setUserSeriesPK(new UserSeriesPK(userId, seriesId));
		this.setSeries(Series.findById(seriesId));
		this.creationDate = new Date();
	}
	
	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Series getSeries() {
		return series;
	}


	public void setSeries(Series series) {
		this.series = series;
	}

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
	
	
	public static Finder<UserSeriesPK, UserSeries> find = new Finder<UserSeriesPK, UserSeries>(
			UserSeriesPK.class, UserSeries.class);

	public static void create(UserSeries usersSeries) {
		
		usersSeries.save();
	}
	
	public static List<UserSeries> getUserSeries(Integer userId) {

		String sql = "find user_series where user_id = :id";

		com.avaje.ebean.Query<UserSeries> query = Ebean.createQuery(UserSeries.class, sql);
		query.setParameter("id", userId);
		return query.findList();
	}
	public static void deleteUser(Integer userId){
		
		String sql = "find user_series where user_id = :id";

		com.avaje.ebean.Query<UserSeries> query = Ebean.createQuery(UserSeries.class, sql);
		query.setParameter("id", userId);
		for(UserSeries userSeries : query.findList()){
			deleteUserSeries(userSeries.getUserSeriesPK().getUserId(), userSeries.getUserSeriesPK().getSeriesId());
		}
	}
	
	public static void deleteSeries(String seriesId){
		
		String sql = "find user_series where series_id = :id";

		com.avaje.ebean.Query<UserSeries> query = Ebean.createQuery(UserSeries.class, sql);
		query.setParameter("id", seriesId);
		for(UserSeries userSeries : query.findList()){
			deleteUserSeries(userSeries.getUserSeriesPK().getUserId(), userSeries.getUserSeriesPK().getSeriesId());
		}
	}
	
	public static void deleteUserSeries(Integer userId, String seriesId) {

		UserSeriesPK userSeriesPK = new UserSeriesPK(userId, seriesId);
		find.ref(userSeriesPK).delete();

	}
	
	public static UserSeries findById(Integer userId, String seriesId){
		
		return find.byId(new UserSeriesPK(userId, seriesId));
    }
	
	
	public static class SeriesAdapter extends XmlAdapter<Series, Series>
	{

		@Override
		public Series marshal(Series series) throws Exception {
			
			return Series.find.byId(series.id);
		}

		@Override
		public Series unmarshal(Series series) throws Exception {

			return series;
		}


	}
	
	@Embeddable
	public static class UserSeriesPK {
		
		@XmlTransient
		@Constraints.Required
		@Column(length = 11)
		private Integer userId;
		
		@XmlTransient
		@Constraints.Required
		@Column(length = 11)
		private String seriesId;

		public UserSeriesPK(Integer userId, String seriesId) {
			this.userId = userId;
			this.seriesId = seriesId;
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public String getSeriesId() {
			return seriesId;
		}

		public void setSeriesId(String seriesId) {
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
					&& (this.seriesId != null && this.seriesId.equals(other.seriesId));
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
