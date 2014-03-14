package in.co.sdslabs.cognizance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{


	final int PAGE_COUNT = 3;
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		Bundle data = new Bundle();
        switch(arg0){
 
            /** tab1 is selected */
            case 0:
             //   Fragment1 fragment1 = new Fragment1();
             //   return fragment1;
 
            /** tab2 is selected */
            case 1:
              //  Fragment2 fragment2 = new Fragment2();
              //  return fragment2;
        }
        return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

}
