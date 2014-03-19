package in.co.sdslabs.mdg.splash;

import java.io.IOException;
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
		try {
			is = getResources().getAssets().open("splash.gif");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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