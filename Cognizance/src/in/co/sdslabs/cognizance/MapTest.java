package in.co.sdslabs.cognizance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MapTest extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// bundle for deciding how the map will open normally or zoomed to a particular state 
		Bundle mapParams = new Bundle();
		mapParams.putInt("mode", 1);    // mode = 0 for normal and mode = 1 for zoomed
		mapParams.putFloat("X", (float) 390.0);
		mapParams.putFloat("Y", (float) 95.0);
		
		Intent i = new Intent(this, in.co.sdslabs.mdg.map.CampusMap.class);
		i.putExtras(mapParams);
		startActivity(i);
	}

}
