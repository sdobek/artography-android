package kartography.app;

import kartography.models.Location;
import kartography.models.Poi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class TakePhotoActivity extends Activity {
	Poi pntOfInterest;
	Location loc;
	ImageView photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photo, menu);
		return true;
	}
	
	public void onTakePicture(View v){
		//Intent to take photo using android camera
		//Return data to store photo
	}
	
	public void onSelectLocation(View v){
		//Intent to set location of the POI
		//Return location data
	}
	
	public void onSavePOI(View v){
		//Save data for POI
		//Open mapview with added photo selected
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Will return intent data for taking photo/poi and setting location
	}

}
