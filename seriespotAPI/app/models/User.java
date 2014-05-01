package models;

import models.Friend;

import com.fasterxml.jackson.annotation.*;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.db.ebean.Model;
import play.mvc.Http;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import util.*;

@Entity
@Table(name = "user")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "fullName", "emailAddress", "password", "friendCount", "creationDate"})
@JsonInclude(Include.NON_NULL) 
public class User extends Model implements HypermediaProvider {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(length = 11)
    public Integer id;

    @XmlElement(name="name", required = true)
    @JsonProperty("name")
    @Constraints.Required
    @Constraints.MaxLength(255)
    @Column(length = 255, nullable = false)
    private String fullName;

    @XmlElement(name="email", required = true)
    @JsonProperty("email")
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    @Column(length = 255, unique = true, nullable = false)
    private String emailAddress;

    @XmlTransient
    @JsonIgnore
    @Column(length = 64, nullable = false)
    private byte[] shaPassword;

    @Transient
    @XmlElement(name="password", required = true, nillable = false)
    @JsonProperty("password")
    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(255)
    private String password;

    @XmlElement(name="friend_count")
    @Column(length = 11, nullable = false)
    private int friendCount;

    @XmlElement(name="created")
    @JsonProperty("created")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssXXX", timezone="CET")
    @Column(nullable = false)
    private Date creationDate;

    @XmlTransient
    @Column(length = 64)
    private byte[] shaAuthToken;

    @Transient
    @XmlTransient
    private String authToken;

    public User() {

        setFriendCount(0);
        this.creationDate = new Date();

    }

    public User(String emailAddress, String password, String fullName) {

        setFullName(fullName);
        setEmailAddress(emailAddress);
        setPassword(password);
        setFriendCount(0);
        this.creationDate = new Date();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
    	if(emailAddress != null)
    		this.emailAddress = emailAddress.toLowerCase();
    	else
    		this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.shaPassword= getSha512(password);
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public String createToken() {
        authToken = UUID.randomUUID().toString();
        shaAuthToken = getSha512(authToken);
        save();
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        shaAuthToken = null;
        save();
    }

    @Override
    public List<Link> getLinks(){

        List<Link> links = new ArrayList<Link>();
        if(getUser() != null){
	        if(getUser().id == this.id){
	        	
	        	links.add(new Link("/friends","friends"));
	            links.add(new Link("/series","series"));
	        }
	        if (getUser().id != this.id && Friend.getFriend(getUser().id, this.id) != null){
	        	
	        	 links.add(new Link("/users/" + this.id + "/friends","friends"));
	             links.add(new Link("/users/" + this.id + "/series","series"));
	        }
        	
        }

        return links.size() != 0 ? links : null;
    }

    @Override
    public String getHrefResource(){
    	
    	
    	if(getUser() != null && getUser().id == this.id){
    		return "/";
    	}
        return "/users/" + this.id;
    }

    public static byte[] getSha512(String value) {

        System.out.println(value);
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // Creates a finder for entity of type T with ID of type I.
    public static Finder<Integer, User> find = new Finder<Integer, User>(Integer.class, User.class);
    
    // find all user.
    public static List<User> findAll(){
        return find.all();
    }
    // find by ID.
    public static User findById(Integer id){
        return find.byId(id);
    }

    // add user to the database.
    public static void create(User user) {
        user.save();
        //save user in the friend list
        Friend.create(new Friend(user.id, user.id, Friend.Status.YOU));
    }

    // delete user from the database.
    public static void deleteById(Integer id){

        // delete all friend of the user 
        Friend.deleteUserFriends(id);

        find.ref(id).delete();
    }

    // update user from the database.
    public static void update(User user){
        user.update();
    }

    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        try  {
            return find.where().eq("shaAuthToken", getSha512(authToken)).findUnique();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static User findByEmailAddressAndPassword(String emailAddress, String password) {
        // todo: verify this query is correct.  Does it need an "and" statement?
        return find.where().eq("emailAddress", emailAddress.toLowerCase()).eq("shaPassword", getSha512(password)).findUnique();
    }


    public static User findByEmailAddress(String emailAddress){
        return find.where().eq("emailAddress", emailAddress.toLowerCase()).findUnique();
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (User.findByEmailAddress(emailAddress) != null) {
            errors.add(new ValidationError("email", "This e-mail is already registered"));
        }
        return errors.isEmpty() ? null : errors;
    }
    
    
	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}

}