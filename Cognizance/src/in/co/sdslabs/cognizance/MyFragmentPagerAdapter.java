package in.co.sdslabs.cognizance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 3;

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		Bundle data = new Bundle();
		switch (arg0) {

		/** tab1 is selected */
		case 0:
			EventByDay day1 = new EventByDay();
			data.putInt("day", 1);
			day1.setArguments(data);
			return day1;

			/** tab2 is selected */
		case 1:
			EventByDay day2 = new EventByDay();
			data.putInt("day", 2);
			day2.setArguments(data);
			return day2;
		case 2:
			EventByDay day3 = new EventByDay();
			data.putInt("day", 3);
			day3.setArguments(data);
			return day3;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

}
