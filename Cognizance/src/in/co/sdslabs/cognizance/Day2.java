package in.co.sdslabs.cognizance;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Day2 extends ListFragment{
	
	public Day2() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		Log.v("Day" ,"2");
		return super.onCreateView(inflater, container, savedInstanceState);
	}


}
