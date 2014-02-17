package kartography.fragments;

import kartography.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyFragment extends Fragment {

	private SupportMapFragment fragment;
	private GoogleMap map;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list_poi, container, false);
		
		
		
		/*   This Fragment was implemented as a possible way to add maps. will be expanded
		 *  or removed depending on which method of adding the map is chosen.     */
		
		
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentManager fm = getChildFragmentManager();
//		fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
//			fm.beginTransaction().replace(R.id.map_container, fragment).commit();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (map == null) {
			map = fragment.getMap();
			map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
		}
	}
}
