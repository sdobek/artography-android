package kartography.app;

import java.util.Date;

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
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends FragmentActivity implements TabListener {

	Poi art;
	User user;

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

	private void makeTestPoiObjectandUser() {
		Date date = new Date();
		String profileURL = "https://pbs.twimg.com/profile_images/378800000504479551/6e237aa9c711a6d3b23ff2ed07e09648.png";
		String graffitiURL = "http://www.thisiscolossal.com/wp-content/uploads/2012/04/tfs-1.jpg";
		user = new User("thatdood", "johnny", "comelately", date, profileURL);
		Long longitude = Long.getLong("37.792962");
		Long latidude = Long.getLong("-122.483236");
		PoiLocation location = new PoiLocation(longitude, latidude);
		art = new Poi("someart", "someartist", date, "test", graffitiURL, user,
				location);

	}

	private void setupNavigationTabs() {

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.setDisplayShowTitleEnabled(true);

		Tab tabHome = actionBar.newTab().setText("List")
				.setTag("PoiListFragment").setIcon(R.drawable.ic_launcher)
				.setTabListener(this);

		Tab tabMentions = actionBar.newTab().setText("Map")
				.setTag("PoiMapFragment").setIcon(R.drawable.ic_launcher)
				.setTabListener(this);

		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);

		actionBar.selectTab(tabHome);

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
		Toast.makeText(getBaseContext(), "hello", Toast.LENGTH_LONG).show();
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
			fts.replace(R.id.frameContainer, new PoiListFragment());
			// if (htlFrag == null) {
			// htlFrag = new HomeTimelineFragment();
			// }

			// fts.replace(R.id.frameContainer, htlFrag, "HTL");
		} else {
			fts.replace(R.id.frameContainer, new PoiMapFragment());
			// if (mFrag == null) {
			// mFrag = new MentionsFragment();
			// }
			// fts.replace(R.id.frameContainer, mFrag, "MF");
		}
		fts.commit();
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
