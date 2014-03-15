package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class Day2 extends ListFragment{

	private String EVENTNAME = "eventname";
	private String EVENTONELINER = "eventoneliner";
	private String EVENTIMAGE = "eventimage";

	public Day2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		List<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
		DatabaseHelper myDbHelper = new DatabaseHelper(getActivity()
				.getBaseContext());
		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		ArrayList<String> eventname = myDbHelper.getEventNamex(2);
		ArrayList<String> eventoneliner = myDbHelper.getEventoneLinerx(2);
		Log.v("dfsd", eventname.get(1));
		for (int i = 0; i < eventname.size(); i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			Log.v("dfsd", eventname.get(i));
			hm.put("eventname", eventname.get(i));
			hm.put("eventoneliner", eventoneliner.get(i));
			// hm.put(EVENTIMAGE,
			// Integer.toString(Drawables.eventsImages[position][i]));
			// hm.put(IMAGE, Integer.toString(mImages[i]));
			hm.put("EVENTNAME", "");
			eventList.add(hm);
		}
		String[] from = { EVENTNAME, EVENTONELINER, EVENTIMAGE };

		int[] to = { R.id.tv_eName, R.id.tv_eDescr, R.id.eventImage };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item

		SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
				.getBaseContext(), eventList, R.layout.eventcategory_list_item,
				from, to);

		// Setting the adapter to the listView
		setListAdapter(mAdapter);
		Log.v("Day", "2");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
