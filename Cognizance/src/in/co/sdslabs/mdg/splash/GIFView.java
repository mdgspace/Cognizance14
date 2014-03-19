package in.co.sdslabs.mdg.splash;

import in.co.sdslabs.cognizance.R;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.view.View;

public class GIFView extends View {
	private Movie movie;
	private InputStream is;
	private long moviestart;

	public GIFView(Context context) {
		super(context);
		is = getResources().openRawResource(R.drawable.splash);
		movie = Movie.decodeStream(is);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long now = android.os.SystemClock.uptimeMillis();

		if (moviestart == 0)
			moviestart = now;

		int relTime = (int) ((now - moviestart) % movie.duration());
		movie.setTime(relTime);
		movie.draw(canvas, 0, 0);
		this.invalidate();
	}

}