package annotations;

import java.util.List;

import models.User;
import play.api.http.MediaRange;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;
import play.mvc.Http.Context;
import util.ErrorMessage;

public class BasicAuthAction extends Action<BasicAuth> {
	
	 private static final String AUTHORIZATION = "authorization";
	 private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	 private static final String REALM = "Basic realm=\"Your Realm Here\"";
	 private static final String JSON_FORMAT = "Application/json";
	 private static final String XML_FORMAT = "Application/xml"; 
	    
	 @Override
	  public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

	        String authHeader = ctx.request().getHeader(AUTHORIZATION);
	        if (authHeader == null) {
	        	ctx.response().setHeader(WWW_AUTHENTICATE, REALM);
	            return Promise.<SimpleResult>pure(unauthorized());
	        }

	        String auth = authHeader.substring(6);
	        byte[] decodedAuth = new sun.misc.BASE64Decoder().decodeBuffer(auth);
	        String[] credString = new String(decodedAuth, "UTF-8").split(":");

	        if (credString == null || credString.length != 2) {
	            return Promise.<SimpleResult>pure(unauthorized());
	        }
	        
	        List<MediaRange> mediaRanges = ctx.request().acceptedTypes();
	        // set content type response.
	        setContentTypeResponse(mediaRanges, ctx);
	        
	        String username = credString[0];
	        String password = credString[1];
	        User authUser = User.findByEmailAddressAndPassword(username, password);
	        if(authUser != null)
	        	ctx.args.put("LoginToken", authUser.createToken());
	        
	        return (authUser == null) ? Promise.<SimpleResult>pure(unauthorized(new ErrorMessage("Unauthorized", 401,
    				"No User found matching the criteria.").marshalError())) : delegate.call(ctx);
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
	 

}
