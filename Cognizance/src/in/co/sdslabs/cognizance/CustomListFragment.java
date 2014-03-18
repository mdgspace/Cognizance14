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
					Log.v("eventname", eventname.get(i));
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
			for (int i = 0; i < eventname.size(); i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				Log.v("dfsd", eventname.get(i));
				hm.put("eventname", eventname.get(i));
				hm.put("eventoneliner",
						myDbHelper.getEventOneLiner(eventname.get(i)));
				x = myDbHelper.getImageX(eventname.get(i));
				y = myDbHelper.getImageY(eventname.get(i));
				hm.put(EVENTIMAGE,
						Integer.toString(Drawables.eventsImages[x][y]));
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
			// put dept name + event_name as title
			// put drawable as Integer
			data.putBoolean("dept", true);
			data.putString("event", eventname.get(pos));
			data.putString("deptt", deptName);
			// Toast.makeText(getActivity(), deptName + " " +
			// eventname.get(pos),
			// Toast.LENGTH_SHORT).show();
			data.putInt("icon", Drawables.eventsImages[11][getArguments()
					.getInt("pos")]);
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			isDepartment = getArguments().getBoolean("dept");
			if (isDepartment) {
				deptName = getArguments().getString("name");
			}
			// Toast.makeText(getActivity(), deptName,
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
		}

	}
}