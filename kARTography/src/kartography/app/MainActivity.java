package kartography.app;

import java.util.Date;

import kartography.fragments.PoiListFragment;
import kartography.fragments.PoiMapFragment;
import kartography.models.Poi;
import kartography.models.PoiLocation;
import kartography.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.google.android.gms.maps.MapFragment;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends FragmentActivity implements TabListener {

	Poi art;
	User user;
	
	private PoiListFragment lFrag = null;
	private PoiMapFragment mFrag = null;
	private MapFragment mMapFragment = null;
	private ParseAnalytics ParseAnalytics = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ParseObject.registerSubclass(Poi.class);
//		Parse.initialize(this, "wN6gpXkwVEF0d9eTw1YzE0ISX2WM8ACdXM0ueuiu",
//				"dGycMyN2IxdihwSV6kzDiCufYAL9UBBQEpOiRmMn");
		setContentView(R.layout.activity_main);

		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			getFacebookInfo();
		}

		setupNavigationTabs();

	}

	private void getFacebookInfo() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							JSONObject userProfile = new JSONObject();
							try {
								// Populate the JSON object
								userProfile.put("facebookId", user.getId());
								userProfile.put("name", user.getName());
								updateViewsWithProfileInfo();
							} catch (JSONException e) {
								Log.d("FB_LOGIN","Error parsing returned user data.");
							}

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d("FB_LOGIN","The facebook session was invalidated.");
								onLogoutButtonClicked();
							} else {
								Log.d("FB_LOGIN","Some other error: "
												+ response.getError()
														.getErrorMessage());
							}
						}
					}
				});
		request.executeAsync();

	}

	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			updateViewsWithProfileInfo();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
		}
	}
	
	/*
	 * If we need to display in main activity
	 */
	private void updateViewsWithProfileInfo() {
		// TODO Auto-generated method stub
		
	}




	private void setupNavigationTabs() {

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.setDisplayShowTitleEnabled(true);

		Tab tabList = actionBar.newTab().setText("List").setTag("PoiListFragment")
				.setIcon(R.drawable.ic_list).setTabListener(this);

//		Tab tabMap = actionBar.newTab().setText("Map").setTag("PoiMapFragment")
//				.setIcon(R.drawable.ic_map).setTabListener(this);
		
		actionBar.addTab(tabList);
		
		//no longer adding map fragment till things get a little more stable.  
//		actionBar.addTab(tabMap);
				
		actionBar.selectTab(tabList);
		
		

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
//			 FragmentTransaction fragmentTransaction =
//			         getFragmentManager().beginTransaction();
//			 fragmentTransaction.add(R.id.frameContainer, mMapFragment);
////			 fragmentTransaction.rep
//			 fragmentTransaction.commit();
//			
			
			fts.replace(R.id.frameContainer, new PoiMapFragment());

			if (mFrag == null) {
				mFrag = new PoiMapFragment();
//				 mMapFragment = MapFragment.newInstance();
//				 fragmentTransaction.add(R.id.my_container, mMapFragment);
//				 fts.(R.id.frameContainer, mMapFragment);
//				 fts.commit();
				
				
			}
			fts.replace(R.id.frameContainer, mFrag, "MF");
			fts.commit();

		}
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	/*
	 * If we want a logout button
	 */
	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();

		// Go to the login view
		startLoginActivity();
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
