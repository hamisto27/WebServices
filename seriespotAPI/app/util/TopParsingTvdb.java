package util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.Series;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class TopParsingTvdb {

	/*private static class CreateTopsElement extends Thread {
         
		private TopSeriesWorkerListener listener;
		
		
		public CreateTopsElement(TopSeriesWorkerListener listener){
			
			
			this.listener = listener;
			
		
		}
        
		
		protected void done(List<Series> topseries) {
			if (this.listener != null) {
				listener.WorkerDone(topseries);
			}

		}
		
		@Override
		public void run() {
			
		
               
			for (int i = 0; i < TopParsingImdb.CreateTopSeries().size(); i++) {
               
				Series element = new Series();

			
				String requestUrl = "http://www.thetvdb.com/api/55D4BDC0A1305510/series/"
						+ GetIdTvDB(TopParsingImdb.CreateTopSeries().get(i)) + "/all/en.xml";

				Node nameNode = null;
				Node overviewNode = null;
				Node genreNode = null;
				Node runtimeNode = null;
				Node statusNode = null;
				Node ratingNode = null;

				DocumentBuilder db = TopParsingTvdb.getDocumentBuilder();
				try {

					Document doc;
					doc = db.parse(requestUrl);

					doc.getDocumentElement().normalize();
					nameNode = doc.getElementsByTagName("SeriesName").item(0);
					overviewNode = doc.getElementsByTagName("Overview").item(0);
					genreNode = doc.getElementsByTagName("Genre").item(0);
					runtimeNode = doc.getElementsByTagName("Runtime").item(0);
					statusNode = doc.getElementsByTagName("Status").item(0);
					ratingNode = doc.getElementsByTagName("Rating").item(0);

				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// setto parametri oggetto element da inserire nella lista top
				element.setId(GetIdTvDB(TopParsingImdb.CreateTopSeries().get(i)));
				element.setName(nameNode.getTextContent());
				element.setOverview(overviewNode.getTextContent());
				element.setGenre(genreNode.getTextContent());
				if (statusNode.getTextContent().equalsIgnoreCase("CONTINUING"))
					element.setStatus(Series.Status.CONTINUING);
				else
					element.setStatus(Series.Status.ENDED);
				element.setRating(ratingNode.getTextContent());
				tops.add(element);
			}

		done(tops);

	}
		
		private static String GetIdTvDB( String idImdb) {

		

			String requestUrl = "http://www.thetvdb.com/api/GetSeriesByRemoteID.php?imdbid="
					+ idImdb + "&language=en";
			Node idNode = null;

			DocumentBuilder db = TopParsingTvdb.getDocumentBuilder();
			try {

				Document doc;
				doc = db.parse(requestUrl);
				doc.getDocumentElement().normalize();
				idNode = doc.getElementsByTagName("seriesid").item(0);
				

			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			return idNode.getTextContent();

		}

	}

	static List<Series> tops;
	private static DocumentBuilder documentBuilder;

	public TopParsingTvdb(TopSeriesWorkerListener listener ) {

		if (tops == null) {
			
			tops = new ArrayList<Series>();
		}
		
		
		new CreateTopsElement(listener).start();

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



	public static List<Series> getTops() {
		return tops;
	}

	public static void setTops(List<Series> tops) {
		TopParsingTvdb.tops = tops;
	}*/

}