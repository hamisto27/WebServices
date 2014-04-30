package controllers;

import play.mvc.*;
import models.Friend;
import play.data.Form;
import play.data.validation.Constraints;
import java.util.*;

import util.*;
import annotations.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.lang3.StringUtils;
import play.data.validation.ValidationError;
import models.*;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

import java.util.Date;

public class FriendController extends BaseController {

	@FromXmlTo(CreateFriend.class)
	@FromJsonTo(CreateFriend.class)
	@With(SecurityController.class)
	public static Result addFriend() throws JAXBException,
			JsonProcessingException {

		CreateFriend friend = bodyRequest(CreateFriend.class);
		Map<String, String> friendData = new HashMap<String, String>();

		friendData.put("friendId", friend.friendId);
		Form<CreateFriend> registerForm = Form.form(CreateFriend.class).bind(
				friendData);

		if (registerForm.hasErrors()) {
			String errorString = "The following errors has been detected: ";
			int i = 0;
			java.util.Map<java.lang.String, java.util.List<ValidationError>> map = registerForm
					.errors();
			for (Map.Entry<String, java.util.List<ValidationError>> entry : map
					.entrySet()) {
				for (ValidationError error : entry.getValue()) {

					errorString = errorString + ++i + ") " + error.toString()
							+ ". ";
				}
			}
			ErrorMessage errorMessage = new ErrorMessage("Bad Request", 400,
					errorString);
			return badRequest(errorMessage.marshalError());
		}
		if (User.findById(Integer.parseInt(friend.friendId)) == null) {

			ErrorMessage error = new ErrorMessage("Not Found", 404,
					"No user found with ID equal to:'" + friend.friendId + "'");
			return Results.notFound(error.marshalError());
		}
		// create user
		Friend friendDB = new Friend(getUser().id,
				Integer.parseInt(friend.friendId), Friend.Status.REQUESTED);
		Friend.create(friendDB);
		response().setHeader(LOCATION, friendDB.getHrefResource());
		return created(ObjectResponseFormatter.objectResponse(models.Friend
				.getPendingFriend(getUser().id,
						Integer.parseInt(friend.friendId))));

	}

	@With(SecurityController.class)
	public static Result getUserFriends(String status, String dir, String name)
			throws JAXBException, JsonProcessingException {

		if (status == null && dir == null && name == null) {
			return ok(ObjectResponseFormatter.objectListResponse(
					models.Friend.getUserFriends(getUser().id),
					models.Friend.class, "/friends"));
		} else {
			if (status != null && dir != null && name == null) {
				if (status.equalsIgnoreCase("pending")
						&& dir.equalsIgnoreCase("out")) {

					return ok(ObjectResponseFormatter.objectListResponse(
							models.Friend.getOutgoingRequests(getUser().id),
							models.Friend.class,
							"/friends?status=pending&dir=out"));
				} else if (status.equalsIgnoreCase("pending")
						&& dir.equalsIgnoreCase("in")) {

					return ok(ObjectResponseFormatter.objectListResponse(
							models.Friend.getIncomingRequests(getUser().id),
							models.Friend.class,
							"/friends?status=pending&dir=in"));
				}
			} else {

				name = name.replace('+', ' ');
				List<models.Friend> friends = models.Friend
						.getUserFriends(getUser().id);
				for (models.Friend friend : friends) {
					if (!StringUtils.containsIgnoreCase(
							friend.userFriendOne.getFullName(), name)
							&& friend.userFriendOne.id != getUser().id) {
						friends.remove(friend);
					}
					if (!StringUtils.containsIgnoreCase(
							friend.userFriendTwo.getFullName(), name)
							&& friend.userFriendTwo.id != getUser().id) {
						friends.remove(friend);
					}
				}

				return ok(ObjectResponseFormatter.objectListResponse(friends,
						models.Friend.class, "/friends?name=" + name));
			}
		}

		return internalServerError();

	}

	@With(SecurityController.class)
	public static Result getFriend(Integer idFriend) throws JAXBException,
			JsonProcessingException {

		models.Friend friend = models.Friend.getFriend(getUser().id, idFriend);

		if (friend == null) {
			ErrorMessage error = new ErrorMessage("Not Found", 404,
					"No friend found with ID equal to:'" + idFriend + "'");
			return Results.notFound(error.marshalError());
		}

		return ok(ObjectResponseFormatter.objectResponse(friend));

	}

	@FromXmlTo(AcceptFriend.class)
	@FromJsonTo(AcceptFriend.class)
	@With(SecurityController.class)
	public static Result confirmPendingFriend(Integer idFriend)
			throws JAXBException, JsonProcessingException {

		AcceptFriend acceptFriend = bodyRequest(AcceptFriend.class);
		Map<String, String> friendData = new HashMap<String, String>();
		friendData.put("status", acceptFriend.status);
		Form<AcceptFriend> registerForm = Form.form(AcceptFriend.class).bind(
				friendData);

		if (registerForm.hasErrors()) {
			String errorString = "The following errors has been detected: ";
			int i = 0;
			java.util.Map<java.lang.String, java.util.List<ValidationError>> map = registerForm
					.errors();
			for (Map.Entry<String, java.util.List<ValidationError>> entry : map
					.entrySet()) {
				for (ValidationError error : entry.getValue()) {

					errorString = errorString + ++i + ") " + error.toString()
							+ ". ";
				}
			}
			ErrorMessage errorMessage = new ErrorMessage("Bad Request", 400,
					errorString);
			return badRequest(errorMessage.marshalError());
		}

		Friend friend = models.Friend.getPendingFriend(idFriend, getUser().id);
		if (friend == null) {
			ErrorMessage error = new ErrorMessage("Not Found", 404,
					"No incoming pending request found with ID equal to:'"
							+ idFriend + "'");
			return notFound(error.marshalError());
		}

		friend.setStatus(models.Friend.Status.CONFIRMED);
		friend.setSince(new Date());
		models.Friend.acceptFriend(friend);

		return ok(ObjectResponseFormatter.objectResponse(friend));

	}

	@With(SecurityController.class)
	public static Result deleteFriend(Integer idFriend, String status,
			String dir) throws JAXBException, JsonProcessingException {

		if (status == null && dir == null) {

			if (models.Friend.getFriend(getUser().id, idFriend) == null) {
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No friend found with ID equal to:'" + idFriend + "'");
				return notFound(error.marshalError());
			}

			models.Friend.deleteFriend(getUser().id, idFriend);
			return noContent();
		}

		else {
			if (status.equalsIgnoreCase("pending")
					&& dir.equalsIgnoreCase("out")) {

				Friend friend = models.Friend.getPendingFriend(getUser().id,
						idFriend);
				if (friend == null) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No outgoing pending request found with ID equal to:'"
									+ idFriend + "'");
					return notFound(error.marshalError());
				}

				models.Friend.deletePendingFriend(friend);
				return noContent();

			} else if (status.equalsIgnoreCase("pending")
					&& dir.equalsIgnoreCase("in")) {

				Friend friend = models.Friend.getPendingFriend(idFriend,
						getUser().id);
				if (friend == null) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No incoming pending request found with ID equal to:'"
									+ idFriend + "'");
					return notFound(error.marshalError());
				}
				models.Friend.deletePendingFriend(friend);
				return noContent();

			}
		}

		return internalServerError();
	}

	@With(SecurityController.class)
	public static Result getFriendsOfFriend(Integer idUser)
			throws JAXBException, JsonProcessingException {

		if (models.Friend.getFriend(getUser().id, idUser) == null) {

			ErrorMessage errorMessage = new ErrorMessage("Unauthorized", 401,
					"The friend ID used does not match one of your friends.");
			return unauthorized(errorMessage.marshalError());
		}

		return ok(ObjectResponseFormatter.objectListResponse(
				models.Friend.getUserFriends(idUser), models.Friend.class,
				"users/" + idUser + "/friends"));
	}

	@With(SecurityController.class)
	public static Result getFriendOfFriend(Integer idUser, Integer idFriend)
			throws JAXBException, JsonProcessingException {

		if (models.Friend.getFriend(getUser().id, idUser) == null) {

			ErrorMessage errorMessage = new ErrorMessage("Unauthorized", 401,
					"The friend ID used does not match one of your friends.");
			return unauthorized(errorMessage.marshalError());
		}

		return ok(ObjectResponseFormatter.objectResponse(models.Friend
				.getFriend(idUser, idFriend)));

	}

	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}

	@XmlRootElement(name = "friendship")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CreateFriend {

		@Constraints.Required
		@XmlElement(name = "friend")
		public String friendId;

	}

	@XmlRootElement(name = "friendship")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class AcceptFriend {

		@Constraints.Required
		@XmlElement(name = "status")
		public String status;

		public List<ValidationError> validate() {

			List<ValidationError> errors = new ArrayList<ValidationError>();
			if (!status.equalsIgnoreCase("CONFIRMED")) {
				errors.add(new ValidationError(
						"status",
						"wrong status value",
						Arrays.asList(new Object[] { "CONFIRMED", "confirmed" })));
			}
			return errors.isEmpty() ? null : errors;
		}

	}

}