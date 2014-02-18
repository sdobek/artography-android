package kartography.app;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import kartography.models.Poi;
import kartography.models.PoiLocation;
import kartography.models.User;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;

public class TakePhotoActivity extends Activity {
	private static final int REQUEST_IMAGE_CAPTURE = 11;
	Poi pntOfInterest;
	PoiLocation loc;
	ImageView photo;
	private Uri fileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);
		
		photo = (ImageView) findViewById(R.id.iv_Photo);
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
		Toast.makeText(getBaseContext(), "Loading Camera", Toast.LENGTH_SHORT).show();
		
		
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	    	
	    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri("photo.jpg"));
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        
	    }
	}
	
	public void onSelectLocation(View v){
		//Intent to set location of the POI
		//Return location data
	}
	
	public void onSavePOI(View v){
		String author = ((EditText)findViewById(R.id.et_author)).getText().toString();
		String title = ((EditText)findViewById(R.id.et_title)).getText().toString();
		String description = ((EditText)findViewById(R.id.et_description)).getText().toString();
		//Some of the data is dummy to be replaced later
		Date date = new Date();
		User u = new User("Steven Dobek", "Steven", "Dobek", null, null);
		Poi pointOfInterest = new Poi(title, author, description,
				fileUri.toString(), u, null,
				null);
		pointOfInterest.saveInBackground();
		this.finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Uri takenPhotoUri = getPhotoFileUri("photo.jpg");
	        Bitmap imageBitmap;
			try {
				imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), takenPhotoUri);
				photo.setImageBitmap(imageBitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	}
	
	public Uri getPhotoFileUri(String fileName) {
		  File mediaStorageDir = new File(
		        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
		        "MyCameraApp");

		    // Create the storage directory if it does not exist
		    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
		        Log.d("MyCameraApp", "failed to create directory");
		    }
		    // Specify the file target for the photo
		    fileUri = Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator +
		                fileName));
		    return fileUri;
		}

}
