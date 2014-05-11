package controllers;

import java.util.List;

import models.User;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.api.http.MediaRange;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.Context;
import util.ErrorMessage;

import static play.mvc.Controller.response;

//@With(HttpsAction.class)
public class SecurityController extends Action.Simple {

    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";
    public final static String JSON_FORMAT = "Application/json";
    public final static String XML_FORMAT = "Application/xml"; 


    public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
        User user = null;
        String[] authTokenHeaderValues = ctx.request().headers().get(AUTH_TOKEN_HEADER);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
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
        return Promise.<SimpleResult>pure(unauthorized(error.marshalError()));
    }

    public static User getUser() {
        return (User)Http.Context.current().args.get("user");
    }


    // returns an authToken
    public static Result login() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }

        Login login = loginForm.get();

        User user = User.findByEmailAddressAndPassword(login.emailAddress, login.password);

        if (user == null) {
            return unauthorized();
        }
        else {
            String authToken = user.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(AUTH_TOKEN, authToken);
            return ok(authTokenJson);
        }
    }

    @With(SecurityController.class)
    public static Result logout() {
        response().discardCookie(AUTH_TOKEN);
        getUser().deleteAuthToken();
        return ok();
    }
    
    private void setContentTypeResponse(List<MediaRange> mediaRanges, Context ctx){

        String[] parts;
        
        for(play.api.http.MediaRange mediaRange: mediaRanges){
                parts = mediaRange.toString().split(";");
                if(parts[0].trim().equalsIgnoreCase(JSON_FORMAT)){

                    ctx.response().setContentType(JSON_FORMAT);
                    ctx.args.put("ContentTypeResponse", JSON_FORMAT);
                    return; 
                }
                if(parts[0].trim().equalsIgnoreCase(XML_FORMAT)){

                    ctx.response().setContentType(XML_FORMAT);
                    ctx.args.put("ContentTypeResponse", XML_FORMAT);
                    return; 
                }
        }

        ctx.response().setContentType(JSON_FORMAT);
        ctx.args.put("ContentTypeResponse", JSON_FORMAT);
        return; 
    }

    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String emailAddress;

        @Constraints.Required
        public String password;

    }


}