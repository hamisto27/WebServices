package controllers;

import javax.xml.bind.JAXBException;

import play.mvc.Result;

import com.fasterxml.jackson.core.JsonProcessingException;

import annotations.BaseController;

public class SeasonController extends BaseController{
	
	public static Result getAllSeasons(String id) throws JAXBException, JsonProcessingException {
		return ok();
	}
	
	public static Result getSeason(String id, String idSeason) throws JAXBException, JsonProcessingException {
		return ok();
	}
	
}
