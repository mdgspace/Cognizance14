package in.co.sdslabs.cognizance;

import java.io.IOException;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventFragment extends Fragment {

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

		return v;
	}

}
