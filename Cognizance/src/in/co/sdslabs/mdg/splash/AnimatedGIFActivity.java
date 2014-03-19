package in.co.sdslabs.mdg.splash;

import in.co.sdslabs.cognizance.MainNavDrawerActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AnimatedGIFActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GIFView gifView = new GIFView(this);
		setContentView(gifView);

		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent openStartingPoint = new Intent(
							AnimatedGIFActivity.this,
							MainNavDrawerActivity.class);
					startActivity(openStartingPoint);
					finish();

				}
			}
		};
		timer.start();
	}
}