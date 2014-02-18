package kartography.fragments;

import kartography.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

public class CustomMapFragment extends Fragment {
	
	private MapView mMapView;
	private GoogleMap googleMap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// inflat and return the layout
		View v = inflater.inflate(R.layout.fragment_map_poi, container, false);
		mMapView = (MapView) v.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();//needed to get the map to display immediately
		
		try {
     		MapsInitializer.initialize(getActivity());
 		} catch (GooglePlayServicesNotAvailableException e) {
     		e.printStackTrace();
 		}
 		
 		googleMap = mMapView.getMap();
 		
 		//Perform any camera updates here
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}
}
