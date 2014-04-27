package annotations;

import play.*;
import play.mvc.Action;
import play.mvc.Http.Request;
import java.lang.reflect.Method;
import play.mvc.*;
import play.libs.F.*;

import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import util.XmlFormatter;
import util.Wrapper;

import play.mvc.Http.Context;
import play.mvc.Http.RequestBody;
import play.api.http.MediaRange;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import play.mvc.Http.Request;

public class XmlParsingAction extends Action<FromXmlTo> {
    
    public final static String XML_FORMAT  = "Application/xml";

  	@Override
  	public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

      if(ctx.request().getHeader("Content-Type").equalsIgnoreCase(XML_FORMAT)){
        
        Logger.debug("Content type request: " + ctx.request().getHeader("Content-Type"));
        Document doc = ctx.request().body().asXml();

        Object xmlObj = XmlFormatter.unmarshalObject(configuration.value(), doc);
      
        ctx.args.put("ObjectRequest", xmlObj);
      }

      return delegate.call(ctx);
  	}

}