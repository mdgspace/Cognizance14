package in.co.sdslabs.cognizance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EventCategoryFragment extends ListFragment {

	//private ListView mEventList;
	private List<HashMap<String, String>> eventList;
	private SimpleAdapter mAdapter;

	private String EVENTNAME = "eventname";
	private String EVENTVENUE = "venue";

	public EventCategoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retrieving the currently selected item number
		 int position = getArguments().getInt("position");
		//
		// // List of rivers
		 String[] countries =
		 getResources().getStringArray(R.array.eventCategories);
		

		// Creating view correspoding to the fragment
		View v = inflater.inflate(R.layout.eventcategoryfragment_layout,
				container, false);

		eventList = new ArrayList<HashMap<String, String>>();

		TextView tv = (TextView) v.findViewById(R.id.tv_categoryDescription);
		tv.setText(countries[position]);
	//	mEventList = (ListView) v.findViewById(R.id.eventCategory_list);

		for (int i = 0; i < 100; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(EVENTNAME, "" + i);
			// hm.put(IMAGE, Integer.toString(mImages[i]));
			eventList.add(hm);
		}

		String[] from = { EVENTNAME };

		int[] to = { R.id.tv_event_name };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item
		mAdapter = new SimpleAdapter(getActivity().getBaseContext(), eventList,
				R.layout.eventcategory_list_item, from, to);

		setListAdapter(mAdapter);

		return v;
	}

}