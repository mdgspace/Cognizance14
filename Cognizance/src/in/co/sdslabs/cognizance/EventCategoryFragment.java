package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EventCategoryFragment extends ListFragment {

	// private ListView mEventList;
	private List<HashMap<String, String>> eventList;
	private SimpleAdapter mAdapter;

	private String EVENTNAME = "eventname";
	private String EVENTONELINER = "eventoneliner";
	private String EVENTIMAGE = "eventimage";

	int position;
	String[] categories;

	ArrayList<String> eventname;
	ArrayList<String> eventoneliner;

	FragmentManager fragmentManager;

	Bundle data;

	public EventCategoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		if (position == categories.length - 1) {

			eventname = myDbHelper.getDepartments();
			for (int i = 0; i < eventname.size(); i++) {

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(EVENTNAME, eventname.get(i));
				try {
					hm.put(EVENTIMAGE, Integer
							.toString(Drawables.eventsImages[position][i]));
				} catch (Exception e) {
					hm.put(EVENTIMAGE, "");
				}
				eventList.add(hm);
			}
		} else {

			eventname = myDbHelper.getEventName(categories[position]);
			eventoneliner = myDbHelper.getEventoneLiner(categories[position]);
			for (int i = 0; i < eventname.size(); i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(EVENTNAME, eventname.get(i));
				hm.put(EVENTONELINER, eventoneliner.get(i));
				hm.put(EVENTIMAGE,
						Integer.toString(Drawables.eventsImages[position][i]));
				// hm.put(IMAGE, Integer.toString(mImages[i]));
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
				.setActionBarTitle(categories[position]);
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
				categories[position]);

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
		final TextView listHeader = new TextView(getActivity());
		listHeader.setTextSize(20);
		listHeader.setPadding(15, 10, 5, 10);

		Typeface mTypeFace = Typeface.createFromAsset(
				getActivity().getAssets(), "Roboto-Medium.ttf");
		listHeader.setTypeface(mTypeFace);
		listHeader.setBackgroundColor(Color.TRANSPARENT);
		listHeader.setTextColor(Color.rgb(211,211, 211));
		// listHeader.setBackgroundResource(R.drawable.eventcat_competetions);
		listHeader.setClickable(false);
		listHeader.setText(myDbHelper
				.getCategoryDescription(categories[position]));
		listHeader.setGravity(Gravity.FILL_HORIZONTAL);

		getListView().addHeaderView(listHeader, null, false);
		// getListView().addFooterView(Color.rgb(1, 140, 149));
		setListAdapter(mAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);

		data = new Bundle();
		if (pos >= 1) {
			// To disable click on header
			if (position == categories.length - 1) {
				String deptName = eventname.get(pos - 1);
				// Toast.makeText(getActivity(), deptName, Toast.LENGTH_SHORT)
				// .show();
				showDepartmentalFragment(deptName, pos);

			} else {

				// Currently selected event
				String eventName = eventname.get(pos - 1);
				Bundle data = new Bundle();
				data.putString("event", eventName);
				Intent i = new Intent(getActivity().getBaseContext(),
						EventActivity.class);
				i.putExtras(data);
				startActivity(i);
			}
		}
	}

	private void showDepartmentalFragment(String deptName, int pos) {

		/**
		 * This method will open up a listView fragment containing all the
		 * events of a particular department
		 */
		// Call the CustomListFragment
		CustomListFragment eFragment = new CustomListFragment();
		// Getting reference to the FragmentManager

		data.putBoolean("dept", true);
		data.putString("name", deptName);
		data.putInt("pos", pos - 1);
		fragmentManager = getActivity().getSupportFragmentManager();
		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();

		eFragment.setArguments(data);

		// Adding a fragment to the fragment transaction
		ft.replace(R.id.content_frame, eFragment);
		ft.addToBackStack(null);

		// Committing the transaction
		ft.commit();

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		// Retrieving the currently selected item number

		position = getArguments().getInt("position");
		categories = getResources().getStringArray(R.array.eventCategories);

		MainNavDrawerActivity.initialTitle = categories[position];
	}

}