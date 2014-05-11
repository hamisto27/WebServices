import play.*;
import play.mvc.Action;
import play.mvc.Http.Request;
import java.lang.reflect.Method;
import play.mvc.*;
import play.libs.F.*;

import java.util.*;

import play.mvc.Http.Context;
import play.api.http.MediaRange;



public class Global extends GlobalSettings {

    public final static String JSON_FORMAT = "Application/json";
    public final static String XML_FORMAT = "Application/xml"; 

	public void onStart(Application app) {
        

    }

    public void onStop(Application app) {
    	
    }

    public Action<Void> onRequest(Request request, Method actionMethod) {

    	return new Action.Simple() {
            public Promise<SimpleResult>  call(Context ctx) throws Throwable {

                List<MediaRange> mediaRanges = ctx.request().acceptedTypes();
                // set content type response.
                setContentTypeResponse(mediaRanges, ctx);
  
                return delegate.call(ctx);
            }
        };

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