package kartography.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import kartography.app.MainActivity.ErrorDialogFragment;
import kartography.models.Poi;
import kartography.models.PoiLocation;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TakePhotoActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	// private final int PHOTO_DETAIL_PIXELS = 1024;
	// private final int PHOTO_SCALED_PIXELS = 512;
	// private final int PHOTO_THUMBNAIL_PIXELS = 128;
	private final float PHOTO_PRCNT = .8f;
	private final float PHOTO_SCALED_PRCNT = .5f;
	private final float PHOTO_THUMB_PRCNT = .15f;

	private static final int REQUEST_IMAGE_CAPTURE = 11;
	Poi pntOfInterest;
	PoiLocation loc;
	ImageView photo;
	EditText etTitle;

	// maps
	private LocationClient mLocationClient;
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	//

	private Uri fileUri;
	// byte[] photoBytes;
	private Poi pointOfInterest;
	private ParseFile photoFile;
	private ParseFile photoFileScaled;
	private ParseFile photoFileThumbnail;
	private ParseGeoPoint locationParse;
	private Bitmap imageBitmap;
	private Bitmap imageBitmapScaled;
	private Bitmap imageBitmapThumbnail;
	ProgressBar pb;
	Button submitBtn;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);

		// maps
		mLocationClient = new LocationClient(this, this, this);
		
		//
		etTitle = (EditText) findViewById(R.id.et_title);
		photo = (ImageView) findViewById(R.id.iv_Photo);
		pb = (ProgressBar) findViewById(R.id.pbLoading);
		submitBtn = (Button) findViewById(R.id.btn_submitPhoto);
		submitBtn.setEnabled(false);
		imageBitmap = null;
		imageBitmapScaled = null;
		ActionBar actionb = getActionBar();
		actionb.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void onBackPressed() {
		setResult(99);
		super.onBackPressed();
	}

//	 @Override
//	 public boolean onCreateOptionsMenu(Menu menu) {
//	 // Inflate the menu; this adds items to the action bar if it is present.
////	 getMenuInflater().inflate(R.menu.take_photo, menu);
//		 
//	 return true;
//	 }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in action bar clicked; go home
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}
	
	public void onTakePicture(View v) {
		// Intent to take photo using android camera
		// Return data to store photo
		Toast.makeText(getBaseContext(), "Loading Camera", Toast.LENGTH_SHORT)
				.show();

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					getPhotoFileUri("photo.jpg"));
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		// //
		super.onStart();
		if (isGooglePlayServicesAvailable()) {
			mLocationClient.connect();
		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);

			}

			return false;
		}
	}

	public void onSelectLocation(View v) {
		// Intent to set location of the POI
		// Return location data
	}

	public void onSavePOI(View v) {
		submitBtn.setEnabled(false);

		String author = ((EditText) findViewById(R.id.et_author)).getText()
				.toString();
		String title = ((EditText) findViewById(R.id.et_title)).getText()
				.toString();
		String description = ((EditText) findViewById(R.id.et_description))
				.getText().toString();
		ParseUser user = ParseUser.getCurrentUser();
		pointOfInterest = ParseObject.create(Poi.class);

		// guards against empty fields.
		if (title.isEmpty()) {
			title = "Unknown";
		}

		if (author.isEmpty()) {
			author = "Unknown";
		}

		Location lastloc = mLocationClient.getLastLocation();

		ParseGeoPoint location = new ParseGeoPoint(lastloc.getLatitude(),
				lastloc.getLongitude());

		pointOfInterest.setFields(title, author, description, user, location);

		pb.setVisibility(ProgressBar.VISIBLE);
		if (imageBitmap != null) {
			savePhoto();
			savePhotoScaled();
			savePhotoThumb();
		}
	}

	protected void savePhoto() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] photoBytes = stream.toByteArray();
		photoFile = new ParseFile("photo2.png", photoBytes);
		photoFile.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Toast.makeText(TakePhotoActivity.this,
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
					pb.setVisibility(ProgressBar.INVISIBLE);
					submitBtn.setEnabled(false);
				} else {
					pointOfInterest.setPhotoFile(photoFile);
					pointOfInterest.saveInBackground();
				}

			}
		});
	}

	protected void savePhotoScaled() {
		// Now try to upload the scaled image
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imageBitmapScaled.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] photoBytes = stream.toByteArray();
		photoFileScaled = new ParseFile("photo3.png", photoBytes);
		photoFileScaled.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Toast.makeText(TakePhotoActivity.this,
							"Error saving scaled: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
					pb.setVisibility(ProgressBar.INVISIBLE);
					submitBtn.setEnabled(false);
				} else {
					// Activity finished ok, return the data
					pointOfInterest.setPhotoFileScaled(photoFileScaled);
					pointOfInterest.saveInBackground();
					setResult(55);
					pb.setVisibility(ProgressBar.INVISIBLE);
					TakePhotoActivity.this.finish();
				}

			}
		});
	}

	public void savePhotoThumb() {
		// Lastly the thumbnail image
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imageBitmapThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] photoBytes = stream.toByteArray();
		photoFileThumbnail = new ParseFile("photo4.png", photoBytes);
		photoFileThumbnail.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Toast.makeText(TakePhotoActivity.this,
							"Error saving thumb: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
					pb.setVisibility(ProgressBar.INVISIBLE);
					submitBtn.setEnabled(false);
				} else {
					pointOfInterest.setPhotoFileThumbnail(photoFileThumbnail);
					pointOfInterest.saveInBackground();
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Uri takenPhotoUri = getPhotoFileUri("photo.jpg");
			try {
				imageBitmap = MediaStore.Images.Media.getBitmap(
						this.getContentResolver(), takenPhotoUri);

				int imgH = imageBitmap.getHeight();
				int imgW = imageBitmap.getWidth();
				// ^^ use these parameters to tell if photo is portrait or
				// landscape
				// and scale accordingly.
				imageBitmapScaled = Bitmap.createScaledBitmap(imageBitmap,
						(Math.round(imgW * PHOTO_SCALED_PRCNT)),
						(Math.round(imgH * PHOTO_SCALED_PRCNT)), true);
				imageBitmapThumbnail = Bitmap.createScaledBitmap(imageBitmap,
						(Math.round(imgW * PHOTO_THUMB_PRCNT)),
						(Math.round(imgH * PHOTO_THUMB_PRCNT)), true);
				// lastly scale the original bitmap
				imageBitmap = Bitmap.createScaledBitmap(imageBitmap,
						(Math.round(imgW * PHOTO_PRCNT)),
						(Math.round(imgH * PHOTO_PRCNT)), true);
				photo.setImageBitmap(imageBitmapScaled);
				submitBtn.setEnabled(true);
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
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
			Log.d("MyCameraApp", "failed to create directory");
		}
		// Specify the file target for the photo
		fileUri = Uri.fromFile(new File(mediaStorageDir.getPath()
				+ File.separator + fileName));
		return fileUri;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

}
