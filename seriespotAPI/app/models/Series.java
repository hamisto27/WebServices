package models;


public class Series extends BaseSeries{

	private String genre;
	private String poster;
	private String status;
	private String rating;


	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getPoster() {

		if(this.poster == null && this.getId() != null){
			setPoster("http://www.thetvdb.com/banners/_cache/posters/"+ this.getId() + "-1.jpg");
		}
		return poster;
	}
	private void setPoster(String poster) {
		this.poster = poster;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}


}