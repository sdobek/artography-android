package kartography.models;

public class PoiLocation {

	Long longitude;
	Long latitude;
	Long accuracy;
	
	public PoiLocation(Long longitude, Long latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.accuracy = (long) 0.99;
	}

	public PoiLocation(Long longitude, Long latitude, Long accuracy) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.accuracy = accuracy;
	}

	@Override
	public String toString() {
		return "PoiLocation [longitude=" + longitude + ", latitude=" + latitude
				+ ", accuracy=" + accuracy + "]";
	}
	
	
	
}
