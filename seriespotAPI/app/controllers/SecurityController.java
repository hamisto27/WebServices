package controllers;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import models.User;

import annotations.BasicAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import play.api.http.MediaRange;
import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.Context;
import util.ErrorMessage;
import util.HtmlGenerator;

import static play.mvc.Controller.response;

//@With(HttpsAction.class)
public class SecurityController extends Action.Simple {

	public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
	public static final String AUTH_TOKEN = "authToken";
	public final static String JSON_FORMAT = "Application/json";
	public final static String XML_FORMAT = "Application/xml";
	private final static String HTML_FORMAT = "Application/xhtml+xml";

	public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
		User user = null;
		String[] authTokenHeaderValues = ctx.request().headers()
				.get(AUTH_TOKEN_HEADER);
		if ((authTokenHeaderValues != null)
				&& (authTokenHeaderValues.length == 1)
				&& (authTokenHeaderValues[0] != null)) {
			user = models.User.findByAuthToken(authTokenHeaderValues[0]);
			if (user != null) {
				// place where put data for the duration of the request.
				ctx.args.put("user", user);
				return delegate.call(ctx);
			}
		}
		// return a 401 response if I don't find any user.
		ErrorMessage error = new ErrorMessage("Unauthorized", 401,
				"You don't have permission to access to this URL.");
		List<MediaRange> mediaRanges = ctx.request().acceptedTypes();
		// set content type response.
		setContentTypeResponse(mediaRanges, ctx);
		return Promise.<SimpleResult> pure(unauthorized(error.marshalError()));
	}

	public static User getUser() {
		return (User) Http.Context.current().args.get("user");
	}

	// returns an authToken
	@BasicAuth
	public static Result login() throws JsonProcessingException, JAXBException {

		return ok(new Token(
				(String) play.mvc.Http.Context.current().args.get("LoginToken"))
				.marshalToken());
	}

	@With(SecurityController.class)
	public static Result logout() {
		response().discardCookie(AUTH_TOKEN);
		getUser().deleteAuthToken();
		return ok();
	}

	private void setContentTypeResponse(List<MediaRange> mediaRanges,
			Context ctx) {

		String[] parts;

		for (play.api.http.MediaRange mediaRange : mediaRanges) {
			parts = mediaRange.toString().split(";");
			if (parts[0].trim().equalsIgnoreCase(JSON_FORMAT)) {

				ctx.response().setContentType(JSON_FORMAT);
				ctx.args.put("ContentTypeResponse", JSON_FORMAT);
				return;
			}
			if (parts[0].trim().equalsIgnoreCase(XML_FORMAT)) {

				ctx.response().setContentType(XML_FORMAT);
				ctx.args.put("ContentTypeResponse", XML_FORMAT);
				return;
			}
			if (parts[0].trim().equalsIgnoreCase(HTML_FORMAT)) {

				ctx.response().setContentType(HTML_FORMAT);
				ctx.args.put("ContentTypeResponse", HTML_FORMAT);
				return;
			}
		}

		ctx.response().setContentType(JSON_FORMAT);
		ctx.args.put("ContentTypeResponse", JSON_FORMAT);
		return;
	}

	@XmlRootElement(name = "authToken")
	@JsonRootName("authToken")
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(propOrder = { "value" })
	public static class Token {

		@XmlElement(name = "value")
		@JsonProperty("value")
		public String value;

		public Token() {
			;
		}

		public Token(String value) {

			this.value = value;

		}

		public String marshalToken() throws javax.xml.bind.JAXBException,
				JsonProcessingException {

			String contentType = (String) play.mvc.Http.Context.current().args
					.get("ContentTypeResponse");
			if (contentType.equals("Application/json")) {

				return marshalConfig();
			} else if (contentType.equals("Application/xml")
					|| contentType.equals("Application/xhtml+xml")) {

				JAXBContext context = JAXBContext.newInstance(this.getClass());

				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						Boolean.TRUE);
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

				StringWriter stringWriter = new StringWriter();
				marshaller.marshal(this, stringWriter);

				if (contentType.equals("Application/xhtml+xml")) {

					return HtmlGenerator.generator(stringWriter.toString());
				}

				return stringWriter.toString();
			}

			return null;
		}

		private String marshalConfig() throws JsonProcessingException {

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
			ObjectWriter writer = objectMapper.writerWithType(Token.class);
			return writer.writeValueAsString(this);
		}

	}

}