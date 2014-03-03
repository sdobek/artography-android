package kartography.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
	private GoogleMap myMap;
	private LocationClient mLocationClient;
	private Location myLocation;

	private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
	private final Map<Marker, String> reversedMapMarker = new HashMap<Marker, String>();
	private final Map<Marker, String> imageStringMapMarker = new HashMap<Marker, String>();
//	private final Map<Marker, Bitmap> imageStringMapMarker = new HashMap<Marker, Bitmap>();
	private int mostRecentMapUpdate = 0;
	private boolean hasSetUpInitialLocation = false;
	private String selectedObjectId;
	private Location lastLocation = null;
	// Fields for the map radius in feet
	  private float radius = 100 ;
	  private float lastRadius = 100;
	
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// end of maps maps
	private ParseAnalytics ParseAnalytics = null;
	

	// Maximum results returned from a Parse query
	private static final int MAX_POST_SEARCH_RESULTS = 20;

	// Maximum post search radius for map in kilometers
	private static final int MAX_POST_SEARCH_DISTANCE = 100;
	
	  /*
	   * Constants for handling location results
	   */
	  // Conversion from feet to meters
	  private static final float METERS_PER_FEET = 0.3048f;

	  // Conversion from kilometers to meters
	  private static final int METERS_PER_KILOMETER = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            startActivity(new Intent(this, LoginHomeActivity.class));
            finish();
            return;
        }
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

		Tab tabList = actionBar.newTab().setText("List")
				.setTag("PoiListFragment").setIcon(R.drawable.ic_list_green)
				.setTabListener(this);

		Tab tabMap = actionBar.newTab().setText("Map").setTag("PoiMapFragment")
				.setIcon(R.drawable.ic_map_green).setTabListener(this);
		
//		tabMap.set
		TabHost tabhost;
		

		actionBar.addTab(tabList);

		// no longer adding map fragment till things get a little more stable.
		actionBar.addTab(tabMap);

		actionBar.selectTab(tabList);

	}

	
	
	@Override
	protected void onResumeFragments() {

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
		startActivityForResult(i, 7);
		overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
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
		
		// lazy instantiation for the win
		if (tab.getTag() == "PoiListFragment") {

			// FragmentManager manager = getSupportFragmentManager();
			//
			// android.support.v4.app.FragmentTransaction fts = manager
			// .beginTransaction();

			// fts.replace(R.id.frameContainer, new PoiListFragment());
			if (lFrag == null) {
				lFrag = new PoiListFragment();
			}

			fts.replace(R.id.frameContainer, lFrag, "HTL");
			fts.commit();
		} else {
			

			fts.replace(R.id.frameContainer, mMapFragment, "MF");
			fts.commit();
			manager.executePendingTransactions();
			// mMapFragment = ((SupportMapFragment)
			// getSupportFragmentManager().findFragmentById(R.id.map));
			if (mMapFragment != null) {
				myMap = mMapFragment.getMap();
				if (myMap != null) {
					Toast.makeText(this, "Map Fragment was loaded properly!",
							Toast.LENGTH_SHORT).show();
					myMap.setMyLocationEnabled(true);
				} else {
					Toast.makeText(this, "Error - Map was null!!",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Error - Map Fragment was null!!",
						Toast.LENGTH_SHORT).show();
			}
			if (isGooglePlayServicesAvailable()) {

				mLocationClient.connect();
			}

		}

	}
	
	/* 
	 * Set up the query to update the map view
	 */
	private void doMapQuery() {
		// final int myUpdateNumber = ++mostRecentMapUpdate;
		Location myLoc = (myLocation == null) ? lastLocation : myLocation;
		// If location info isn't available, clean up any existing markers
		if (myLoc == null) {
//			cleanUpMarkers(new HashSet<String>());
			return;
		}
		final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
		// Create the map Parse query
		ParseQuery<Poi> mapQuery = ParseQuery.getQuery(Poi.class);
		// Set up additional query filters
		mapQuery.whereWithinKilometers("location", myPoint,
				MAX_POST_SEARCH_DISTANCE);
		mapQuery.whereNotEqualTo("flagged", true);
		mapQuery.include("user");
		mapQuery.orderByDescending("createdAt");
//		mapQuery.setLimit(MAX_POST_SEARCH_RESULTS);
		// Kick off the query in the background
	
		mapQuery.findInBackground(new FindCallback<Poi>() {
			@Override
			public void done(List<Poi> objects, ParseException e) {
				if (e != null) {
					Log.d("DEBUG", "An error occurred while querying for map posts.", e);
					return;
				}
				/*
				 * Make sure we're processing results from the most recent
				 * update, in case there may be more than one in progress.
				 */

				// Posts to show on the map
				
				Set<String> toKeep = new HashSet<String>();
				// Loop through the results of the search
				for (final Poi poi : objects) {
					// Add this post to the list of map pins to keep
					toKeep.add(poi.getObjectId());
					// Check for an existing marker for this post
					
					// Set up the map marker's location
					MarkerOptions markerOpts = new MarkerOptions()
							.position(new LatLng(poi.getLocation()
									.getLatitude(), poi.getLocation()
									.getLongitude()));
					
						// Display a green marker with the post information
						markerOpts = markerOpts
								.title(poi.getTitle())
								.snippet(poi.getArtist())
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_paint_maps));
						//former icon
//						BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
					
					
					
					// Add a new marker
					Marker marker = myMap.addMarker(markerOpts);
					mapMarkers.put(poi.getObjectId(), marker);
					//made in reverse bc steven and I don't understand hashmaps
					// also so we can start detail activity dependent on the marker
					reversedMapMarker.put(marker, poi.getObjectId());

					// add
					String uriThumbNail = poi.getPhotoFileThumbnail().getUrl();
					imageStringMapMarker.put(marker,uriThumbNail);
					ImageView imageview = new ImageView(getApplicationContext());

					
					if (poi.getObjectId().equals(selectedObjectId)) {
						marker.showInfoWindow();
						

						selectedObjectId = null;
					}
				}
				
			}
		});
	}

	/*
	 * Helper method to get the Parse GEO point representation of a location
	 */
	private ParseGeoPoint geoPointFromLocation(Location loc) {
		return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
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
		// if (isGooglePlayServicesAvailable()) {
		// mLocationClient.connect();
		// }

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

		case 7:
			if(resultCode == 55){
			//evil android magic to wait for the parse server to update
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			    @Override
			    public void run() {
			    	
			    	
//					getActionBar().selectTab(tabList);
			    	
			    	FragmentManager manager = getSupportFragmentManager();

					android.support.v4.app.FragmentTransaction fts = manager
							.beginTransaction();
					fts.replace(R.id.frameContainer, new PoiListFragment());
					fts.commitAllowingStateLoss();
			    }
			}, 100);
			}
			
			break;
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			
			case Activity.RESULT_OK:
				mLocationClient.connect();
				break;
			}
			default:
			break;

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
				errorFragment.show(getSupportFragmentManager(),
						"Location Updates");
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
		myLocation = mLocationClient.getLastLocation();
		if (myLocation != null) {
			// Toast.makeText(this, "GPS location was found!",
			// Toast.LENGTH_SHORT).show();
			LatLng latLng = new LatLng(myLocation.getLatitude(),
					myLocation.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, 17);
			myMap.animateCamera(cameraUpdate);
			
			// very important method
			doMapQuery();
			//
			
			
			myMap.setInfoWindowAdapter(new CustomWindowAdapter(this.getLayoutInflater(), imageStringMapMarker, getApplicationContext()));
			myMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker steve) {
					String objectId = reversedMapMarker.get(steve).toString();
					Intent j = new Intent(getBaseContext(), PoiDetailActivity.class);
					j.putExtra("id", objectId);
					startActivity(j);
					
				}
			});
			myMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(final Marker steve) {
					
					    	
				 	steve.showInfoWindow();
				 	
			        final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
					    @Override
					    public void run() {
					    	steve.showInfoWindow();

					    }
					}, 400);

					return true;
				}
			});
		} else {
			// Toast.makeText(this,
			// "Current location was null, enable GPS on emulator!",
			// Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
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
					"Sorry. Location services not available to you",
					Toast.LENGTH_LONG).show();
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
