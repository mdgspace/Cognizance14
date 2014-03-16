package in.co.sdslabs.cognizance;

import java.io.IOException;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends ActionBarActivity implements OnTouchListener {

	public EventActivity() {
	}
	
	DatabaseHelper myDbHelper;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);

		TextView eName = (TextView) findViewById(R.id.event_name);
		// TextView eOneliner = (TextView) v.findViewById(R.id.event_tag);
		TextView eDescription = (TextView) findViewById(R.id.event_description);

		TextView eVenue = (TextView) findViewById(R.id.event_venue);
		ImageView eventIcon = (ImageView) findViewById(R.id.event_ImView);

		myDbHelper = new DatabaseHelper(this);
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

		b = getIntent().getExtras();

		eName.setText(b.getString("event"));
		// eOneliner.setText(getArguments().getString("oneliner"));
		eDescription.setText(myDbHelper.getEventDescription(b
				.getString("event")));
		int x = myDbHelper.getImageX(b.getString("event"));
		int y = myDbHelper.getImageY(b.getString("event"));
		Log.v("Image", "x :" + x);
		Log.v("Image", "y :" + y);
		eventIcon.setImageResource(Drawables.eventsImages[x][y]);
		eVenue.setText(myDbHelper.getVenueDisplay(b.getString("event")));
		eVenue.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		showZoomedMap(myDbHelper.getVenueMap(b.getString("event")));
		return false;
	}

	private void showZoomedMap(String place) {

		PointF coord = myDbHelper.searchPlaceForCoordinates(place);
		Bundle mapParams = new Bundle();
		mapParams.putInt("mode", 1); // mode = 0 for normal and mode = 1 for
										// zoomed
		mapParams.putFloat("X", (float) coord.x);
		mapParams.putFloat("Y", (float) coord.y);

		Intent i = new Intent(this, in.co.sdslabs.mdg.map.CampusMap.class);
		i.putExtras(mapParams);
		startActivity(i);
		// myDbHelper.close();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	

}
