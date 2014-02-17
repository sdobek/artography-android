package kartography.fragments;

import java.util.List;

import kartography.app.POIArrayAdapter;
import kartography.app.PoiHandler;
import kartography.app.R;
import kartography.models.Poi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class PoiListFragment extends Fragment{

	ListView lvPoi;
	POIArrayAdapter poiAdapter;
	PoiHandler poiHandler;
	List<Poi> images;
	ProgressBar pBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {

		
		View view = inflater.inflate(R.layout.fragment_list_poi, null, false);
		lvPoi = (ListView) view.findViewById(R.id.lvPoi);
		pBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		

		

       
//    for later    lvPoi.setOnScrollListener(endlessScrollListener);
		return view;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		/// this temporarily put on hold till we figure out callbacks
//		 poiHandler = new PoiHandler();
//		 
//		 images = poiHandler.getPoi();
		 
		List<Poi> ohHaiPoi = null;
		ParseQuery<Poi> query = ParseQuery.getQuery(Poi.class);
		query.findInBackground(new FindCallback<Poi>() {
		    public void done(List<Poi> itemList, ParseException e) {
		        if (e == null) {
		            // Access the array of results here
		        	for(Poi poi: itemList){
		        		String itemId = poi.getObjectId();
			            Log.d("DEBUG", itemId + "ER MER GERD");	
			            Log.d("DEBUG", poi.toString());
		        	}
		        	pBar.setVisibility(View.INVISIBLE);
		            images = itemList;
		            poiAdapter = new POIArrayAdapter(getActivity(), images);
		   	     	lvPoi.setAdapter(poiAdapter);
		        } else {
		            Log.d("item", "Error: " + e.getMessage());
		            Log.d("DEBUG", "Oh noooooooooooooooooooooooo");
		        }
		    }
		});
		 
		 
		
	
		super.onActivityCreated(savedInstanceState);
	}
	
}
