package controllers;

import play.mvc.*;
import models.Friend;
import models.User;
import play.data.Form;

import java.util.*;

import javax.xml.bind.JAXBException;

import util.*;
import annotations.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import play.data.validation.ValidationError;

public class UserController extends BaseController {

	@With(SecurityController.class)
	public static Result getProfile() throws JsonProcessingException,
			JAXBException {
		if (request().queryString().isEmpty()) {

			return ok(ObjectResponseFormatter.objectResponse(User
					.findById(getUser().id)));
		}
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());

	}

	@With(SecurityController.class)
	public static Result getUser(Integer id) throws JAXBException,
			JsonProcessingException {
		if (request().queryString().isEmpty()) {
			User user = User.findById(id);
			if (user == null) {
				ErrorMessage error = new ErrorMessage("Not Found", 409,
						"No user found with ID equal to:'" + id + "'");
				return Results.notFound(error.marshalError());
			}
			if (Friend.getFriend(getUser().id, id) == null
					&& getUser().id != id) {
				user.setEmailAddress(null); // only friend sees email
			}

			return ok(ObjectResponseFormatter.objectResponse(user));
		}
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());
	}

	@With(SecurityController.class)
	public static Result getAllUsers() throws JAXBException,
			JsonProcessingException {
		if (request().queryString().isEmpty()) {
			List<User> users = User.findAll();
			for (User user : users) {
				if (Friend.getFriend(getUser().id, user.id) == null
						&& getUser().id != user.id) {
					user.setEmailAddress(null);
				}
			}
			return ok(ObjectResponseFormatter.objectListResponse(users,
					User.class, "/users"));
		}
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());
	}

	@FromXmlTo(User.class)
	@FromJsonTo(User.class)
	public static Result createUser() throws JAXBException,
			JsonProcessingException {

		User user = bodyRequest(User.class);
		Map<String, String> userData = new HashMap<String, String>();

		/*
		 * if(request().cookies() != null){ return redirect("/"); }
		 */
		if (user.getEmailAddress() != null)
			userData.put("emailAddress", user.getEmailAddress());
		if (user.getFullName() != null)
			userData.put("fullName", user.getFullName());
		if (user.getPassword() != null)
			userData.put("password", user.getPassword());

		Form<models.User> registerForm = Form.form(models.User.class).bind(
				userData);

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
		// create user
		User userDB = new User(user.getEmailAddress(), user.getPassword(),
				user.getFullName());
		User.create(userDB);
		response().setHeader(LOCATION, "users/" + User.findById(userDB.id).id);
		return created(ObjectResponseFormatter.objectResponse(User
				.findById(userDB.id)));

	}

	@FromXmlTo(User.class)
	@FromJsonTo(User.class)
	@With(SecurityController.class)
	public static Result updateUser() throws JAXBException,
			JsonProcessingException {

		User userToUpdate = bodyRequest(User.class);
		User user = User.findById(getUser().id);

		if (userToUpdate.getEmailAddress() != null)
			user.setEmailAddress(userToUpdate.getEmailAddress());
		if (userToUpdate.getFullName() != null)
			user.setFullName(userToUpdate.getFullName());
		if (userToUpdate.getPassword() != null)
			user.setPassword(userToUpdate.getPassword());

		User.update(user);

		return ok(ObjectResponseFormatter.objectResponse(user));

	}

	@With(SecurityController.class)
	public static Result deleteUser() throws JAXBException,
			JsonProcessingException {

		User.deleteById(getUser().id);
		return noContent();
	}

	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}
}
