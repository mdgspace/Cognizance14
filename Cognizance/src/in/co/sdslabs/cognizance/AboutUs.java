package in.co.sdslabs.cognizance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AboutUs extends ActionBarActivity implements OnClickListener{

	public AboutUs() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		ImageView fb = (ImageView)findViewById(R.id.fb);
		ImageView git = (ImageView)findViewById(R.id.git);
		ImageView play = (ImageView)findViewById(R.id.play);
		fb.setOnClickListener(this);
		git.setOnClickListener(this);
		play.setOnClickListener(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.fb :
			Intent fbIntent =
	          new Intent("android.intent.action.VIEW",
	            Uri.parse("https://www.facebook.com/mdgiitr"));
	          startActivity(fbIntent);
			break;
		case R.id.git :
			Intent gitIntent =
	          new Intent("android.intent.action.VIEW",
	            Uri.parse("https://github.com/sdsmdg"));
	          startActivity(gitIntent);			
			break;
		case R.id.play :
			Intent playIntent =
	          new Intent("android.intent.action.VIEW",
	            Uri.parse("https://play.google.com/store/apps/developer?id=SDSLabs"));
	          startActivity(playIntent);	
			break;
		}
	}

}
