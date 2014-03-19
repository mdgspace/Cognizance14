package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends ActionBarActivity implements OnClickListener {

	public EventActivity() {
	}

	DatabaseHelper myDbHelper;
	Bundle b;
	boolean fav;
	GPSTracker gps;
	TextView on, off;
	String dept_name;

	boolean isDept = false;
	
	protected static int HELLO_ID =1;

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

		CheckBox star = (CheckBox) findViewById(R.id.star);

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

		try {
			isDept = b.getBoolean("dept");
		} catch (Exception e) {

		}

		if (isDept) {
			dept_name = b.getString("deptt");
			final String event_name = b.getString("event");
			if (myDbHelper.isFavouriteD(event_name, dept_name)) {
				fav = true;
				star.setChecked(true);
			} else {
				fav = false;
				star.setChecked(false);
			}

			star.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (fav) {
						myDbHelper.unmarkAsFavouriteD(event_name, dept_name);
						// Add code to delete alarm
					} else {
						myDbHelper.markAsFavouriteD(event_name, dept_name);

						setDepartmentalAlarm(event_name, dept_name);
					}
				}
			});

			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(b.getString("event"));

			eName.setText(b.getString("event"));
			// eOneLiner
			// .setText(myDbHelper.getEventOneLiner(b.getString("event")));
			eDescription.setText(myDbHelper.getDepartmentalEventDescription(
					dept_name, event_name));

			try {
				eventIcon.setImageResource(Drawables.eventsImages[11][b
						.getInt("icon")]);
				// eventIcon.setImageResource(Drawables.eventsImages[x][y]);
			} catch (Exception e) {
				eventIcon.setImageResource(0);
			}
			eDate.setText("DATE : "
					+ myDbHelper.getEventDDate(event_name, dept_name));
			eTime.setText("TIME : " + setTimeD(dept_name, event_name));
			eVenue.setTextColor(Color.rgb(1, 140, 149));
			String venue = myDbHelper.getDepartmentalEventVenue(dept_name,
					event_name);
			if (venue.equals("")) {
				venue = dept_name;
				eVenue.setOnClickListener(null);
			} else {
				eVenue.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (v.getId() == R.id.event_venue)
							try {
								showZoomedMap(myDbHelper
										.getVenueMapD(dept_name));
							} catch (Exception e) {

							}
						else if (v.getId() == R.id.online) {
							if (myDbHelper.getVenueMap(dept_name) != "") {

							} else {
								try {
									PointF coord = myDbHelper
											.searchPlaceForLatLong(myDbHelper
													.getVenueMapD(dept_name));
									getPathFromPresentLocation(coord.x, coord.y);
								} catch (Exception e) {

								}
							}

						}
					}
				});
			}
			eVenue.setText("VENUE : " + venue);

		} else {

			if (myDbHelper.isFavourite(b.getString("event"))) {
				fav = true;
				star.setChecked(true);
			} else {
				fav = false;
				star.setChecked(false);
			}

			star.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {

					if (fav) {
						myDbHelper.unmarkAsFavourite(b.getString("event"));
					} else {
						myDbHelper.markAsFavourite(b.getString("event"));
					}
				}
			});

			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(b.getString("event"));

			eName.setText(b.getString("event"));
			eOneLiner
					.setText(myDbHelper.getEventOneLiner(b.getString("event")));
			eDescription.setText(myDbHelper.getEventDescription(b
					.getString("event")));
			int x = myDbHelper.getImageX(b.getString("event"));
			int y = myDbHelper.getImageY(b.getString("event"));
			Log.v("Image", "x :" + x);
			Log.v("Image", "y :" + y);

			try {
				eventIcon.setImageResource(Drawables.eventsImages[x][y]);
			} catch (Exception e) {
				eventIcon.setImageResource(0);
			}
			eDate.setText("DATE : "
					+ myDbHelper.getEventDate(b.getString("event")));
			eTime.setText("TIME : " + setTime(b.getString("event")));
			eVenue.setTextColor(Color.rgb(1, 140, 149));
			eVenue.setText("VENUE : "
					+ myDbHelper.getVenueDisplay(b.getString("event")));

			eVenue.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						showZoomedMap(myDbHelper.getVenueMap(b
								.getString("event")));
					} catch (Exception e) {

					}
				}
			});
		}

	}

	private void showZoomedMap(String place) {

		PointF coord = myDbHelper.searchPlaceForCoordinates(place);
		Bundle mapParams = new Bundle();
		mapParams.putInt("mode", 1); // mode = 0 for normal and mode = 1 for
										// zoomed
		mapParams.putFloat("X", (float) coord.x);
		mapParams.putFloat("Y", (float) coord.y);
		Log.i("coord : ", coord.x + " : " + coord.y);

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
		Log.i("venue", myDbHelper.getVenueMap("AHEC"));
		// if (v.getId() == R.id.event_venue)

		if (v.getId() == R.id.online) {
			PointF coord = myDbHelper.searchPlaceForLatLong(myDbHelper
					.getVenueMap(b.getString("event")));
			getPathFromPresentLocation(coord.x, coord.y);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			break;
		case R.id.navigate:
			// Log.i("nav", myDbHelper.getVenueMap(b.getString("event")));
			if (!isDept)
				showDialog();
			else
				showDialogD();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDialogD() {
		// TODO Auto-generated method stub
		PointF coord = myDbHelper.searchPlaceForLatLong(myDbHelper
				.getVenueMapD(dept_name));
		getPathFromPresentLocation(coord.x, coord.y);
	}

	public String setTime(String event) {

		int start = myDbHelper.getStartTime(event);
		int end = myDbHelper.getEndTime(event);

		String startX;
		String endX;

		if (start < 1200) {
			if (start % 100 == 0) {
				startX = start / 100 + ":" + "00 am";
			} else {
				startX = start / 100 + ":" + start % 100 + " am";
			}
		} else if (start >= 1200 && start < 1300) {
			if (start % 100 == 0) {
				startX = start / 100 + ":" + "00 pm";
			} else {
				startX = start / 100 + ":" + start % 100 + " pm";
			}
		} else {
			if (start % 100 == 0) {
				startX = (start / 100) - 12 + ":" + "00 pm";
			} else {
				startX = (start / 100) - 12 + ":" + start % 100 + " pm";
			}
		}

		if (end < 1300) {
			if (end % 100 == 0) {
				endX = end / 100 + ":" + "00 am";
			} else {
				endX = end / 100 + ":" + end % 100 + " am";
			}
			if (end > 1200 && end < 1300) {
				if (end % 100 == 0) {
					endX = end / 100 + ":" + "00 pm";
				} else {
					endX = start / 100 + ":" + end % 100 + " pm";
				}
			}
		} else {
			if (end % 100 == 0) {
				endX = (end / 100) - 12 + ":" + "00 pm";
			} else {
				endX = (end / 100) - 12 + ":" + end % 100 + " pm";
			}
		}

		return startX + " - " + endX;
	}

	public String setTimeD(String dept, String event) {

		int start = myDbHelper.getStartTimeD(dept, event);
		int end = myDbHelper.getEndTimeD(dept, event);

		String startX;
		String endX;

		if (start < 1200) {
			if (start % 100 == 0) {
				startX = start / 100 + ":" + "00 am";
			} else {
				startX = start / 100 + ":" + start % 100 + " am";
			}
		} else if (start >= 1200 && start < 1300) {
			if (start % 100 == 0) {
				startX = start / 100 + ":" + "00 pm";
			} else {
				startX = start / 100 + ":" + start % 100 + " pm";
			}
		} else {
			if (start % 100 == 0) {
				startX = (start / 100) - 12 + ":" + "00 pm";
			} else {
				startX = (start / 100) - 12 + ":" + start % 100 + " pm";
			}
		}

		if (end < 1300) {
			if (end % 100 == 0) {
				endX = end / 100 + ":" + "00 am";
			} else {
				endX = end / 100 + ":" + end % 100 + " am";
			}
			if (end > 1200 && end < 1300) {
				if (end % 100 == 0) {
					endX = end / 100 + ":" + "00 pm";
				} else {
					endX = start / 100 + ":" + end % 100 + " pm";
				}
			}
		} else {
			if (end % 100 == 0) {
				endX = (end / 100) - 12 + ":" + "00 pm";
			} else {
				endX = (end / 100) - 12 + ":" + end % 100 + " pm";
			}
		}

		return startX + " - " + endX;
	}

	private void showDialog() {
		// TODO Auto-generated method stub
		// final Dialog dialog = new Dialog(this);
		// dialog.setContentView(R.layout.navigate_options);
		// dialog.setTitle("Choose Mode of Navigation");
		//
		// try {
		// off = (TextView) findViewById(R.id.offline);
		// on = (TextView) findViewById(R.id.online);
		// //System.out.println(off.getText().toString());
		// off.setOnClickListener(this);
		// on.setOnClickListener(this);
		// } catch (Exception e) {
		// Log.i("navError", e.toString());
		// }
		//
		// dialog.show();
		PointF coord = myDbHelper.searchPlaceForLatLong(myDbHelper
				.getVenueMap(b.getString("event")));
		getPathFromPresentLocation(coord.x, coord.y);

	}

	private void getPathFromPresentLocation(double destLat, double destLong) {
		// TODO Auto-generated method stub
		// create class object
		gps = new GPSTracker(this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			onlineMap(latitude, longitude, destLat, destLong);

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
	}

	private void onlineMap(double startLat, double startLong, double destLat,
			double destLong) {
		// TODO Auto-generated method stub
		String uri = "http://maps.google.com/maps?saddr=" + startLat + ","
				+ startLong + "&daddr=" + destLat + "," + destLong;
		Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse(uri));
		intent1.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent1);
	}
	
	public void setDepartmentalAlarm(String event_name , String dept_name){
		
		Calendar cal = Calendar.getInstance();
		// Add code to set alarm
		
		int hr = myDbHelper.getStartTimeD( dept_name , event_name)/100;
		int min = myDbHelper.getStartTimeD( dept_name , event_name)%100;
		cal.set(Calendar.MONTH , 3);
		cal.set(Calendar.YEAR , 2014);
		cal.set(Calendar.DAY_OF_MONTH , myDbHelper.
				getEventDayDept(event_name,dept_name)+20);
		if(min>=30){
		cal.set(Calendar.HOUR_OF_DAY, hr);
		cal.set(Calendar.MINUTE , 00);
		}
		else{
			cal.set(Calendar.HOUR_OF_DAY, hr-1);
			cal.set(Calendar.MINUTE , 30);
		}
		
		Intent alarmIntent = new Intent(getApplicationContext() , AlarmReceiver.class);
		alarmIntent.putExtra("title", "Cognizance 2014");
		alarmIntent.putExtra("note", event_name+" "+dept_name+" is about to start !");
		
		PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), HELLO_ID,
				alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
				//VERY IMPORTANT TO SET FLAG_UPDATE_CURRENT... this will send correct extra's informations to 
				//AlarmReceiver Class
								// Get the AlarmManager service
				 
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);	
	}
	
public void setEventAlarm(String event_name){
		
		Calendar cal = Calendar.getInstance();
		// Add code to set alarm
		
		int hr = myDbHelper.getStartTime(event_name)/100;
		int min = myDbHelper.getStartTime(event_name)%100;
		cal.set(Calendar.MONTH , 3);
		cal.set(Calendar.YEAR , 2014);
		cal.set(Calendar.DAY_OF_MONTH , myDbHelper.
				getEventDay(event_name)+20);
		if(min>=30){
		cal.set(Calendar.HOUR_OF_DAY, hr);
		cal.set(Calendar.MINUTE , 00);
		}
		else{
			cal.set(Calendar.HOUR_OF_DAY, hr-1);
			cal.set(Calendar.MINUTE , 30);
		}
		
		Intent alarmIntent = new Intent(getApplicationContext() , AlarmReceiver.class);
		alarmIntent.putExtra("title", "Cognizance 2014");
		alarmIntent.putExtra("note", event_name+" is about to start !");
		
		PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), HELLO_ID,
				alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
				//VERY IMPORTANT TO SET FLAG_UPDATE_CURRENT... this will send correct extra's informations to 
				//AlarmReceiver Class
								// Get the AlarmManager service
				 
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);	
	}
	
}