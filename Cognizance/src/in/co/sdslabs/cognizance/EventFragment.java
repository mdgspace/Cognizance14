package in.co.sdslabs.cognizance;

import java.io.IOException;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventFragment extends Fragment implements OnTouchListener {

	public EventFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.event_details, container, false);

		TextView eName = (TextView) v.findViewById(R.id.event_name);
		// TextView eOneliner = (TextView) v.findViewById(R.id.event_tag);
		TextView eDescription = (TextView) v
				.findViewById(R.id.event_description);

		TextView eVenue = (TextView) v.findViewById(R.id.event_venue);
		ImageView eventIcon = (ImageView) v.findViewById(R.id.event_ImView);

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

		eName.setText(getArguments().getString("event"));
		// eOneliner.setText(getArguments().getString("oneliner"));
		eDescription.setText(myDbHelper.getEventDescription(getArguments()
				.getString("event")));
		eventIcon.setImageResource(getArguments().getInt("image"));
		eVenue.setText(myDbHelper.getVenue(getArguments().getString("event")));
		eVenue.setOnTouchListener(this);
		return v;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		// fire an intent to show the zoomed in map
		showZoomedMap("LHC");
		return false;
	}

	private void showZoomedMap(String place) {
		// TODO Auto-generated method stub

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

		PointF coord = myDbHelper.searchPlaceForCoordinates(place);
		Bundle mapParams = new Bundle();
		mapParams.putInt("mode", 1); // mode = 0 for normal and mode = 1 for
										// zoomed
		mapParams.putFloat("X", (float) coord.x);
		mapParams.putFloat("Y", (float) coord.y);

		Intent i = new Intent(getActivity().getBaseContext(),
				in.co.sdslabs.mdg.map.CampusMap.class);
		i.putExtras(mapParams);
		startActivity(i);
	}
}
