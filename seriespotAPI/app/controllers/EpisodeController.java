package controllers;

import java.util.List;

import javax.xml.bind.JAXBException;

import models.Episode;

import play.mvc.Result;
import util.ErrorMessage;
import util.ObjectResponseFormatter;
import util.SeriesUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

import annotations.BaseController;

public class EpisodeController extends BaseController{

	public static Result getAllEpisodeSeason(String idSeries, String idSeason) throws JAXBException, JsonProcessingException {
		
		if (request().queryString().isEmpty()) {
			List<Episode> episodes = SeriesUtil.createEpisodesList(idSeries, idSeason);
			if(episodes == null){
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No episodes found with  seried ID equal to:'" + idSeries + "' and season ID equal to: '" + idSeason + "'");
				return notFound(error.marshalError());
			}
			return ok(ObjectResponseFormatter.objectListResponse(episodes, Episode.class, "/series/" + idSeries + "/seasons/" + idSeason + "/episodes"));
		}
	
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());
	}
	
	public static Result getEpisodeSeason(String idSeries, String idSeason, String idEpisode) throws JAXBException, JsonProcessingException {
		
		if (request().queryString().isEmpty()) {
			Episode episode = SeriesUtil.createEpisodesDetail(idSeason, idEpisode);
			if(episode != null){
				
				return ok(ObjectResponseFormatter.objectResponse(episode));
			}
			else{
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No episode found with  seried ID equal to:'" + idSeries + "', season ID equal to: '" + idSeason + "' and episode ID equal to: '" + idEpisode + "'");

				return notFound(error.marshalError());
			}
		}
		
		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());
	}
}
