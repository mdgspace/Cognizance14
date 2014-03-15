package in.co.sdslabs.cognizance;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;

public class MapTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String place = "";
		showZoomedMap(place);
		// // bundle for deciding how the map will open normally or zoomed to a
		// particular state
		Bundle mapParams = new Bundle();
		mapParams.putInt("mode", 1); // mode = 0 for normal and mode = 1 for
										// zoomed
		mapParams.putFloat("X", (float) 390.0);
		mapParams.putFloat("Y", (float) 95.0);

		Intent i = new Intent(this, in.co.sdslabs.mdg.map.CampusMap.class);
		i.putExtras(mapParams);
		startActivity(i);
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
		mapParams.putInt("mode", 1); // mode = 0 for normal and mode = 1 for zoomed
		mapParams.putFloat("X", (float) coord.x);
		mapParams.putFloat("Y", (float) coord.y);

		Intent i = new Intent(this, in.co.sdslabs.mdg.map.CampusMap.class);
		i.putExtras(mapParams);
		finish();
		startActivity(i);
	}

	private void onlineMap() {
		// TODO Auto-generated method stub
		String uri = "http://maps.google.com/maps?saddr=" + "29.865866" + ","
				+ "77.896316" + "&daddr=" + "29.867294" + "," + "77.901182";
		Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse(uri));
		intent1.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent1);
	}

}
