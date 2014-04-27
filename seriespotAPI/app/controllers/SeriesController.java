package controllers;

import play.*;
import play.mvc.*;
import play.db.ebean.*;


import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.io.*;
import java.io.StringWriter;
import javax.persistence.*;

import util.Wrapper;
import util.SeriesUtil;

import play.data.format.*;

import models.BaseSeries;

import static play.libs.Json.toJson;
import static play.mvc.Controller.request;
import static play.mvc.Controller.response;

import util.*;
import javax.xml.bind.JAXBException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SeriesController extends Controller {

	public static Result searchSeries(String name, Integer limit) throws JAXBException, JsonProcessingException{
		
		name = name.replace('+', ' ');
		List<BaseSeries> series = new SeriesUtil.SeriesListBuilder()
										.seriesName(name)
										.limit(limit)
										.buildSeriesList();
		for (BaseSeries serie : series) {
    		System.out.println("NAME: " + serie.getName());
		}
		return ok(ObjectResponseFormatter.objectListResponse(series, BaseSeries.class, "/series?name=" + name));
	}

}