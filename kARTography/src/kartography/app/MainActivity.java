package kartography.app;


import kartography.app.MapActivity.ErrorDialogFragment;
import kartography.fragments.PoiListFragment;
import kartography.models.Poi;
import kartography.models.User;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, TabListener {

	Poi art;
	User user;
	ActionBar actionBar;
	Tab tabList;
	private PoiListFragment lFrag = null;
	// all dem maps
	private SupportMapFragment mMapFragment = null;
	private GoogleMap map;
	private LocationClient mLocationClient;
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// end of maps maps
	private ParseAnalytics ParseAnalytics = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ParseObject.registerSubclass(Poi.class);
		setContentView(R.layout.activity_main);

		makeMap();
		mLocationClient = new LocationClient(this, this, this);
		

		setupNavigationTabs();

		
	}


	private void makeMap() {
		mMapFragment = new SupportMapFragment();
		
	
	}


	private void setupNavigationTabs() {

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabList = actionBar.newTab().setText("List").setTag("PoiListFragment")
				.setIcon(R.drawable.ic_list).setTabListener(this);

		Tab tabMap = actionBar.newTab().setText("Map").setTag("PoiMapFragment")
				.setIcon(R.drawable.ic_map).setTabListener(this);
		
		actionBar.addTab(tabList);
		
		//no longer adding map fragment till things get a little more stable.  
		actionBar.addTab(tabMap);
				
		actionBar.selectTab(tabList);
		
		
	}
	
	@Override
	protected void onResumeFragments() {
		
		// hrm, this totally isn't working. 
		if(actionBar != null){ 
			actionBar.selectTab(tabList);
		}
		super.onResumeFragments();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addNewArt(MenuItem m) {
		Intent i = new Intent(this, TakePhotoActivity.class);
		startActivity(i);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		
		android.support.v4.app.FragmentTransaction fts = manager
				.beginTransaction();
		
		
		
		//lazy instantiation for the win
		if (tab.getTag() == "PoiListFragment") {
			
//			FragmentManager manager = getSupportFragmentManager();
//			
//			android.support.v4.app.FragmentTransaction fts = manager
//					.beginTransaction();
			
//			fts.replace(R.id.frameContainer, new PoiListFragment());
			if (lFrag == null) {
				lFrag = new PoiListFragment();
			}
			
			fts.replace(R.id.frameContainer, lFrag, "HTL");
			fts.commit();
		} else {
//			lFrag.
			
//			mMapFragment = MapFragment.newInstance();
//			 FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//			 fragmentTransaction.add(R.id.frameContainer, mMapFragment);
////			 fragmentTransaction.rep
//			 fragmentTransaction.commit();
			
			
			
			
			
			
			
			//this worked
//			Intent i = new Intent(this, MapActivity.class);
//			startActivity(i);
			
			
			
			
			
			
			
			
			
//			fts.replace(R.id.frameContainer, new CustomMapFragment());
//			fts.replace(R.id.frameContainer, new PoiMapFragment());
//			if (mMapFragment == null) {
//				mMapFragment = new SupportMapFragment();
//				mFrag = new PoiMapFragment();
//				 mMapFragment = MapFragment.newInstance();
//				 fragmentTransaction.add(R.id.my_container, mMapFragment);
//				 fts.(R.id.frameContainer, mMapFragment);
//				 fts.commit();
				
				
//			}
			fts.replace(R.id.frameContainer, mMapFragment, "MF");
			fts.commit();
			manager.executePendingTransactions();
//			mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
			if (mMapFragment != null) {
				map = mMapFragment.getMap();
				if (map != null) {
					Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
					map.setMyLocationEnabled(true);
				} else {
					Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
			} 
			if (isGooglePlayServicesAvailable()) {
				
				mLocationClient.connect();
			}
			 
		}
		
	}
		
	

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
//		if (isGooglePlayServicesAvailable()) {
//			mLocationClient.connect();
//		}

	}

	
	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}
	
	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				mLocationClient.connect();
				break;
			}

		}
	}
	
	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}

			return false;
		}
	}
	
	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Location location = mLocationClient.getLastLocation();
		if (location != null) {
//			Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
			map.animateCamera(cameraUpdate);
		} else {
//			Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}


	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
		}
	}


	// Define a DialogFragment that displays the error dialog
		public static class ErrorDialogFragment extends DialogFragment {

			// Global field to contain the error dialog
			private Dialog mDialog;

			// Default constructor. Sets the dialog field to null
			public ErrorDialogFragment() {
				super();
				mDialog = null;
			}

			// Set the dialog to display
			public void setDialog(Dialog dialog) {
				mDialog = dialog;
			}

			// Return a Dialog to the DialogFragment.
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return mDialog;
			}
		}


	
	
}
