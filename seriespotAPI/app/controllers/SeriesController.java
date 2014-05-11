package controllers;

import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.mvc.*;

import java.util.*;

import util.ErrorMessage;
import util.ObjectResponseFormatter;
import util.SeriesUtil;
import util.TopParsingImdb;

import models.Rating;
import models.Series;
import models.User;
import models.UserSeries;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import annotations.BaseController;
import annotations.FromJsonTo;
import annotations.FromXmlTo;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SeriesController extends BaseController {

	public static Result searchSeries(String name, String location,
			Integer limit) throws JAXBException, JsonProcessingException {

		if (name == null && limit == null && request().queryString().isEmpty()) {

			return ok(ObjectResponseFormatter.objectListResponse(
					Series.findAll(), Series.class, "/series"));

		} else {
			if (name != null) {
				name = name.replace('+', ' ');
				if (limit == null)
					limit = 0;

				List<Series> series = null;
				if (location.equalsIgnoreCase("TVDB")) {
					series = new SeriesUtil.SeriesListBuilder()
							.seriesName(name).limit(limit).buildSeriesList();
				} else if (location.equalsIgnoreCase("SerieSpot"))
					series = Series.findByName(name, limit);

				else {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"The following location are available :'SERIESPOT/seriespot' - 'tvdb/TVDB'");
					return Results.notFound(error.marshalError());
				}

				return ok(ObjectResponseFormatter.objectListResponse(series,
						Series.class, "/series?name=" + name + "&location="
								+ location + "&limit=" + limit));
			}

			ErrorMessage error = new ErrorMessage("Internal server error", 500,
					"Invalid URL format.");
			return internalServerError(error.marshalError());
		}

	}

	public static Result getSeries(String id, String location, String sort)
			throws JAXBException, JsonProcessingException {
		
		if (id.equalsIgnoreCase("RATINGS")) {
			
			if (request().queryString().isEmpty())
				return ok(ObjectResponseFormatter.objectListResponse(
						Rating.findAll(), Rating.class, "/series/ratings"));
			else if (request().getQueryString("sort") != null
					&& request().queryString().size() == 1) {
				if (request().getQueryString("sort").equalsIgnoreCase(
						"DESC")) {

					List<Rating> ratingsDesc = Rating.findAll();
					Comparator<Rating> desc = new SortRatingDesc();
					Collections.sort(ratingsDesc, desc);

					return ok(ObjectResponseFormatter.objectListResponse(
							ratingsDesc, Rating.class,
							"/series/ratings?sort=DESC"));
				} else if (request().getQueryString("sort")
						.equalsIgnoreCase("ASC")) {

					List<Rating> ratingsAsc = Rating.findAll();
					Comparator<Rating> asc = new SortRatingAsc();
					Collections.sort(ratingsAsc, asc);

					return ok(ObjectResponseFormatter.objectListResponse(
							ratingsAsc, Rating.class,
							"/series/ratings?sort=ASC"));
				} else {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"The following location are available :'SERIESPOT/seriespot' - 'tvdb/TVDB'");
					return Results.notFound(error.marshalError());
				}
			} else {
				ErrorMessage error = new ErrorMessage(
						"Internal server error", 500, "Invalid URL format.");
				return internalServerError(error.marshalError());
			}

		}
		
		if (request().queryString().isEmpty()) {

			Series series = Series.findById(id);

			if (series == null) {
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No series found with ID equal to:'" + id + "'");
				return Results.notFound(error.marshalError());
			}

			return ok(ObjectResponseFormatter.objectResponse(series));
		} else {
			if (routes.SeriesController.getSeries(id, location, sort).url()
					.equals("/series/top?location=" + location)) {
				return ok(ObjectResponseFormatter.objectListResponse(
						TopParsingImdb.createTopSeries(), Series.class,
						"/series/top?location=" + location));
			} else if (routes.SeriesController.getSeries(id, location, sort).url()
					.equals("/series/" + id + "?location=" + location)) {

				Series series = null;
				if (location.equalsIgnoreCase("TVDB")) {
					series = SeriesUtil.createDetailSeries(false, id);
					if (series == null) {
						ErrorMessage error = new ErrorMessage("Not Found", 404,
								"No series found with ID equal to:'" + id + "'");
						return Results.notFound(error.marshalError());
					}
				} else if (location.equalsIgnoreCase("SerieSpot")) {
					series = Series.findById(id);
					if (series == null) {
						ErrorMessage error = new ErrorMessage("Not Found", 404,
								"No series found with ID equal to:'" + id + "'");
						return Results.notFound(error.marshalError());
					}
				} else {
					ErrorMessage error = new ErrorMessage("Not Found", 404,
							"The following location are available :'SERIESPOT/seriespot' - 'tvdb/TVDB'");
					return Results.notFound(error.marshalError());
				}
				return ok(ObjectResponseFormatter.objectResponse(series));
			}

			ErrorMessage error = new ErrorMessage("Internal server error", 500,
					"Invalid URL format.");
			return internalServerError(error.marshalError());
		}
	}

	public static Result getRatedSeries(String id){
		return TODO;
		
	}
	
	
	@With(SecurityController.class)
	public static Result getAllUserSeries() throws JAXBException,
			JsonProcessingException {

		return ok(ObjectResponseFormatter.objectListResponse(
				models.UserSeries.getUserSeries(getUser().id),
				models.UserSeries.class, "/users/me/series"));
	}

	@With(SecurityController.class)
	public static Result getUserSeries(String id) throws JAXBException,
			JsonProcessingException {

		UserSeries series = UserSeries.findById(getUser().id, id);

		if (series == null) {
			ErrorMessage error = new ErrorMessage("Not Found", 404,
					"No series found with ID equal to:'" + id + "'");
			return Results.notFound(error.marshalError());
		}

		return ok(ObjectResponseFormatter.objectResponse(series));
	}

	@FromXmlTo(CreateSeries.class)
	@FromJsonTo(CreateSeries.class)
	@With(SecurityController.class)
	public static Result addSeries() throws JAXBException,
			JsonProcessingException {

		CreateSeries createSeries = bodyRequest(CreateSeries.class);
		Map<String, String> friendData = new HashMap<String, String>();

		friendData.put("seriesId", createSeries.seriesId);
		Form<CreateSeries> registerForm = Form.form(CreateSeries.class).bind(
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

		if (Series.findById(createSeries.seriesId) != null) {

			UserSeries.create(new UserSeries(getUser().id,
					createSeries.seriesId));
		} else {
			Series series = SeriesUtil.createDetailSeries(false,
					createSeries.seriesId);
			if (series != null) {

				Series.create(series);
				UserSeries.create(new UserSeries(getUser().id,
						createSeries.seriesId));
			} else {
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No series found in the database or usirng TvDB API with ID equal to:'"
								+ createSeries.seriesId + "'");
				return notFound(error.marshalError());
			}
		}

		response().setHeader(LOCATION,
				"/users/me/series/" + createSeries.seriesId);
		return created(ObjectResponseFormatter.objectResponse(UserSeries
				.findById(getUser().id, createSeries.seriesId)));

	}

	@FromXmlTo(RateSeries.class)
	@FromJsonTo(RateSeries.class)
	@With(SecurityController.class)
	public static Result rateSeries(String id) throws JAXBException,
			JsonProcessingException {

		RateSeries rateSeries = bodyRequest(RateSeries.class);
		Map<String, String> rateData = new HashMap<String, String>();

		rateData.put("rate", rateSeries.rate);
		Form<RateSeries> registerForm = Form.form(RateSeries.class).bind(
				rateData);

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

		UserSeries userSeries = UserSeries.findById(getUser().id, id);
		if (userSeries == null) {
			ErrorMessage error = new ErrorMessage("Not Found", 404,
					"No series found with ID equal to:'" + id + "'");
			return notFound(error.marshalError());
		}
		if(userSeries.getRate() == null){
			userSeries.setRate(Integer.parseInt(rateSeries.rate));
			UserSeries.update(userSeries);
		}
		else{
			ErrorMessage error = new ErrorMessage("Conflict", 409,
					"You have already rate the series with ID equal to:'" + id + "'");
			return status (Http.Status.CONFLICT, error.marshalError());
		}

		Rating rating = Rating.findBySeriesId(userSeries.getSeries().getId());

		if (rating == null) {
			rating = new Rating(userSeries.getSeries(),
					Integer.parseInt(rateSeries.rate), 1);
			Rating.create(rating);
		} else {
			ErrorMessage error = new ErrorMessage("Not Found", 404,
					"No series found with ID equal to:'" + id + "'");
			return notFound(error.marshalError());
		}

		// update global rating relative to one series.
		Series series = userSeries.getSeries();
		Float score = (float) (rating.getTotal() / rating.getVotes());
		series.setRating(score);
		series.update();

		return ok(ObjectResponseFormatter.objectResponse(userSeries));
	}

	@With(SecurityController.class)
	public static Result deleteSeries(String id) throws JAXBException,
			JsonProcessingException {

		if(request().queryString().isEmpty()){
			if (UserSeries.findById(getUser().id, id) == null) {
				ErrorMessage error = new ErrorMessage("Not Found", 404,
						"No series found with ID equal to:'" + id + "'");
				return notFound(error.marshalError());
			}
	
			UserSeries.deleteUserSeries(getUser().id, id);
			return noContent();
		}
		

		ErrorMessage error = new ErrorMessage("Internal server error", 500,
				"Invalid URL format.");
		return internalServerError(error.marshalError());
		
	}

	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}

	@XmlRootElement(name = "series")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CreateSeries {

		@Constraints.Required
		@XmlElement(name = "id")
		public String seriesId;

	}

	@XmlRootElement(name = "series")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class RateSeries {

		@Constraints.Required
		@Constraints.Pattern("[1-9]|10")
		@XmlElement(name = "rating")
		public String rate;

	}

	private static class SortRatingDesc implements Comparator<Rating> {

		public int compare(Rating rat1, Rating rat2) {

			if (rat1.getSeries().getRating() > rat2.getSeries().getRating())
				return -1;
			if (rat1.getSeries().getRating() == rat2.getSeries().getRating())
				return 0;

			return 1;
		}
	}

	private static class SortRatingAsc implements Comparator<Rating> {

		public int compare(Rating rat1, Rating rat2) {

			if (rat1.getSeries().getRating() < rat2.getSeries().getRating())
				return -1;
			if (rat1.getSeries().getRating() == rat2.getSeries().getRating())
				return 0;

			return 1;
		}
	}
}