package in.co.sdslabs.cognizance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ContactFragment extends ListFragment {

	// private ListView mContactList;
	private List<HashMap<String, String>> contactList;
	private SimpleAdapter mAdapter;

	private String name;
	private String number = "";
	private String email;
	private String post;

	int position;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Creating view correspoding to the fragment
				View v = inflater.inflate(R.layout.contactfragment,
						container, false);
		
		
				
		return v;
		
	}

}
