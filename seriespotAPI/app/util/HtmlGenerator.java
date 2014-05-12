package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer; 
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource; 
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// For write operation
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;

import org.stringtemplate.v4.*;

public class HtmlGenerator {

	private static DocumentBuilder documentBuilder;

    public static String generator(String xmlInput){
    	
    	try {
	    	File stylesheet = new File("xslt/template.xsl");
	    	
	    	Document document = getDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(xmlInput.getBytes("utf-8"))));
	    	
	    	TransformerFactory tFactory = TransformerFactory.newInstance();
	    	StreamSource stylesource = new StreamSource(stylesheet); 
	        Transformer transformer = tFactory.newTransformer(stylesource);
	        
	        StringWriter writer = new StringWriter();
	        StreamResult result = new StreamResult(writer);
	        transformer.transform(new DOMSource(document), result);
	        
	        writer.flush();
	        AutoIndentWriter writerIndent = new AutoIndentWriter(writer);
	        return writer.toString();
    	}
    	catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());

            // Use the contained exception, if any
            Throwable x = tce;

            if (tce.getException() != null) {
                x = tce.getException();
            }

            x.printStackTrace();
        } catch (TransformerException te) {
            // Error generated by the parser
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());

            // Use the contained exception, if any
            Throwable x = te;

            if (te.getException() != null) {
                x = te.getException();
            }

            x.printStackTrace();
        } catch (SAXException sxe) {
            // Error generated by this application
            Exception x = sxe;

            if (sxe.getException() != null) {
                x = sxe.getException();
            }

            x.printStackTrace();
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        }
    	
    	
		return null;
    	
    }
    
    private static DocumentBuilder getDocumentBuilder() {
		if (documentBuilder == null) {
			try {
				documentBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return documentBuilder;
	}

}