package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import models.Series;
import models.User;
import models.Comment;

import play.data.Form;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;
import util.ErrorMessage;
import util.ObjectResponseFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;

import annotations.BaseController;
import annotations.FromJsonTo;
import annotations.FromXmlTo;

public class CommentController extends BaseController{

		@FromXmlTo(CreateComment.class)
		@FromJsonTo(CreateComment.class)
		@With({SecurityController.class, HttpsAction.class})
		public static Result addComment(String id) throws JAXBException, JsonProcessingException {
			
			
			if(request().queryString().isEmpty()){
				
				CreateComment createComment = bodyRequest(CreateComment.class);
				
				Map<String, String> commentData = new HashMap<String, String>();

				commentData.put("message", createComment.message);
				Form<CreateComment> commentForm = Form.form(CreateComment.class).bind(
						commentData);

				if (commentForm.hasErrors()) {
					String errorString = "The following errors has been detected: ";
					int i = 0;
					java.util.Map<java.lang.String, java.util.List<ValidationError>> map = commentForm
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
				
				Series series = Series.findById(id);

				if (series == null) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No series found with ID equal to:'" + id + "'");
					return Results.notFound(error.marshalError());
				}
				
				Comment comment = new Comment(createComment.message, series, getUser());
				
				comment.save();
				
				response().setHeader(LOCATION,
						"/series/" + comment.getSeries().getId()  + "comments/" + comment.getId());
				return created(ObjectResponseFormatter.objectResponse(Comment.findById(comment.getId()))); 
				
			}
			
			ErrorMessage error = new ErrorMessage(
					"Internal server error", 500, "Invalid URL format.");
			return internalServerError(error.marshalError());
			
		}
		
		public static Result getAllComments(String id) throws JAXBException, JsonProcessingException {
			
			if(request().queryString().isEmpty()){
				
				Series series = Series.findById(id);
				
				if (series == null) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No series found with ID equal to:'" + id + "'");
					return Results.notFound(error.marshalError());
				}
				
				return ok(ObjectResponseFormatter.objectListResponse(Comment.findBySeries(id), Comment.class, "/series/" + id + "/comments"));
			}
			
			ErrorMessage error = new ErrorMessage(
					"Internal server error", 500, "Invalid URL format.");
			return internalServerError(error.marshalError());
	
		}
		
		
		public static Result getComment(String idSeries, Integer idComment) throws JAXBException, JsonProcessingException {
			
			if(request().queryString().isEmpty()){
				
				Series series = Series.findById(idSeries);
				
				if (series == null) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No series found with ID equal to:'" + idSeries + "'");
					return Results.notFound(error.marshalError());
				}
				
				Comment comment = Comment.findById(idComment);
				
				if (comment == null) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No comment found with ID equal to:'" + idComment + "'");
					return Results.notFound(error.marshalError());
				}
				
				return ok(ObjectResponseFormatter.objectResponse(Comment.findById(idComment)));

			}
			
			ErrorMessage error = new ErrorMessage(
					"Internal server error", 500, "Invalid URL format.");
			return internalServerError(error.marshalError());
	
		}
		
		@With({SecurityController.class, HttpsAction.class})
		public static Result deleteComment(String idSeries, Integer idComment) throws JAXBException, JsonProcessingException {
			
			if(request().queryString().isEmpty()){
				
				if (Series.findById(idSeries) == null) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No series found with ID equal to:'" + idSeries + "'");
					return Results.notFound(error.marshalError());
				}
				
				Comment comment = Comment.findById(idComment);
				if (comment == null || comment.getUser().id != getUser().id) {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"No comment found with ID equal to:'" + idComment + "'");
					return Results.notFound(error.marshalError());
				}

				Comment.deleteById(idComment);
				
				return noContent();
				
			}
			
			ErrorMessage error = new ErrorMessage(
					"Internal server error", 500, "Invalid URL format.");
			return internalServerError(error.marshalError());
	
		}
		
		public static User getUser() {
			return (User) Http.Context.current().args.get("user");
		}
		
		@XmlRootElement(name = "comment")
		@XmlAccessorType(XmlAccessType.FIELD)
		public static class CreateComment {

			@Constraints.Required
			@XmlElement(name = "message")
			public String message;

		}
		
}
