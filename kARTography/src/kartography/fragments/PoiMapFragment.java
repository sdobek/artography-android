package kartography.fragments;
import kartography.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.SupportMapFragment;


public class PoiMapFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_map_poi, parent, false);
		
//		mLocationClient = new LocationClient(this, this, this);
//		mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
//		if (mapFragment != null) {
//			map = mapFragment.getMap();
//			if (map != null) {
//				Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
//				map.setMyLocationEnabled(true);
//			} else {
//				Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
//			}
//		} else {
//			Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
//		}
		
		return view;
	}
	
	
	
}
