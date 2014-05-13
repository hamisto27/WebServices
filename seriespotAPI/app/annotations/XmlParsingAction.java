package annotations;

import play.mvc.Action;
import play.mvc.*;
import play.libs.F.*;

import util.XmlFormatter;


import org.w3c.dom.Document;

public class XmlParsingAction extends Action<FromXmlTo> {
    
    public final static String XML_FORMAT  = "Application/xml";

  	@Override
  	public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

      if(ctx.request().getHeader("Content-Type").equalsIgnoreCase(XML_FORMAT)){
        
        Document doc = ctx.request().body().asXml();

        Object xmlObj = XmlFormatter.unmarshalObject(configuration.value(), doc);
      
        ctx.args.put("ObjectRequest", xmlObj);
      }

      return delegate.call(ctx);
  	}

}