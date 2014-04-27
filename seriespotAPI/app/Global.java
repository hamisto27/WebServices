import play.*;
import play.mvc.Action;
import play.mvc.Http.Request;
import java.lang.reflect.Method;
import play.mvc.*;
import play.libs.F.*;

import java.util.*;
import javax.xml.bind.JAXBContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.Wrapper;

import play.mvc.Http.Context;
import play.mvc.Http.RequestBody;
import play.api.http.MediaRange;

import annotations.*;
import models.*;


public class Global extends GlobalSettings {

    public final static String JSON_FORMAT = "Application/json";
    public final static String XML_FORMAT = "Application/xml";

    private JAXBContext context;
    private ObjectMapper mapper; 

	public void onStart(Application app) {
        
        /*try {
			List<ApplicationClass> applicationClasses =
  				Play.classes.getAnnotatedClasses(XmlRootElement.class);
    		List<Class> classes = new ArrayList<Class>();
    		for (ApplicationClass applicationClass : applicationClasses) {
      			classes.add(applicationClass.javaClass);
    		}
    		context = JAXBContext.newInstance(Wrapper.class, classes.toArray(new Class[]{}));
  		}
  		catch (JAXBException e) {
    		Logger.error(e, "Problem initializing jaxb context: %s",e.getMessage());
  		}

  		mapper = new ObjectMapper();*/

    }

    public void onStop(Application app) {
        //Logger.info("Application shutdown...");
    }

    //@FromXmlTo(User.class)
    public Action onRequest(Request request, Method actionMethod) {

    	return new Action.Simple() {
            public Promise<SimpleResult>  call(Context ctx) throws Throwable {

                RequestBody body = ctx.request().body();
                List<MediaRange> mediaRanges = ctx.request().acceptedTypes();
                // set content type response.
                setContentTypeResponse(mediaRanges, ctx);
   
                /*ctx.response().setHeader("Access-Control-Allow-Origin", "*");
                ctx.response().setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
                ctx.response()
                        .setHeader("Access-Control-Allow-Headers",
                                "accept, origin, Content-type, x-json, x-prototype-version, x-requested-with, PLAY_SESSION");*/
                return delegate.call(ctx);
            }
        };
  		//gson = new GsonBuilder().create();
    	/*String[] acceptValues = request.headers().get("Accept"); 
        System.out.println("before each request..." + request.toString());
        System.out.println(acceptValues[0]);*/
        //return super.onRequest(request, actionMethod);

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