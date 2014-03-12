package in.co.sdslabs.cognizance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

	// Array of integers points to images stored in /res/drawable-ldpi/
	int[] mImages = new int[] { R.drawable.eventcat_theme,
			R.drawable.eventcat_robotics, R.drawable.eventcat_competetions,
			R.drawable.eventcat_literario, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.eventcat_csr, R.drawable.eventcat_efiesta,
			R.drawable.eventcat_braintainment, R.drawable.eventcat_department,
			R.drawable.ic_launcher };

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawer;
	private List<HashMap<String, String>> mList;
	private SimpleAdapter mAdapter;
	final private String EVENTCATEGORY = "eventcategory";
	final private String IMAGE = "image";
	private HomeFragment hFragment;
	FragmentManager fragmentManager;
	FragmentTransaction ft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addHomeFragment();

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
		for (int i = 0; i < mEventCategories.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(EVENTCATEGORY, mEventCategories[i]);
			hm.put(IMAGE, Integer.toString(mImages[i]));
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
				//getSupportActionBar().setTitle("Cognizance 2014");
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

				// Show fragment for eventCategories
				showFragment(position);

				// Closing the drawer
				mDrawerLayout.closeDrawer(mDrawer);
			}
		});

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// Changing the background of the color drawable
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.rgb(234, 234, 234)));
		// Setting the adapter to the listView
		mDrawerList.setAdapter(mAdapter);
	}

	private void addHomeFragment() {
		// TODO Auto-generated method stub

		// initialize the HomeFragment
		hFragment = new HomeFragment();

		// Getting reference to the FragmentManager
		fragmentManager = getSupportFragmentManager();

		// Creating a fragment transaction
		ft = fragmentManager.beginTransaction();

		// Adding a fragment to the fragment transaction
		ft.replace(R.id.content_frame, hFragment);

		// Committing the transaction
		ft.commit();
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

	public void showFragment(int position) {

		// Currently selected eventCategory
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
		fragmentManager = getSupportFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();

		// Adding a fragment to the fragment transaction
		ft.replace(R.id.content_frame, eFragment);
		ft.addToBackStack(null);

		// Committing the transaction
		ft.commit();
	}

	// Highlight the selected eventCategory
	public void highlightSelectedEventCategory() {

		mDrawerList.setItemChecked(mPosition, true);
		if (mPosition != -1)
			getSupportActionBar().setTitle(mEventCategories[mPosition]);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		fragmentManager.popBackStack();
	}
}
