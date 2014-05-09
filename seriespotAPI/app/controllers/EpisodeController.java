package controllers;

import javax.xml.bind.JAXBException;

import play.mvc.Result;

import com.fasterxml.jackson.core.JsonProcessingException;

import annotations.BaseController;

public class EpisodeController extends BaseController{

	public static Result getAllEpisodeSeason(String id, String idSeason) throws JAXBException, JsonProcessingException {
		return ok();
	}
	
	public static Result getEpisodeSeason(String id, String idSeason, String idEpisode) throws JAXBException, JsonProcessingException {
		return ok();
	}
}
