package in.co.sdslabs.cognizance;

import java.io.IOException;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends ActionBarActivity implements OnClickListener{

	public EventActivity() {
	}
	
	DatabaseHelper myDbHelper;
	Bundle b;
	boolean fav;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);

		TextView eName = (TextView) findViewById(R.id.event_name);
		TextView eOneLiner = (TextView) findViewById(R.id.event_oneliner);
		TextView eDescription = (TextView) findViewById(R.id.event_description);
		TextView eDate = (TextView) findViewById(R.id.event_date);
		TextView eTime = (TextView) findViewById(R.id.event_time);
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
		
		CheckBox star = (CheckBox)findViewById(R.id.star);
		if(myDbHelper.isFavourite(b.getString("event"))){
			fav = true;
			star.setChecked(true);
		}else {
			fav = false;
			star.setChecked(false);
		}
		
		star.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(fav){
					myDbHelper.unmarkAsFavourite(b.getString("event"));
				}else{
					myDbHelper.markAsFavourite(b.getString("event"));
				}
			}
		});

		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(b.getString("event"));
		
		eName.setText(b.getString("event"));
		eOneLiner.setText(myDbHelper.getEventOneLiner(b.getString("event")));
		eDescription.setText(myDbHelper.getEventDescription(b
				.getString("event")));
		int x = myDbHelper.getImageX(b.getString("event"));
		int y = myDbHelper.getImageY(b.getString("event"));
		Log.v("Image", "x :" + x);
		Log.v("Image", "y :" + y);
		
		eventIcon.setImageResource(Drawables.eventsImages[x][y]);
		eDate.setText("DATE : " + myDbHelper.getEventDate(b.getString("event")));
		eTime.setText("TIME : " + myDbHelper.getEventTime(b.getString("event")));
		eVenue.setTextColor(Color.rgb(1,140,149));
		eVenue.setText("VENUE : "+myDbHelper.getVenueDisplay(b.getString("event")));
		
		eVenue.setOnClickListener(this);
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
		super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {		
		showZoomedMap(myDbHelper.getVenueMap(b.getString("event")));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	       finish();	       	     
	    }
	    return super.onOptionsItemSelected(item);
	}
}