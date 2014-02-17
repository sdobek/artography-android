package kartography.app;


import java.util.Date;

import kartography.fragments.PoiListFragment;
import kartography.fragments.PoiMapFragment;
import kartography.models.Poi;
import kartography.models.PoiLocation;
import kartography.models.User;
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
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
		Parse.initialize(this, "wN6gpXkwVEF0d9eTw1YzE0ISX2WM8ACdXM0ueuiu", "dGycMyN2IxdihwSV6kzDiCufYAL9UBBQEpOiRmMn");
		setContentView(R.layout.activity_main);
		  
		setupNavigationTabs();

		
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

}
