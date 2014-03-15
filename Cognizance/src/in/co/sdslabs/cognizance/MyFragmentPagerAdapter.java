package in.co.sdslabs.cognizance;

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
		//Bundle data = new Bundle();
		switch (arg0) {

		/** tab1 is selected */
		case 0:
			Day1 day1 = new Day1();
			return day1;
			/** tab2 is selected */
		case 1:
			Day2 day2 = new Day2();
			return day2;
		case 2:
			Day3 day3 = new Day3();
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
