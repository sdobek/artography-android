package kartography.models;

import java.util.Arrays;
import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("POI")
public class Poi extends ParseObject {

	String title;
	String artist;
	Date createdAt;
	String description;
	String artPhotoUrl;
	User uploadedByUser;
	String[] tags;
	String location;
	Boolean flagged = false;

	public Poi() {
		super();
	}

	public void setFields(String title, String artist, String description,
            ParseUser user, ParseGeoPoint location) {
        put("title", title);
        put("artist", artist);
        put("description", description);
        //put("user", user);
        put("uploadedByUsername", user.getUsername());
        put("location", location);
        put("flagged", false);
    }

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		this.title = title;
		put("title", title);
	}

	public String getArtist() {
		return getString("artist");

	}

	public void setArtist(String artist) {
		this.artist = artist;
		put("artist", artist);
	}

	public Date getCreatedAt() {
		return (Date) get("createdAt");
	}

	// public void setCreatedAt(Date createdAt) {
	// this.createdAt = createdAt;
	// no longer need as parse handles this
	// }

	public String getDescription() {
		return (String) get("description");
	}

	public void setDescription(String description) {
		this.description = description;
		put("description", description);
	}

	public String getArtPhotoUrl() {
		return (String) get("artPhotoUrl");
	}

	public void setArtPhotoUrl(String artPhotoUrl) {
		this.artPhotoUrl = artPhotoUrl;
		put("artPhotoUrl", artPhotoUrl);
	}

	public String getUploadedByUser() {
		return (String) get("uploadedByUser");
	}

	// public void setUploadedByUser(User uploadedByUser) {
	// this.uploadedByUser = uploadedByUser;
	// probably won't need this one either.
	// }

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	// public String getLocation() {
	// return (String) get("location");
	// }
	//
	// public void setLocation(PoiLocation location) {
	// this.location = location.toString();
	// }

	public Boolean getFlagged() {
		return (Boolean) get("flagged");
	}

	public void setFlagged(Boolean flagged) {
		this.flagged = flagged;
		put("flagged", flagged);
	}

	@Override
	public String toString() {
		return "Poi [title=" + getTitle() + ", artist=" + getArtist()
				+ ", createdAt=" + getCreatedAt() + ", description="
				+ getDescription() + ", artPhotoUrl=" + getArtPhotoUrl()
				+ ", uploadedByUser=" + uploadedByUser + ", tags="
				+ Arrays.toString(tags) + ", location=" + getLocation()
				+ ", flagged=" + getFlagged() + "]";
	}

	public void setPhotoFile(ParseFile photoFile) {
		put("photoFile", photoFile);

	}

	public ParseFile getPhotoFile() {
		return getParseFile("photoFile");
	}

	public void setPhotoFileScaled(ParseFile photoFileScaled) {
		put("photoFileScaled", photoFileScaled);

	}

	public ParseFile getPhotoFileScaled() {
		return getParseFile("photoFileScaled");
	}

	public void setPhotoFileThumbnail(ParseFile photoFileThumbNail) {
		put("photoFileThumbnail", photoFileThumbNail);

	}

	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}

	public void setLocation(ParseGeoPoint value) {
		put("location", value);
	}

	

}
