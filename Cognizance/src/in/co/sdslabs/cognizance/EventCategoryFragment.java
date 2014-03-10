package in.co.sdslabs.cognizance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventCategoryFragment extends Fragment{

	public EventCategoryFragment() {	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Retrieving the currently selected item number
        int position = getArguments().getInt("position");
 
        // List of rivers
        String[] countries = getResources().getStringArray(R.array.eventCategories);
 
        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.eventfragment_layout, container, false);
 
        // Getting reference to the TextView of the Fragment
        TextView tv = (TextView) v.findViewById(R.id.tv_content);
 
        // Setting currently selected river name in the TextView
        tv.setText(countries[position]);
		return v;
	}
	
	
	
	

}
