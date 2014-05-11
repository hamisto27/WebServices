package controllers;

import java.util.List;

import javax.xml.bind.JAXBException;

import models.Season;
import models.User;

import play.mvc.Http;
import play.mvc.Result;
import util.ErrorMessage;
import util.ObjectResponseFormatter;
import util.SeriesUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

import annotations.BaseController;

public class SeasonController extends BaseController{
	
	public static Result getAllSeasons(String id) throws JAXBException, JsonProcessingException {
		
		if (request().queryString().isEmpty()) {
			List<Season> seasons = SeriesUtil.createSeasonList(id);
			if(seasons == null){
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No seasons found with series ID equal to:'" + id + "'");
				return notFound(error.marshalError());
			}
			return ok(ObjectResponseFormatter.objectListResponse(seasons, Season.class, "/series/" + id + "/seasons"));
		}
	
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());
	}
	
	public static Result getSeason(String idSeries, String idSeason) throws JAXBException, JsonProcessingException {
		
		if (request().queryString().isEmpty()) {
			Season season = SeriesUtil.createSeasonDetail(idSeries, idSeason);
			if(season == null){
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No season found with  seried ID equal to:'" + idSeries + "' and season ID equal to: '" + idSeason + "'");
				return notFound(error.marshalError());
			}
			
			return ok(ObjectResponseFormatter.objectResponse(season));
		}
		
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());
	}
	
	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}
	
}
