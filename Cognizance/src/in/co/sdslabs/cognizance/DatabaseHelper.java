package in.co.sdslabs.cognizance;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/in.co.sdslabs.cognizance/databases/";
	private static String DB_NAME = "cognizance14.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private DatabaseHelper ourHelper;

	// fields for table 1
	public static final String KEY_ROWID_VENUE = "_id_venue";
	public static final String KEY_MINX = "_minX";
	public static final String KEY_MINY = "_minY";
	public static final String KEY_MAXX = "_maxX";
	public static final String KEY_MAXY = "_maxY";
	public static final String KEY_TOUCH_VENUE = "_touch_venue";
	public static final String KEY_VENUE = "_place_name";

	public static final String DATABASE_TABLE1 = "table_venue";
	public static final String DATABASE_TABLE2 = "table_place";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist)
			return;
		else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	public DatabaseHelper getInstance(Context context) {
		if (ourHelper == null) {
			ourHelper = new DatabaseHelper(context);
		}
		return this;
	}

	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}
		if (checkDB != null)
			checkDB.close();
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public ArrayList<String> getCategory() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM table_category_details",
				null);
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("category")));
			}
		}
		cursor.close();
		return data;
	}

	public String getCategoryDescription(String category_name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_category_details WHERE category='"
						+ category_name + "'", null);
		String data = null;
		if (cursor != null) {
			cursor.moveToFirst();
		}
		data = cursor.getString(cursor.getColumnIndex("category_description"));
		cursor.close();
		return data;
	}

	public ArrayList<String> getEventName(String category_name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE category='"
						+ category_name + "'", null);
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("event_name")));
			}
		}
		cursor.close();
		return data;
	}

	public ArrayList<String> getEventoneLiner(String category_name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE category='"
						+ category_name + "'", null);
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("one_liner")));
			}
		}
		cursor.close();
		return data;
	}

	public String getEventDescription(String eventname) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='"
						+ eventname + "'", null);
		String data = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				cursor.moveToFirst();
			}
			data = cursor.getString(cursor.getColumnIndex("description"));
		}
		cursor.close();
		return data;
	}

	public String getEventOneLiner(String eventname) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='"
						+ eventname + "'", null);
		String data = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				cursor.moveToFirst();
			}
			data = cursor.getString(cursor.getColumnIndex("one_liner"));
		}
		cursor.close();
		return data;
	}

	public ArrayList<String> getEventNamex(int day) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		if (day == 1) {
			cursor = db
					.rawQuery(
							"SELECT * FROM table_event_details WHERE day= 1 OR day = 12 OR day = 123 ORDER BY start_time",
							null);
		} else if (day == 2) {
			cursor = db
					.rawQuery(
							"SELECT * FROM table_event_details WHERE day= 2 OR day = 23 OR day = 123 ORDER BY start_time",
							null);
		} else {
			cursor = db
					.rawQuery(
							"SELECT * FROM table_event_details WHERE day= 3 OR day = 23 OR day = 123 ORDER BY start_time",
							null);
		}
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("event_name")));
			}
		}
		cursor.close();
		return data;
	}

	public ArrayList<String> getEventoneLinerx(int day) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		if (day == 1) {
			cursor = db
					.rawQuery(
							"SELECT * FROM table_event_details WHERE day= 1 OR day = 12 OR day = 123 ORDER BY start_time",
							null);
		} else if (day == 2) {
			cursor = db
					.rawQuery(
							"SELECT * FROM table_event_details WHERE day= 2 OR day = 23 OR day = 123 ORDER BY start_time",
							null);
		} else {
			cursor = db
					.rawQuery(
							"SELECT * FROM table_event_details WHERE day= 3 OR day = 23 OR day = 123 ORDER BY start_time",
							null);
		}
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("one_liner")));
			}
		}
		cursor.close();
		return data;
	}

	// public String getEventDescriptionx(String eventname, int day) {
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor cursor = db.rawQuery(
	// "SELECT * FROM table_event_details WHERE event_name='"
	// + eventname + "'AND day= '" + day + "'", null);
	// String data = null;
	// if (cursor != null) {
	// if (cursor.moveToNext()) {
	// cursor.moveToFirst();
	// }
	// data = cursor.getString(cursor.getColumnIndex("description"));
	// }
	// cursor.close();
	// return data;
	// }

	public String searchEntryForVenue(String x, String y) throws SQLException {
		// TODO Auto-generated method stub
		myDataBase = this.getReadableDatabase();
		String[] columns = new String[] { KEY_ROWID_VENUE, KEY_MINX, KEY_MINY,
				KEY_MAXX, KEY_MAXY, KEY_TOUCH_VENUE };

		int ix = Integer.parseInt(x) * 2;
		int iy = Integer.parseInt(y) * 2;

		Cursor c = myDataBase.query(DATABASE_TABLE1, columns, KEY_MINX + "<="
				+ ix + " AND " + KEY_MINY + "<=" + iy + " AND " + KEY_MAXX
				+ ">=" + ix + " AND " + KEY_MAXY + ">=" + iy, null, null, null,
				null);
		try {
			if (c != null) {
				c.moveToFirst();
				String venue = c.getString(5);
				c.close();
				return venue;
			}
		} catch (CursorIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			c.close();
			return null;
		}
		return null;
	}

	public PointF searchPlaceForCoordinates(String selection) {
		// TODO Auto-generated method stub
		myDataBase = this.getReadableDatabase();
		String[] columns = new String[] { KEY_MINX, KEY_MINY, KEY_MAXX,
				KEY_MAXY, KEY_TOUCH_VENUE };
		PointF coor = new PointF();

		Cursor c = myDataBase.query(DATABASE_TABLE1, columns, KEY_TOUCH_VENUE
				+ "==\"" + selection + "\"", null, null, null, null);

		int iMinX = c.getColumnIndex(KEY_MINX);
		int iMinY = c.getColumnIndex(KEY_MINY);
		int iMaxX = c.getColumnIndex(KEY_MAXX);
		int iMaxY = c.getColumnIndex(KEY_MAXY);

		try {
			if (c != null) {
				c.moveToFirst();
				coor.x = (Integer.parseInt(c.getString(iMinX)) + Integer
						.parseInt(c.getString(iMaxX))) / 4;
				coor.y = (Integer.parseInt(c.getString(iMinY)) + Integer
						.parseInt(c.getString(iMaxY))) / 4;
				c.close();
			}
		} catch (CursorIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return coor;
	}

	public String getVenueDisplay(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);
		String data = null;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		data = cursor.getString(cursor.getColumnIndex("venue_display"));
		cursor.close();
		return data;
	}

	public String getVenueMap(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);
		String data = null;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		data = cursor.getString(cursor.getColumnIndex("venue_map"));
		cursor.close();
		return data;
	}

	public int getID(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);
		String category;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		category = cursor.getString(cursor.getColumnIndex("category"));
		cursor.close();

		cursor = db.rawQuery(
				"SELECT * FROM table_category_details WHERE category= '"
						+ category + "'", null);
		int id;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		id = cursor.getInt(cursor.getColumnIndex("category"));
		cursor.close();
		return (id - 1);
	}

	public ArrayList<String> getcontactsname() {
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				list.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
			}
			// if(cursor.moveToNext()) cursor.moveToFirst();
		}
		cursor.close();
		return list;
	}

	public ArrayList<String> getcontactsnumber() {
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				list.add(cursor.getString(cursor
						.getColumnIndexOrThrow("number")));
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<String> getcontactspost() {
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				list.add(cursor.getString(cursor
						.getColumnIndexOrThrow("position")));
			}
			// if(cursor.moveToNext()) cursor.moveToFirst();
		}
		cursor.close();
		return list;
	}

	public ArrayList<String> getcontactsemail() {
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				list.add(cursor.getString(cursor
						.getColumnIndexOrThrow("email_id")));
			}
			cursor.close();
		}
		return list;
	}

	public int getImageX(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);

		int imageX;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		imageX = cursor.getInt(cursor.getColumnIndex("image_x"));
		cursor.close();
		return (imageX);
	}

	public int getImageY(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);

		int imageY;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		imageY = cursor.getInt(cursor.getColumnIndex("image_y"));
		cursor.close();
		return (imageY);
	}

	public void markAsFavourite(String event) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"UPDATE table_event_details SET isFavourite = 1 WHERE event_name='"
						+ event + "'", null);

		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		cursor.close();
	}

	public void unmarkAsFavourite(String event) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"UPDATE table_event_details SET isFavourite = 0 WHERE event_name='"
						+ event + "'", null);

		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		cursor.close();
	}

	public boolean isFavourite(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);

		int flag;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		flag = cursor.getInt(cursor.getColumnIndex("isFavourite"));
		cursor.close();
		if (flag == 1) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> getFavouritesName() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE isFavourite= 1", null);
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("event_name")));
			}
		}
		cursor.close();
		return data;
	}

	public String getEventDate(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);

		int day;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		day = cursor.getInt(cursor.getColumnIndex("day"));
		cursor.close();

		switch (day) {
		case 1:
			return "21st March 2014";
		case 2:
			return "22nd March 2014";
		case 3:
			return "23rd March 2014";
		case 12:
			return "21-22 March 2014";
		case 123:
			return "21-23 March 2014";
		case 23:
			return "22-23 March 2014";
		default:
			return null;
		}
	}

	public String getEventTime(String string) {
		return null;
	}

	public int getStartTime(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);

		int startTime;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		startTime = cursor.getInt(cursor.getColumnIndex("start_time"));
		cursor.close();
		return (startTime);
	}

	public int getEndTime(String event) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE event_name='" + event
						+ "'", null);

		int endTime;
		if (cursor != null) {
			if (cursor.moveToNext())
				cursor.moveToFirst();
		}
		endTime = cursor.getInt(cursor.getColumnIndex("end_time"));
		cursor.close();
		return (endTime);
	}

	public PointF searchPlaceForLatLong(String selection) {
		// TODO Auto-generated method stub

		myDataBase = this.getReadableDatabase();
		String[] columns = new String[] { "_lat", "_lng" };
		PointF coor = new PointF();

		Cursor c = myDataBase.query(DATABASE_TABLE2, columns, KEY_VENUE
				+ "==\"" + selection + "\"", null, null, null, null);

		int iLat = c.getColumnIndex("_lat");
		int iLong = c.getColumnIndex("_lng");

		try {
			if (c != null) {
				c.moveToFirst();
				coor.x = (Float.parseFloat(c.getString(iLat)));
				coor.y = (Float.parseFloat(c.getString(iLong)));
				c.close();
			}
		} catch (CursorIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return coor;
	}
	
	/** Functions for Departmental Events */
	
	public ArrayList<String> getDepartments() {
		
		/** Retrieves unique department names from the database*/
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT DISTINCT dept_name FROM table_departments",
				null);
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("dept_name")));
			}
		}
		cursor.close();
		return data;
	}
	
}