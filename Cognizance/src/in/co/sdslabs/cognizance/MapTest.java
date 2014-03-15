package in.co.sdslabs.cognizance;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class MapTest extends Activity {

	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// bundle for deciding how the map will open normally or zoomed to a
		// particular state
		Bundle mapParams = new Bundle();
		mapParams.putInt("mode", 0); // mode = 0 for normal and mode = 1 for
										// zoomed

		Intent i = new Intent(this, in.co.sdslabs.mdg.map.CampusMap.class);
		i.putExtras(mapParams);
		finish();
		startActivity(i);

		// getPathFromPresentLocation();
	}

	private void getPathFromPresentLocation(double destLat, double destLong) {
		// TODO Auto-generated method stub
		// create class object
		gps = new GPSTracker(this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			// \n is for new line
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
			onlineMap(latitude, longitude, destLat, destLong);

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
	}

	private void showZoomedMap(String place) {
		// TODO Auto-generated method stub

		DatabaseHelper myDbHelper = new DatabaseHelper(this);
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

		Intent i = new Intent(this, in.co.sdslabs.mdg.map.CampusMap.class);
		i.putExtras(mapParams);
		finish();
		startActivity(i);
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
		finish();
		startActivity(intent1);
	}

}
