package in.co.sdslabs.cognizance;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/in.co.sdslabs.cognizance/databases/";
	private static String DB_NAME = "cognizance14.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;

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
}
