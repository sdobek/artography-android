package kartography.app;

import java.util.List;

import kartography.models.Poi;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class PoiHandler {
	Poi poi;
	Parse parse;
	ParseAnalytics pa;
	ParseObject po;
	List<Poi> ohHaiPoi;
	
	public static interface PoiHandlerCallback {
	      public void onSuccess(List<Poi> results);
	      public void onFinished();
	    };
	
	public PoiHandler(){
		
	}
	
	
	public List<Poi> getPoi() {
		//Parse magic. 
		ohHaiPoi = null;
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
		            ohHaiPoi = itemList;
		        } else {
		            Log.d("item", "Error: " + e.getMessage());
		            Log.d("DEBUG", "Oh noooooooooooooooooooooooo");
		        }
		    }
		});
		
		
		return ohHaiPoi;
	}
	
	public Poi getSinglePoi(){
		// Define the class we would like to query
		ParseQuery<Poi> query = ParseQuery.getQuery(Poi.class);
		// Define our query conditions
		query.orderByAscending("createdAt").setLimit(10);
		
		//whereWithinMiles(key, point, maxDistance) <== we'll eventually use this one. 
		
		// Execute the find asynchronously
		Poi ohHaiPoi = null;
		query.findInBackground(new FindCallback<Poi>() {
		    public void done(List<Poi> itemList, ParseException e) {
		        if (e == null) {
		            // Access the array of results here
		            String firstItemId = itemList.get(0).getObjectId();
		            Log.d("DEBUG", firstItemId + "ER MER GERD");
		            Poi ohHaiPoi = itemList.get(0);
		        } else {
		            Log.d("item", "Error: " + e.getMessage());
		            Log.d("DEBUG", "Oh noooooooooooooooooooooooo");
		        }
		    }
		});
		
		return ohHaiPoi;
		
	}
	
}
