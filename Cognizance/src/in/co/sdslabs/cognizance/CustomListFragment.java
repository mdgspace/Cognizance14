package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CustomListFragment extends ListFragment {

	private String EVENTNAME = "eventname";
	private String EVENTONELINER = "eventoneliner";
	private String EVENTIMAGE = "eventimage";
	ArrayList<String> eventname;
	ArrayList<String> eventoneliner;
	FragmentManager fragmentManager;
	String deptName = null;
	String eventName = null;
	boolean isDepartment = false;

	public CustomListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.eventcategoryfragment_layout,
				container, false);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

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

		if (isDepartment) {

			try {
				eventname = myDbHelper.getDepartmentalEvents(deptName);

				for (int i = 0; i < eventname.size(); i++) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("eventname", eventname.get(i));
					hm.put(EVENTIMAGE, Integer
							.toString(Drawables.eventsImages[11][getArguments()
									.getInt("pos")]));
					eventList.add(hm);
				}
			} catch (Exception e) {
			}

		} else {
			eventname = myDbHelper.getFavouritesName();

			if (eventname.size() == 0) {
				Toast.makeText(getActivity().getBaseContext(),
						"There are no current Favourites", Toast.LENGTH_SHORT)
						.show();
				getActivity().getSupportFragmentManager().popBackStack();

			}
			int x, y;
			boolean isDeptevent;
			for (int i = 0; i < eventname.size(); i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("eventname", eventname.get(i));
				String[] values = new String[2];
				if ((eventname.get(i)).contains(":")) {
					values = (eventname.get(i)).split(":");
					eventName = values[0];
					deptName = values[1];
				} else
					values[0] = eventname.get(i);
				try {
					isDeptevent = myDbHelper.isDeptEvent(values[0]);
					if (isDeptevent) {
						hm.put(EVENTIMAGE,
								Integer.toString(Drawables.eventsImages[11][0]));
						//icon to be changed here
					} else {
						hm.put("eventoneliner",
								myDbHelper.getEventOneLiner(values[0]));
						x = myDbHelper.getImageX(values[0]);
						y = myDbHelper.getImageY(values[0]);
						hm.put(EVENTIMAGE,
								Integer.toString(Drawables.eventsImages[x][y]));
					}
				} catch (Exception e) {
					hm.put("eventoneliner",
							myDbHelper.getEventOneLiner(values[0]));
					x = myDbHelper.getImageX(values[0]);
					y = myDbHelper.getImageY(values[0]);
					hm.put(EVENTIMAGE,
							Integer.toString(Drawables.eventsImages[x][y]));
				}
				eventList.add(hm);
			}

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

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, 1, id);

		Bundle data = new Bundle();
		if (isDepartment) {
			data.putBoolean("dept", true);
			data.putString("event", eventname.get(pos));
			data.putString("deptt", deptName);
			data.putInt("icon", getArguments().getInt("pos"));
			Intent intent = new Intent(getActivity().getBaseContext(),
					EventActivity.class);
			intent.putExtras(data);
			startActivity(intent);
		} else {
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
			boolean isDeptevent = false;
			String[] values = new String[2];
			try {
				values = (eventname.get(pos)).split(":");
				isDeptevent = myDbHelper.isDeptEvent(values[0]);
			} catch (Exception e) {
			}
			if (isDeptevent) {
				data.putBoolean("dept", true);
				data.putString("event", values[0]);
				data.putString("deptt", values[1]);
				//icon to be changed
				data.putInt("icon", 0);
				Intent intent = new Intent(getActivity().getBaseContext(),
						EventActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			} else {
				data.putString("event", eventname.get(pos));
				Intent i = new Intent(getActivity().getBaseContext(),
						EventActivity.class);
				i.putExtras(data);
				startActivity(i);

			}

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			isDepartment = getArguments().getBoolean("dept");
			if (isDepartment) {
				deptName = getArguments().getString("name");
				((ActionBarActivity) activity).getSupportActionBar().setTitle(
						deptName);
			}
			// Toast.makeText(getActivity(), deptName,
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
		}

	}
}