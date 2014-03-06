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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TakePhotoActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	private final float PHOTO_PRCNT = .5f;
	private final float PHOTO_SCALED_PRCNT = .25f;
	private final float PHOTO_THUMB_PRCNT = .05f;

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
	private Bitmap imageBitmap;
	private Bitmap imageBitmapScaled;
	private Bitmap imageBitmapThumbnail;
	TextView tvPhoto;
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
		tvPhoto = (TextView) findViewById(R.id.tv_Photo);
		ActionBar actionb = getActionBar();
		actionb.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void onBackPressed() {
		setResult(99);
		// super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.top_out, R.anim.bottom_in);
	}

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
			tvPhoto.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onStart() {
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
		//Using nested callbacks to make sure all photo sizes save correctly
		if (imageBitmap != null) {
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
						// Now try to upload the scaled image
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						imageBitmapScaled.compress(Bitmap.CompressFormat.PNG,
								100, stream);
						byte[] photoBytes = stream.toByteArray();
						photoFileScaled = new ParseFile("photo3.png",
								photoBytes);
						photoFileScaled.saveInBackground(new SaveCallback() {
							@Override
							public void done(ParseException e) {
								if (e != null) {
									Toast.makeText(
											TakePhotoActivity.this,
											"Error saving scaled: "
													+ e.getMessage(),
											Toast.LENGTH_LONG).show();
									pb.setVisibility(ProgressBar.INVISIBLE);
									submitBtn.setEnabled(false);
								} else {
									pointOfInterest
											.setPhotoFileScaled(photoFileScaled);
									// Lastly the thumbnail image
									ByteArrayOutputStream stream = new ByteArrayOutputStream();
									imageBitmapThumbnail.compress(
											Bitmap.CompressFormat.PNG, 100,
											stream);
									byte[] photoBytes = stream.toByteArray();
									photoFileThumbnail = new ParseFile(
											"photo4.png", photoBytes);
									photoFileThumbnail
											.saveInBackground(new SaveCallback() {
												@Override
												public void done(
														ParseException e) {
													if (e != null) {
														Toast.makeText(
																TakePhotoActivity.this,
																"Error saving thumb: "
																		+ e.getMessage(),
																Toast.LENGTH_LONG)
																.show();
														pb.setVisibility(ProgressBar.INVISIBLE);
														submitBtn.setEnabled(false);
													} else {
														//If everything worked, save
														pointOfInterest.setPhotoFileThumbnail(photoFileThumbnail);
														pointOfInterest.saveInBackground();
														pb.setVisibility(ProgressBar.INVISIBLE);
														TakePhotoActivity.this.finish();
													}

												}
											});
								}

							}
						});
					}

				}
			});

		}
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
				Picasso.with(this).load(takenPhotoUri).fit().skipMemoryCache()
						.into(photo);
				submitBtn.setEnabled(true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
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
