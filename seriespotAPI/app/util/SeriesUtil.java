package util;

import models.Series;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SeriesUtil {

	private static final String ID_1 = "seriesid";
	private static final String ID_2 = "id";
	private static final String NAME = "SeriesName";
	private static final String OVERVIEW = "Overview";
	private static final String GENRE = "Genre";
	private static final String RATING = "Rating";
	private static final String POSTER = "poster";
	private static final String STATUS = "Status";

	private static DocumentBuilder documentBuilder;

	private static ArrayList<Series> createSeriesList(String seriesName,
			Integer limit) {

		ArrayList<Series> baseSeriesList = new ArrayList<Series>();
		String requestUrl = "http://thetvdb.com/api/GetSeries.php?seriesname="
				+ seriesName;

		NodeList idNode = null;
		NodeList nameNode = null;
		NodeList overviewNode = null;

		try {
			Document doc = getDocumentBuilder().parse(requestUrl);
			doc.getDocumentElement().normalize();
			idNode = doc.getElementsByTagName(ID_1);
			nameNode = doc.getElementsByTagName(NAME);
			overviewNode = doc.getElementsByTagName(OVERVIEW);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; (i <= idNode.getLength()) && (i < limit || limit == 0); i++) {

			if (idNode.item(i) != null && nameNode.item(i) != null
					&& overviewNode.item(i) != null) {

				Series series = new Series();
				series.setId(idNode.item(i).getTextContent());
				series.setName(nameNode.item(i).getTextContent());
				series.setOverview(overviewNode.item(i).getTextContent());
				baseSeriesList.add(series);
			}
		}

		return baseSeriesList;

	}

	public static Series createDetailSeries(String id) {
		
		NodeList idNode = null;
		NodeList genreNode = null;
		NodeList ratingNode = null;
		NodeList posterNode = null;
		NodeList seriesNameNode = null;
		NodeList overviewNode = null;
		NodeList statusNode = null;

		try {
			Document doc = getDocumentBuilder().parse(
					"http://thetvdb.com/api/55D4BDC0A1305510/series/" + id);
			
			doc.getDocumentElement().normalize();
			idNode = doc.getElementsByTagName(ID_2);
			genreNode = doc.getElementsByTagName(GENRE);
			ratingNode = doc.getElementsByTagName(RATING);
			seriesNameNode = doc.getElementsByTagName(NAME);
			posterNode = doc.getElementsByTagName(POSTER);
			overviewNode = doc.getElementsByTagName(OVERVIEW);
			statusNode = doc.getElementsByTagName(STATUS);
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(idNode == null)
			return null;
		
		else{
			
			Series series = new Series();
			
			series.setId(idNode.item(0).getTextContent());
			
			if(seriesNameNode != null)
				series.setName(seriesNameNode.item(0).getTextContent());
			if(overviewNode != null)
				series.setOverview(overviewNode.item(0).getTextContent());
			if(posterNode != null)
				series.setPoster(posterNode.item(0).getTextContent());
			if(genreNode != null)
				series.setGenre(genreNode.item(0).getTextContent());
			if(ratingNode != null)
				series.setRating(ratingNode.item(0).getTextContent());
			if(statusNode != null){
				if (statusNode.item(0).getTextContent().equalsIgnoreCase("CONTINUING"))
					series.setStatus(Series.Status.CONTINUING);
				else
					series.setStatus(Series.Status.ENDED);
			}
			return series;
		}

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

		public SeriesListBuilder() {
			;
		}

		public ArrayList<Series> buildSeriesList() {
			return createSeriesList(seriesName, limit);
		}

		public SeriesListBuilder seriesName(String seriesName) {
			this.seriesName = seriesName;
			return this;
		}

		public SeriesListBuilder limit(Integer limit) {
			this.limit = limit;
			return this;
		}
	}
}
