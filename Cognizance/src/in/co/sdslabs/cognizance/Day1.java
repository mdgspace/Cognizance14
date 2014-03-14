package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Day1 extends ListFragment {

	public Day1() {
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
		
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
//			hm.put(EVENTNAME, eventname.get(i));
//			hm.put(EVENTONELINER, eventoneliner.get(i));
//			hm.put(EVENTIMAGE,
//					Integer.toString(Drawables.eventsImages[position][i]));
//			// hm.put(IMAGE, Integer.toString(mImages[i]));
			hm.put("EVENTNAME" , "");
			eventList.add(hm);
		}

		Log.v("Day", "1");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
