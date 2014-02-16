package kartography.fragments;

import kartography.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PoiListFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		//where you inflate xml
		return inflater.inflate(R.layout.fragment_list_poi, parent);
	}
	
	
}
