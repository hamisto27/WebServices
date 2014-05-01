package controllers;
import play.mvc.*;

import java.util.*;
import util.SeriesUtil;


import models.BaseSeries;


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
	
	public static Result addSeries(Integer idSeries) throws JAXBException, JsonProcessingException{
		
		return ok();
	}

}