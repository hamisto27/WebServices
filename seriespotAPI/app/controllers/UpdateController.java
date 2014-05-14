package controllers;

import java.text.ParseException;

import javax.xml.bind.JAXBException;

import org.w3c.dom.DOMException;

import com.fasterxml.jackson.core.JsonProcessingException;

import models.Episode;
import models.User;
import models.UserSeries;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;
import util.ErrorMessage;
import util.ObjectResponseFormatter;
import util.SeriesUtil;
import annotations.BaseController;

@With(HttpsAction.class)
public class UpdateController extends BaseController{

	
	@With({SecurityController.class, HttpsAction.class})
	public static Result getUpdateForSeries(String id) throws JAXBException,
	JsonProcessingException, NumberFormatException, DOMException, ParseException{
		
		if(request().queryString().isEmpty()){
			
			UserSeries series = UserSeries.findById(getUser().id, id);
			
			if (series == null) {
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No series found with ID equal to:'" + id + "'");
				return Results.notFound(error.marshalError());
			}
			
			return ok(ObjectResponseFormatter.objectListResponse(SeriesUtil.createUpdateEpisodes(id), Episode.class, "/users/me/series/" + id + "/news"));
			
		}
		
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		
		return internalServerError(error.marshalError());
		
	}
	
	
	
	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}
}
