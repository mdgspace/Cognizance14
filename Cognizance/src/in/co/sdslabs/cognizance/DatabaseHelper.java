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

	public static final String DATABASE_TABLE1 = "table_venue";

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
		data = cursor.getString(cursor.getColumnIndex("description"));
		cursor.close();
		return data;
	}

	public ArrayList<String> getEventName(String category_name, int Day) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE day='" + Day
						+ "' AND category='" + category_name + "'", null);
		ArrayList<String> data = new ArrayList<String>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				data.add(cursor.getString(cursor.getColumnIndex("event_name")));
			}
		}
		cursor.close();
		return data;
	}

	public ArrayList<String> getEventoneLiner(String category_name, int Day) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM table_event_details WHERE day='" + Day
						+ "' AND category='" + category_name + "'", null);
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
	
	public String searchEntryForVenue(String x, String y) throws SQLException {
		// TODO Auto-generated method stub
		myDataBase = ourHelper.getWritableDatabase();
		String[] columns = new String[] { KEY_ROWID_VENUE, KEY_MINX, KEY_MINY,
				KEY_MAXX, KEY_MAXY, KEY_TOUCH_VENUE };

		int ix = Integer.parseInt(x) * 2;
		int iy = Integer.parseInt(y) * 2;

		Cursor c = myDataBase.query(DATABASE_TABLE1, columns, KEY_MINX + "<=" + ix
				+ " AND " + KEY_MINY + "<=" + iy + " AND " + KEY_MAXX + ">="
				+ ix + " AND " + KEY_MAXY + ">=" + iy, null, null, null, null);
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
		myDataBase = ourHelper.getWritableDatabase();
		String[] columns = new String[] { KEY_MINX, KEY_MINY, KEY_MAXX,
				KEY_MAXY, KEY_TOUCH_VENUE };
		PointF coor = new PointF();

		Cursor c = myDataBase.query(DATABASE_TABLE1, columns, KEY_TOUCH_VENUE + "==\""
				+ selection + "\"", null, null, null, null);

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
}

