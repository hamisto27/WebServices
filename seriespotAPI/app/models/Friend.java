package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import com.avaje.ebean.annotation.*;

import com.avaje.ebean.*;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.*;

import play.mvc.*;

import util.*;

@Entity
@Table(name = "friend")
@XmlRootElement(name = "friend")
@XmlAccessorType(XmlAccessType.FIELD)
public class Friend extends Model implements HypermediaProvider {

	private static final long serialVersionUID = 1L;

	@EnumMapping(nameValuePairs = "REQUESTED=0, CONFIRMED=1, YOU=2")
	public enum Status {
		REQUESTED(0), CONFIRMED(1), YOU(2);

		private int statusCode;

		private Status(int statusCode) {
			this.statusCode = statusCode;
		}

		public int getStatusCode() {
			return statusCode;
		}
	}

	@EmbeddedId
	@XmlElement(name = "friendship")
	@JsonProperty("friendship")
	public FriendPK friendPK;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "friend_one", insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	@XmlTransient
	public User userFriendOne;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "friend_two", insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	@XmlTransient
	public User userFriendTwo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "CET")
	@Column(nullable = false)
	@JsonProperty("since")
	@XmlElement(name = "since")
	private Date since;

	public Friend(Integer friendOne, Integer friendTwo, Status status) {
		this.friendPK = new FriendPK(friendOne, friendTwo);
		setStatus(status);
		this.since = new Date();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

	// primary key
	public FriendPK getFriendPK() {
		return friendPK;
	}

	public void setStatus(FriendPK friendPK) {
		this.friendPK = friendPK;
	}

	public static Finder<FriendPK, Friend> find = new Finder<FriendPK, Friend>(
			FriendPK.class, Friend.class);

	// add request to your friend.
	public static void create(Friend friend) {
		friend.save();
	}

	// add request to your friend.
	public static void deleteUserFriends(Integer userId) {

		String sql = "find friend where friend_one = :id_one or friend_two = :id_two";

		com.avaje.ebean.Query<Friend> query = Ebean.createQuery(Friend.class,
				sql);
		query.setParameter("id_one", userId);
		query.setParameter("id_two", userId);

		List<Friend> friendList = query.findList();
		for (Friend friend : friendList) {

			deleteFriend(friend.friendPK.getFriendOne(),
					friend.friendPK.getFriendTwo());
		}

	}

	public static List<Friend> getUserFriends(Integer userId) {

		String sql = "find friend where (friend_one = :id_one or friend_two = :id_two) and status = :status";

		com.avaje.ebean.Query<Friend> query = Ebean.createQuery(Friend.class,
				sql);
		query.setParameter("id_one", userId);
		query.setParameter("id_two", userId);
		query.setParameter("status", Status.CONFIRMED);

		return query.findList();
	}

	public static Friend getFriend(Integer friendOne, Integer friendTwo) {

		if (friendOne != friendTwo) {
			String sql = "find friend where (friend_one = :id_one or friend_two = :id_one) and (friend_one = :id_two or friend_two = :id_two) and status = :status limit 1";

			com.avaje.ebean.Query<Friend> query = Ebean.createQuery(
					Friend.class, sql);
			query.setParameter("id_one", friendOne);
			query.setParameter("id_two", friendTwo);
			query.setParameter("status", Status.CONFIRMED);

			return query.findUnique();
		}

		return null;

	}

	public static Friend getOutgoingFriend(Integer friendOne, Integer friendTwo) {

		String sql = "find friend where friend_one = :id_one and friend_two = :id_two and status = :status";

		com.avaje.ebean.Query<Friend> query = Ebean.createQuery(Friend.class,
				sql);
		query.setParameter("id_one", friendOne);
		query.setParameter("id_two", friendTwo);
		query.setParameter("status", Status.REQUESTED);

		return query.findUnique();

	}

	public static List<Friend> getOutgoingRequests(Integer userId) {

		String sql = "find friend where friend_one = :id_one and status = :status";

		com.avaje.ebean.Query<Friend> query = Ebean.createQuery(Friend.class,
				sql);
		query.setParameter("id_one", userId);
		query.setParameter("status", Status.REQUESTED);

		return query.findList();
	}

	public static List<Friend> getIncomingRequests(Integer userId) {

		String sql = "find friend where friend_two = :id_two and status = :status";

		com.avaje.ebean.Query<Friend> query = Ebean.createQuery(Friend.class,
				sql);
		query.setParameter("id_two", userId);
		query.setParameter("status", Status.REQUESTED);

		return query.findList();

	}

	// delete friend from the database.
	public static void deleteFriend(Integer friendOne, Integer friendTwo) {

		FriendPK friendPK = new FriendPK(friendOne, friendTwo);
		if (find.byId(friendPK) != null)
			find.ref(friendPK).delete();
		else
			find.ref(new FriendPK(friendTwo, friendOne)).delete();

	}

	public static Friend getPendingFriend(Integer friendOne, Integer friendTwo) {

		String sql = "find friend where friend_one = :id_one and friend_two = :id_two and status = :status limit 1";

		com.avaje.ebean.Query<Friend> query = Ebean.createQuery(Friend.class,
				sql);
		query.setParameter("id_one", friendOne);
		query.setParameter("id_two", friendTwo);
		query.setParameter("status", Status.REQUESTED);
		return query.findUnique();
	}

	public static void acceptFriend(Friend friend) {

		friend.update();
	}

	public static void deletePendingFriend(Friend friend) {

		friend.delete();
	}

	private User getUser() {
		return (User) Http.Context.current().args.get("user");
	}

	@Override
	public List<Link> getLinks() {

		List<Link> links = new ArrayList<Link>();
		if (getStatus() == Status.REQUESTED) {
			if (getUser().id == this.friendPK.getFriendTwo()) {
				links.add(new Link("/friends/" + this.friendPK.getFriendOne(), "accept"));
			}
		}
		if (getStatus() == Status.CONFIRMED) {
			if (getUser().id == this.friendPK.getFriendOne()) {
				links.add(new Link("/friends/" + this.friendPK.getFriendTwo(),
						"friend"));
				links.add(new Link("/users/" + this.friendPK.getFriendTwo(),
						"profile"));
				links.add(new Link("/friends/" + this.friendPK.getFriendTwo()
						+ "/series", "series"));
			}
			if (getUser().id == this.friendPK.getFriendTwo()) {
				links.add(new Link("/friends/" + this.friendPK.getFriendOne(),
						"friend"));
				links.add(new Link("/users/" + this.friendPK.getFriendOne(),
						"profile"));
				links.add(new Link("/friends/" + this.friendPK.getFriendOne()
						+ "/series", "series"));
			}

		}

		return links.size() != 0 ? links : null;
	}

	@Override
	public String getHrefResource() {

		if (getStatus() == Status.CONFIRMED) {

			if (getUser().id == this.friendPK.getFriendOne()) {
				return "/friends/" + this.friendPK.getFriendTwo();
			}
			if (getUser().id == this.friendPK.getFriendTwo()) {
				return "/friends/" + this.friendPK.getFriendOne();
			}

		}

		return null;
	}

	@Embeddable
	public static class FriendPK {

		@Column
		@Constraints.Required
		@Constraints.MinLength(10)
		@Constraints.MaxLength(10)
		public Integer friendOne;

		@Column
		@Constraints.Required
		@Constraints.MinLength(10)
		@Constraints.MaxLength(10)
		public Integer friendTwo;

		public FriendPK(Integer friendOne, Integer friendTwo) {
			this.friendOne = friendOne;
			this.friendTwo = friendTwo;
		}

		@XmlTransient
		public Integer getFriendOne() {
			return friendOne;
		}

		public void setFriendOne(Integer friendOne) {
			this.friendOne = friendOne;
		}

		@XmlTransient
		public Integer getFriendTwo() {
			return friendTwo;
		}

		public void setFriendTwo(Integer friendTwo) {
			this.friendTwo = friendTwo;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object)
				return true;

			if (object == null || (this.getClass() != object.getClass())) {
				return false;
			}

			FriendPK other = (FriendPK) object;

			return (this.friendOne != null && this.friendOne == other.friendOne)
					&& (this.friendTwo != null && this.friendTwo == other.friendTwo);
		}

		@Override
		public int hashCode() {
			int hash = 0;
			hash = 31 * hash
					+ (this.friendOne != null ? this.friendOne.hashCode() : 0);
			hash = 31 * hash
					+ (this.friendTwo != null ? this.friendTwo.hashCode() : 0);

			return hash;
		}
	}

}
