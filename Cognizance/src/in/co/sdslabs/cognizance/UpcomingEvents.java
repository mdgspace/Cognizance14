package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class UpcomingEvents extends ListFragment {

	// private ListView mEventList;
	private List<HashMap<String, String>> eventList;
	private SimpleAdapter mAdapter;

	private String EVENTNAME = "eventname";
	private String EVENTONELINER = "eventoneliner";
	private String EVENTIMAGE = "eventimage";

	int position;
	String[] categories;
	int day;
	int time;

	ArrayList<String> eventname;
	ArrayList<Long> startTime;
	ArrayList<String> eventoneliner;

	FragmentManager fragmentManager;

	Bundle data;

	public UpcomingEvents() {
		// TODO Auto-generated constructor stub
		// to be set to current date and time
		Calendar c = Calendar.getInstance();
		int d = c.get(Calendar.DAY_OF_WEEK);
		switch (d) {
		case 6:
			day = 1;
			break;
		case 7:
			day = 2;
			break;
		case 8:
			day = 3;
			break;
		default:
			day = 1;
		}
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		time = hour * 100 + min;
		
		Log.v("DAY" , "day :"+day );
		Log.v("DAY" , "time" + time);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Creating view corresponding to the fragment
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

		// checking recent
		startTime = myDbHelper.getUpcomingTime(day);
		eventname = myDbHelper.getUpcomingEventNames(day);

		for (int i = 0; i < startTime.size(); i++) {

			if (startTime.get(i) - time <= 100) {

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(EVENTNAME, eventname.get(i));
				hm.put(EVENTONELINER,
						myDbHelper.getEventOneLiner(eventname.get(i)));
				int x = myDbHelper.getImageX(eventname.get(i));
				int y = myDbHelper.getImageY(eventname.get(i));
				try {
					hm.put(EVENTIMAGE,
							Integer.toString(Drawables.eventsImages[x][y]));
				} catch (Exception e) {
					hm.put(EVENTIMAGE, "");
				}
				eventList.add(hm);
			}
		}

		String[] from = { EVENTNAME, EVENTONELINER, EVENTIMAGE };

		int[] to = { R.id.tv_eName, R.id.tv_eDescr, R.id.eventImage };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item

		mAdapter = new SimpleAdapter(getActivity().getBaseContext(), eventList,
				R.layout.eventcategory_list_item, from, to);

		((MainNavDrawerActivity) getActivity())
				.setActionBarTitle("Starting Shortly");
		return v;

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		// Currently selected event
		String eventName = eventname.get(position);
		Bundle data = new Bundle();
		data.putString("event", eventName);
		Intent i = new Intent(getActivity().getBaseContext(),
				EventActivity.class);
		i.putExtras(data);
		startActivity(i);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
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
		// final TextView listHeader = new TextView(getActivity());
		// listHeader.setTextSize(20);
		// listHeader.setPadding(15, 10, 5, 10);
		//
		// Typeface mTypeFace = Typeface.createFromAsset(
		// getActivity().getAssets(), "Roboto-Medium.ttf");
		// listHeader.setTypeface(mTypeFace);
		// listHeader.setBackgroundColor(Color.rgb(1, 140, 149));
		// //
		// listHeader.setBackgroundResource(R.drawable.eventcat_competetions);
		// listHeader.setClickable(false);
		// listHeader.setText(myDbHelper
		// .getCategoryDescription(categories[position]));
		// listHeader.setGravity(Gravity.FILL_HORIZONTAL);
		//
		// getListView().addHeaderView(listHeader, null, false);
		// getListView().addFooterView(Color.rgb(1, 140, 149));
		setListAdapter(mAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

}
