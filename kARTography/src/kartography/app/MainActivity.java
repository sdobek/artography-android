package kartography.app;

import java.util.Date;

import kartography.models.Poi;
import kartography.models.PoiLocation;
import kartography.models.User;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends FragmentActivity implements TabListener {

	Poi art;
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "wN6gpXkwVEF0d9eTw1YzE0ISX2WM8ACdXM0ueuiu", "dGycMyN2IxdihwSV6kzDiCufYAL9UBBQEpOiRmMn");
		setContentView(R.layout.activity_main);
		   
		makeTestPoiObjectandUser();
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		//right now these crash the app :( will 
		//try to understand parse more. 
//		testObject.put("artObjectFoo", art);
//		testObject.add("user", user);
		testObject.saveInBackground();
		
		setupNavigationTabs();
		
	}

	private void makeTestPoiObjectandUser() {
		Date date = new Date();
		String profileURL = "https://pbs.twimg.com/profile_images/378800000504479551/6e237aa9c711a6d3b23ff2ed07e09648.png";
		String graffitiURL = "http://www.thisiscolossal.com/wp-content/uploads/2012/04/tfs-1.jpg";
		user = new User("thatdood", "johnny", "comelately", date, profileURL);
		Long longitude =  Long.getLong("37.792962");
		Long latidude = Long.getLong("-122.483236");
		PoiLocation location = new PoiLocation(longitude, latidude);
		art = new Poi("someart", "someartist", date, "test", graffitiURL, user, location);
		
	}

	private void setupNavigationTabs() {

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab().setText("List").setTag("PoiListFragment")
				.setIcon(R.drawable.ic_launcher).setTabListener(this);

		Tab tabMentions = actionBar.newTab().setText("Map").setTag("PoiMapFragment")
				.setIcon(R.drawable.ic_launcher).setTabListener(this);
		
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

		//lazy instantiation for the win
		if (tab.getTag() == "PoiListFragment") {
			fts.replace(R.id.frameContainer, new PoiListFragment());
//			if (htlFrag == null) {
//				htlFrag = new HomeTimelineFragment();
//			}
			
//			fts.replace(R.id.frameContainer, htlFrag, "HTL");
		} else {
			fts.replace(R.id.frameContainer, new PoiMapFragment());
//			if (mFrag == null) {
//				mFrag = new MentionsFragment();
//			}
//			fts.replace(R.id.frameContainer, mFrag, "MF");
		}
		fts.commit();
	}
		
	

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
