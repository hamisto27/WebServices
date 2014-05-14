package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
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

import play.db.ebean.Model;
import play.mvc.Http;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import util.HypermediaProvider;
import util.Link;

@Entity
@Table(name = "comment")
@XmlRootElement(name = "comment")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "message", "series", "user" })
@JsonInclude(Include.NON_NULL)
public class Comment extends Model implements HypermediaProvider {

	private static final long serialVersionUID = 1L;

	@XmlElement
	@Id
	private Integer id;

	@XmlElement
	@Column(columnDefinition = "TEXT")
	private String message;

	@XmlElement
	@XmlJavaTypeAdapter(CommentSeriesAdapter.class)
	@JsonSerialize(using=JsonCommentSeriesAdapter.class)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "series")
	private Series series;

	@XmlElement
	@XmlJavaTypeAdapter(CommentUserAdapter.class)
	@JsonSerialize(using=JsonCommentUserAdapter.class)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "users")
	private User user;

	public Comment() {
		;
	}

	public Comment(String message, Series series, User user) {

		setMessage(message);
		setSeries(series);
		setUser(user);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static Finder<Integer, Comment> find = new Finder<Integer, Comment>(
			Integer.class, Comment.class);

	
	public static Comment findById(Integer id) {
		return find.byId(id);
	}

	
	public static List<Comment> findBySeries(String id) {
		
		 return find.where().eq("series_id", id).findList();
	}

	public static List<Comment> findByUser(Integer id) {
		
		 return find.where().eq("user_id", id).findList();
	}


	public static void create(Comment comment) {
		
		comment.save();
	}

	public static void deleteById(Integer id) {

		find.ref(id).delete();
	}
	
	public static void deleteBySeries(String id) {

		String sql = "find comment where series_id = :id";

		com.avaje.ebean.Query<Comment> query = Ebean.createQuery(Comment.class, sql);
		query.setParameter("id", id);
		for(Comment comment : query.findList()){
			deleteById(comment.getId());
		}
	}
	
	public static void deleteByUser(Integer id) {

		String sql = "find comment where user_id = :id";

		com.avaje.ebean.Query<Comment> query = Ebean.createQuery(Comment.class, sql);
		query.setParameter("id", id);
		for(Comment comment : query.findList()){
			deleteById(comment.getId());
		}
	}
	
	public static void update(Comment comment) {
		
		comment.update();
	}
	
	@Override
	@JsonIgnore
	@XmlTransient
	public List<Link> getLinks() {
		
		List<Link> links = new ArrayList<Link>();
		
		links.add(new Link("users/me/series/" + this.getSeries().getId() + "/comments", "add comment", "POST"));
		links.add(new Link("/series/" + this.getSeries().getId(), "series info", "GET"));
		links.add(new Link("/users/" + this.getUser().id, "user info", "GET"));
		
		return links;

	}

	@Override
	@JsonIgnore
	@XmlTransient
	public String getHrefResource() {

		return "/series/" + this.getSeries().getId() + "/comments/" + this.getId();
	}
	

	
	private static class CommentSeriesAdapter extends XmlAdapter<Series, Series>
	{
		
		@Override
		public Series marshal(Series series) throws Exception {
			
			Series seriesDb = Series.find.byId(series.getId());
			seriesDb.setStatus(null);
			seriesDb.setOverview(null);
			seriesDb.setGenre(null);
			seriesDb.setPoster(null);
			seriesDb.setRatingTvdb(null);
			seriesDb.setRating(null);
			return seriesDb;
		}

		@Override
		public Series unmarshal(Series series) throws Exception {

			return series;
		}


	}
	
	private static class JsonCommentSeriesAdapter extends JsonSerializer<Series> {
		
		@Override
		public void serialize(Series series, JsonGenerator jgen, SerializerProvider provider) throws JsonProcessingException, IOException {

			jgen.writeStartObject();
			jgen.writeStringField("id", series.getId());
			jgen.writeStringField("name", series.getName());
			jgen.writeEndObject();
		   
		}

	}
	
	private static class CommentUserAdapter extends XmlAdapter<User, User>
	{
		
		@Override
		public User marshal(User user) throws Exception {
			
			User userDb = User.find.byId(user.id);
			userDb.setEmailAddress(null);
			userDb.setFriendCount(null);
			userDb.setCreationDate(null);
			return  userDb;
		}

		@Override
		public User unmarshal(User user) throws Exception {

			return user;
		}


	}
	
	private static class JsonCommentUserAdapter extends JsonSerializer<User> {
		
		@Override
		public void serialize(User user, JsonGenerator jgen, SerializerProvider provider) throws JsonProcessingException, IOException {

			jgen.writeStartObject();
			jgen.writeNumberField("id", user.id);
			jgen.writeStringField("name", user.getFullName());
			jgen.writeEndObject();
		   
		}

	}

}
