package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Day1 extends ListFragment {

	private String EVENTNAME = "eventname";
	private String EVENTONELINER = "eventoneliner";
	private String EVENTIMAGE = "eventimage";
	ArrayList<String> eventname;
	ArrayList<String> eventoneliner;
	FragmentManager fragmentManager;

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
		eventname = myDbHelper.getEventNamex(1);
		eventoneliner = myDbHelper.getEventoneLinerx(1);
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
		Log.v("Day", "1");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, 1, id);

		// To disable onClick on header

		showEventFragment(pos, v);

	}

	private void showEventFragment(int pos, View v) {

		// Currently selected event
		String eventName = eventname.get(pos);

		// Creating a fragment object
		EventFragment eFragment = new EventFragment();

		// Creating a Bundle object
		Bundle data = new Bundle();

		// Setting the index of the currently selected item of mDrawerList
		data.putString("event", eventName);
		data.putString("oneliner", eventoneliner.get(pos));
		data.putInt("image", Drawables.eventsImages[0][pos]);
		// Setting the position to the fragment
		eFragment.setArguments(data);

		// Getting reference to the FragmentManager
		fragmentManager = getActivity().getSupportFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();

		// Adding a fragment to the fragment transaction
		ft.replace(R.id.content_frame, eFragment);
		ft.addToBackStack(null);

		// Committing the transaction
		ft.commit();

	}
	
}