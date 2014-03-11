package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class EventCategoryFragment extends ListFragment {

	// private ListView mEventList;
	private List<HashMap<String, String>> eventList;
	private SimpleAdapter mAdapter;

	private String EVENTNAME = "eventname";
	private String EVENTVENUE = "eventvenue";
	private String EVENTTIME = "eventtime";

	public EventCategoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");

		String[] categories = getResources().getStringArray(
				R.array.eventCategories);

		// Creating view correspoding to the fragment
		View v = inflater.inflate(R.layout.eventcategoryfragment_layout,
				container, false);

		eventList = new ArrayList<HashMap<String, String>>();

		// Initialising DBhelper class

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
		// TextView tv = (TextView) v.findViewById(R.id.tv_categoryDescription);
		// tv.setText(categories[position]);
		// mEventList = (ListView) v.findViewById(R.id.eventCategory_list);

		/** Create function in DBhelper to return these three values **/
		// String Data = DBhelper.getReducedEvent(categories[position]);
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(EVENTNAME, "");
			hm.put(EVENTVENUE, "");
			hm.put(EVENTTIME, "");
			// hm.put(IMAGE, Integer.toString(mImages[i]));
			eventList.add(hm);
		}

		String[] from = { EVENTNAME };

		int[] to = { R.id.tv_eName, R.id.tv_eVenue, R.id.tv_eTime };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item
		mAdapter = new SimpleAdapter(getActivity().getBaseContext(), eventList,
				R.layout.eventcategory_list_item, from, to);

		setListAdapter(mAdapter);

		return v;
	}

}