package in.co.sdslabs.cognizance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ActionBarActivity {

	int mPosition = -1;
	String mTitle = "";

	// Array of strings storing Event Category names
	String[] mEventCategories;

	/** Uncoment this after adding drawables for respective event categories **/
	// // Array of integers points to images stored in /res/drawable-ldpi/
	// int[] mImages = new int[]{
	// R.drawable.india,
	// R.drawable.pakistan,
	// R.drawable.srilanka,
	// R.drawable.china,
	// R.drawable.bangladesh,
	// R.drawable.nepal,
	// R.drawable.afghanistan,
	// R.drawable.nkorea,
	// R.drawable.skorea,
	// R.drawable.japan
	// };

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawer;
	private List<HashMap<String, String>> mList;
	private SimpleAdapter mAdapter;
	final private String EVENTCATEGORY = "eventcategory";
	final private String IMAGE = "image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Getting an array of country names
		mEventCategories = getResources().getStringArray(
				R.array.eventCategories);

		// Title of the activity
		mTitle = (String) getTitle();

		// Getting a reference to the drawer listview
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting a reference to the sidebar drawer ( Title + ListView )
		mDrawer = (LinearLayout) findViewById(R.id.drawer);

		// Each row in the list stores country name, count and flag
		mList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 10; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(EVENTCATEGORY, mEventCategories[i]);
			// TODO : uncomment this line after adding eventcategory images
			// hm.put(IMAGE, Integer.toString(mImages[i]) );
			mList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { IMAGE, EVENTCATEGORY };

		// Ids of views in listview_layout
		int[] to = { R.id.image, R.id.eventcategory };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item
		mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from,
				to);

		// Getting reference to DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Creating a ToggleButton for NavigationDrawer with drawer event
		// listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				highlightSelectedEventCategory();
				getSupportActionBar().setTitle("Cognizance 2014");
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle("Select a Category");
				supportInvalidateOptionsMenu();
			}
		};

		// Setting event listener for the drawer
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// ItemClick event handler for the drawer items
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// Show fragment for countries : 0 to 4
				showFragment(position);

				// Closing the drawer
				mDrawerLayout.closeDrawer(mDrawer);
			}
		});

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// Setting the adapter to the listView
		mDrawerList.setAdapter(mAdapter);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void showFragment(int position){
		 
		 //Currently selected country
		 mTitle = mEventCategories[position];
		 
		// Creating a fragment object
		 EventCategoryFragment eFragment = new EventCategoryFragment();
		 
		// Creating a Bundle object
		 Bundle data = new Bundle();
		 
		// Setting the index of the currently selected item of mDrawerList
		 data.putInt("position", position);
		 
		// Setting the position to the fragment
		 eFragment.setArguments(data);
		 
		// Getting reference to the FragmentManager
		 FragmentManager fragmentManager = getSupportFragmentManager();
		 
		// Creating a fragment transaction
		 FragmentTransaction ft = fragmentManager.beginTransaction();
		 
		// Adding a fragment to the fragment transaction
		 ft.replace(R.id.content_frame, eFragment);
		 
		// Committing the transaction
		 ft.commit();
		 }

	 // Highlight the selected country : 0 to 4
	 public void highlightSelectedEventCategory(){
	 
	 mDrawerList.setItemChecked(mPosition, true);
	 if(mPosition!=-1)
	 getSupportActionBar().setTitle(mEventCategories[mPosition]);
	 }
	
}
