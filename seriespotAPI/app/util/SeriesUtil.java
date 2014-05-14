package util;

import models.Episode;
import models.Season;
import models.Series;
import models.Update;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.w3c.dom.DOMException;
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
	private static final String SEASON_ID = "seasonid";
	private static final String SEASON_NUMBER = "SeasonNumber";
	private static final String EPISODE_NUMBER = "EpisodeNumber";
	private static final String EPISODE_NAME = "EpisodeName";
	private static final String FIRST_AIRED = "FirstAired";
	private static final String TIME = "time";

	private static DocumentBuilder documentBuilder;

	private static List<Series> createSeriesList(String seriesName,
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

	public static Series createDetailSeries(Boolean isImdbId, String id) {
		
		NodeList idNode = null;
		NodeList genreNode = null;
		NodeList ratingNode = null;
		NodeList posterNode = null;
		NodeList seriesNameNode = null;
		NodeList overviewNode = null;
		NodeList statusNode = null;
		String url = null;
		if(!isImdbId) url = "http://thetvdb.com/api/55D4BDC0A1305510/series/" + id;
		else url = "http://www.thetvdb.com/api/55D4BDC0A1305510/series/" + id + "/all/en.xml";

		try {
			Document doc = getDocumentBuilder().parse(url);
			
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
				series.setPoster("http://www.thetvdb.com/banners/_cache/" + posterNode.item(0).getTextContent());
			if(genreNode != null)
				series.setGenre(genreNode.item(0).getTextContent());
			if(ratingNode != null)
				series.setRatingTvdb(ratingNode.item(0).getTextContent());
			if(statusNode != null){
				if (statusNode.item(0).getTextContent().equalsIgnoreCase("CONTINUING"))
					series.setStatus(Series.Status.CONTINUING);
				else
					series.setStatus(Series.Status.ENDED);
			}
			return series;
		}

	}
	
	public static List<Episode> createUpdateEpisodes(String seriesId) throws NumberFormatException, DOMException, ParseException {
		
		List<Episode> episodes = new ArrayList<Episode>();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		
		date = new Date();
		dateFormat.format(date);
		

		NodeList epi_idNode = null;
		NodeList epi_nameNode = null;
		NodeList seasonIdNode = null;
		NodeList epi_numberNode = null;
		NodeList overviewNode = null;
		NodeList first_airedNode = null;

		try {
			String requestUrl = ("http://www.thetvdb.com/api/55D4BDC0A1305510/series/"
					+ seriesId + "/all/en.xml");
			Document doc = getDocumentBuilder().parse(requestUrl);
			doc.getDocumentElement().normalize();

			epi_idNode = doc.getElementsByTagName(ID_2);
			epi_nameNode = doc.getElementsByTagName(EPISODE_NAME);
			seasonIdNode = doc.getElementsByTagName(SEASON_ID);
			epi_numberNode = doc.getElementsByTagName(EPISODE_NUMBER);
			overviewNode = doc.getElementsByTagName(OVERVIEW);
			first_airedNode = doc.getElementsByTagName(FIRST_AIRED);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(epi_idNode == null)
			return null;
		
		int count = 0;
		Season season = new Season(createDetailSeries(false, seriesId));
		
		for (int i = 0; i <= seasonIdNode.getLength(); i++) {
			if (seasonIdNode.item(i) != null && epi_nameNode.item(i) != null
					&& epi_numberNode.item(i) != null) {
				if ((first_airedNode.item(i).getTextContent() != null && first_airedNode.item(i).getTextContent() != "") && dateFormat.parse(first_airedNode.item(i).getTextContent()).compareTo(date) >= 0) {
					count++;
					season.setId(seasonIdNode.item(i).getTextContent());
					Episode episode = new Episode(season);
					episode.setId(epi_idNode.item(i + 1).getTextContent());
					episode.setName(epi_nameNode.item(i).getTextContent());
					episode.setNumber(Integer.parseInt(epi_numberNode.item(i).getTextContent()));
					episode.setFirstAired(first_airedNode.item(i)
							.getTextContent());
					episode.setOverview(overviewNode.item(i).getTextContent());
					episodes.add(episode);

				}
			}
		}

		return count == 0 ? null : episodes;

	}


	public static List<Season> createSeasonList(String seriesId) {

		List<Season> seasons = new ArrayList<Season>();

		NodeList epi_nameNode = null;
		NodeList seasonIdNode = null;
		NodeList seasonNumberNode = null;
		NodeList epi_numberNode = null;

		try {
			String requestUrl = ("http://www.thetvdb.com/api/55D4BDC0A1305510/series/"
					+ seriesId + "/all/en.xml");
			Document doc = getDocumentBuilder().parse(requestUrl);
			doc.getDocumentElement().normalize();
			
			epi_nameNode = doc.getElementsByTagName(EPISODE_NAME);
			seasonIdNode = doc.getElementsByTagName(SEASON_ID);
			seasonNumberNode = doc.getElementsByTagName(SEASON_NUMBER);
			epi_numberNode = doc.getElementsByTagName(EPISODE_NUMBER);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(seasonIdNode == null)
			return null;
		
		String current_season = null;
		Season season = null;
		List<Episode> episodes = new ArrayList<Episode>();
		
		for (int i = 0; i <= seasonIdNode.getLength(); i++) {
			if (seasonIdNode.item(i) != null && !seasonNumberNode.item(i).getTextContent().equals("0")) {
				
				if(i-1 >= 0 )
					current_season = seasonIdNode.item(i-1).getTextContent();
				
				if (!seasonIdNode.item(i).getTextContent().equals(current_season)) {
					
					if(episodes.size() > 0){
						season.setEpisodes(episodes);
						seasons.add(season);
					}
					
					season = new Season(Series.findById(seriesId));
					season.setId(seasonIdNode.item(i).getTextContent());
					season.setNumber(Integer.parseInt(seasonNumberNode.item(i).getTextContent()));
					
					episodes = new ArrayList<Episode>();
					Episode episode = new Episode(season);
					episode.setName(epi_nameNode.item(i).getTextContent());
					episode.setNumber(Integer.parseInt(epi_numberNode.item(i).getTextContent()));
					episodes.add(episode);
				}
				else{
					
					Episode episode = new Episode(season);
					episode.setName(epi_nameNode.item(i).getTextContent());
					episode.setNumber(Integer.parseInt(epi_numberNode.item(i).getTextContent()));
					episodes.add(episode);
				}
			}
		}

		return seasons;

	}
	
	public static Season createSeasonDetail(String seriesId, String seasonId) {
		
		NodeList epi_nameNode = null;
		NodeList seasonIdNode = null;
		NodeList seasonNumberNode = null;
		NodeList epi_numberNode = null;

		try {
			String requestUrl = ("http://www.thetvdb.com/api/55D4BDC0A1305510/series/"
					+ seriesId + "/all/en.xml");
			Document doc = getDocumentBuilder().parse(requestUrl);
			doc.getDocumentElement().normalize();
			
			epi_nameNode = doc.getElementsByTagName(EPISODE_NAME);
			seasonIdNode = doc.getElementsByTagName(SEASON_ID);
			seasonNumberNode = doc.getElementsByTagName(SEASON_NUMBER);
			epi_numberNode = doc.getElementsByTagName(EPISODE_NUMBER);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(seasonIdNode == null)
			return null;
		
		List<Episode> episodes = new ArrayList<Episode>();
		Season season = new Season(createDetailSeries(false, seriesId));
		int count = 0;
		
		for (int i = 0; i <= seasonIdNode.getLength(); i++) {
			if (seasonIdNode.item(i) != null && !seasonNumberNode.item(i).getTextContent().equals("0")) {
				
				if (seasonIdNode.item(i).getTextContent().equals(seasonId)) {
					
					Episode episode = new Episode(season);
					episode.setName(epi_nameNode.item(i).getTextContent());
					episode.setNumber(Integer.parseInt(epi_numberNode.item(i).getTextContent()));
					episodes.add(episode);
					
					if(count == 0){
						season.setId(seasonIdNode.item(i).getTextContent());
						season.setNumber(Integer.parseInt(seasonNumberNode.item(i).getTextContent()));
						count ++;
					}

				}
			}
		}
		
		season.setEpisodes(episodes);
		return count == 0 ? null : season;

	}
	
	public static List<Episode> createEpisodesList(String seriesId,
			String seasonId) {

		List<Episode> episodes = new ArrayList<Episode>();

		NodeList epi_idNode = null;
		NodeList epi_nameNode = null;
		NodeList seasonIdNode = null;
		NodeList epi_numberNode = null;
		NodeList overviewNode = null;
		NodeList first_airedNode = null;

		try {
			String requestUrl = ("http://www.thetvdb.com/api/55D4BDC0A1305510/series/"
					+ seriesId + "/all/en.xml");
			Document doc = getDocumentBuilder().parse(requestUrl);
			doc.getDocumentElement().normalize();

			epi_idNode = doc.getElementsByTagName(ID_2);
			epi_nameNode = doc.getElementsByTagName(EPISODE_NAME);
			seasonIdNode = doc.getElementsByTagName(SEASON_ID);
			epi_numberNode = doc.getElementsByTagName(EPISODE_NUMBER);
			overviewNode = doc.getElementsByTagName(OVERVIEW);
			first_airedNode = doc.getElementsByTagName(FIRST_AIRED);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(epi_idNode == null)
			return null;
		
		int count = 0;
		Season season = new Season(createDetailSeries(false, seriesId));
		season.setId(seasonId);
		
		for (int i = 0; i <= seasonIdNode.getLength(); i++) {
			if (seasonIdNode.item(i) != null && epi_nameNode.item(i) != null
					&& epi_numberNode.item(i) != null) {
				if (seasonIdNode.item(i).getTextContent().equals(seasonId)) {
					System.out.println(epi_numberNode.item(i).getTextContent() + " " + epi_idNode.item(i + 1).getTextContent());
					count++;
					Episode episode = new Episode(season);
					episode.setId(epi_idNode.item(i + 1).getTextContent());
					episode.setName(epi_nameNode.item(i).getTextContent());
					episode.setNumber(Integer.parseInt(epi_numberNode.item(i).getTextContent()));
					episode.setFirstAired(first_airedNode.item(i)
							.getTextContent());
					episode.setOverview(overviewNode.item(i).getTextContent());
					episodes.add(episode);

				}
			}
		}

		return count == 0 ? null : episodes;

	}
	
	public static Episode createEpisodesDetail(String seasonId, String episodeId) {

		NodeList series_idNode = null;
		NodeList epi_idNode = null;
		NodeList epi_nameNode = null;
		NodeList seasonIdNode = null;
		NodeList epi_numberNode = null;
		NodeList overviewNode = null;
		NodeList first_airedNode = null;

		try {
			String requestUrl = ("http://www.thetvdb.com/api/55D4BDC0A1305510/episodes/"
					+ episodeId + "/en.xml");
			Document doc = getDocumentBuilder().parse(requestUrl);
			doc.getDocumentElement().normalize();
			
			series_idNode = doc.getElementsByTagName(ID_1);
			epi_idNode = doc.getElementsByTagName(ID_2);
			epi_nameNode = doc.getElementsByTagName(EPISODE_NAME);
			seasonIdNode = doc.getElementsByTagName(SEASON_ID);
			epi_numberNode = doc.getElementsByTagName(EPISODE_NUMBER);
			overviewNode = doc.getElementsByTagName(OVERVIEW);
			first_airedNode = doc.getElementsByTagName(FIRST_AIRED);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(epi_idNode == null || !seasonIdNode.item(0).getTextContent().equals(seasonId))
			return null;
		else{
			
			Season season = new Season(createDetailSeries(false, series_idNode.item(0).getTextContent()));
			season.setId(seasonId);
			
			Episode episode = new Episode(season);
			episode.setId(epi_idNode.item(0).getTextContent());
			if(epi_nameNode != null)
				episode.setName(epi_nameNode.item(0).getTextContent());
			if(epi_numberNode != null)
				episode.setNumber(Integer.parseInt(epi_numberNode.item(0).getTextContent()));
			if(first_airedNode != null)
				episode.setFirstAired(first_airedNode.item(0).getTextContent());
			if( overviewNode != null)
				episode.setOverview(overviewNode.item(0).getTextContent());
			
			return episode;
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

		public List<Series> buildSeriesList() {
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
