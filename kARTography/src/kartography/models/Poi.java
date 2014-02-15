package kartography.models;

import java.util.Arrays;
import java.util.Date;

public class Poi {
	
	String title;
	String artist;
	Date createdAt;
	String description;
	String artPhotoUrl;
	User uploadedByUser;
	String[] tags;
	PoiLocation location;
	Boolean flagged = false;
	
	//full art
	public Poi(String title, String artist, Date createdAt, String description,
			String artPhotoUrl, User uploadedByUser, String[] tags,
			PoiLocation location) {
		super();
		this.title = title;
		this.artist = artist;
		this.createdAt = createdAt;
		this.description = description;
		this.artPhotoUrl = artPhotoUrl;
		this.uploadedByUser = uploadedByUser;
		this.tags = tags;
		this.location = location;
	}
	
	//minimum viable object
	public Poi( Date createdAt, 
			String artPhotoUrl, User uploadedByUser,
			PoiLocation location, Boolean flagged) {
		super();
		this.title = "unknown";
		this.artist = "unknown";
		this.createdAt = createdAt;
		this.description = "";
		this.artPhotoUrl = artPhotoUrl;
		this.uploadedByUser = uploadedByUser;
		String[] tags = new String[]{"","","","","","","","",""};
		this.tags = tags;
		this.location = location;
		this.flagged = flagged;
	}

	public Poi(String string, String string2, Date date, String string3,
			String graffitiURL, User user, PoiLocation location2) {
		super();
		this.title = "unknown";
		this.artist = "unknown";
		this.createdAt = date;
		this.description = "";
		this.artPhotoUrl = graffitiURL;
		this.uploadedByUser = user;
		String[] tags = new String[]{"","","","","","","","",""};
		this.tags = tags;
		this.location = location2;
		this.flagged = false;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArtPhotoUrl() {
		return artPhotoUrl;
	}

	public void setArtPhotoUrl(String artPhotoUrl) {
		this.artPhotoUrl = artPhotoUrl;
	}

	public User getUploadedByUser() {
		return uploadedByUser;
	}

	public void setUploadedByUser(User uploadedByUser) {
		this.uploadedByUser = uploadedByUser;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public PoiLocation getLocation() {
		return location;
	}

	public void setLocation(PoiLocation location) {
		this.location = location;
	}

	public Boolean getFlagged() {
		return flagged;
	}

	public void setFlagged(Boolean flagged) {
		this.flagged = flagged;
	}

	@Override
	public String toString() {
		return "Poi [title=" + title + ", artist=" + artist + ", createdAt="
				+ createdAt + ", description=" + description + ", artPhotoUrl="
				+ artPhotoUrl + ", uploadedByUser=" + uploadedByUser
				+ ", tags=" + Arrays.toString(tags) + ", location=" + location
				+ ", flagged=" + flagged + "]";
	}
	
	
}
