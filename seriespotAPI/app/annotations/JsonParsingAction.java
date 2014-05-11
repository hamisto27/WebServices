package annotations;

import play.*;
import play.mvc.Action;
import play.mvc.*;
import play.libs.F.*;

import play.libs.Json;

public class JsonParsingAction extends Action<FromJsonTo> {
  
  public final static String JSON_FORMAT = "Application/json";

  @Override
  public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

  	  if(ctx.request().getHeader("Content-Type").equalsIgnoreCase(JSON_FORMAT)){

  	  	Logger.debug("Content type request: " + ctx.request().getHeader("Content-Type"));
  	  	  
	      Class<?> clazz = configuration.value();
	      Object jsonObj = Json.fromJson(ctx.request().body().asJson(), clazz);
	      ctx.args.put("ObjectRequest", jsonObj);
	  }

      return delegate.call(ctx);
  }

}