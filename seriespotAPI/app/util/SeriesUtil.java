package util;

import models.BaseSeries;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SeriesUtil {

	private static final String ID = "seriesid";
  	private static final String NAME = "SeriesName";
  	private static final String OVERVIEW = "Overview";

  	private static DocumentBuilder documentBuilder;

	private static ArrayList<BaseSeries> createSeriesList(String seriesName, Integer limit) {
        
        ArrayList<BaseSeries> baseSeriesList = new ArrayList<BaseSeries>();
		String requestUrl="http://www.thetvdb.com/api/GetSeries.php?seriesname="+seriesName;

		NodeList idNode = null;
		NodeList nameNode = null;
		NodeList overviewNode = null;

		try {
			Document doc = getDocumentBuilder().parse(requestUrl);
			doc.getDocumentElement().normalize();
			idNode = doc.getElementsByTagName(ID);
			nameNode = doc.getElementsByTagName(NAME);
			overviewNode = doc.getElementsByTagName(OVERVIEW);


		}  catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i = 0; (i <= idNode.getLength()) && (i < limit || limit == 0); i++) {

			if(idNode.item(i) != null && nameNode.item(i) != null && overviewNode.item(i) != null  ) {

				BaseSeries baseSeries = new BaseSeries();
				baseSeries.setId(idNode.item(i).getTextContent());
				baseSeries.setName(nameNode.item(i).getTextContent());
				baseSeries.setOverview(overviewNode.item(i).getTextContent());
				baseSeriesList.add(baseSeries);
			}
		}

		return baseSeriesList;

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


	public static class SeriesListBuilder {
		private String seriesName;
		private Integer limit = 0;

		public SeriesListBuilder() { ; }

		public ArrayList<BaseSeries> buildSeriesList(){
			return createSeriesList(seriesName, limit);
		}

		public SeriesListBuilder seriesName(String seriesName){
        	this.seriesName = seriesName;
        	return this;
    	}

    	public SeriesListBuilder limit(Integer limit){
        	this.limit = limit;
        	return this;
    	}
	}
}
