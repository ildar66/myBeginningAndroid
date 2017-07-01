package ru.ildar66.location.diary;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDBAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_TIME = "time";
	private static final String TAG = "diaryDBAdapter";

	private static final String DATABASE_NAME = "LocationDiaryDB";
	private static final String DATABASE_TABLE = "diary";
	private static final int DATABASE_VERSION = 3;

	private static final String DATABASE_CREATE = "create table diary (_id integer primary key autoincrement, "
			+ "latitude double not null, longitude double not null, time long not null);";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DiaryDBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	// ---opens the database---
	public DiaryDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a row into the database---
	public long insertRow(double lat, double lng, long time) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_LATITUDE, lat);
		initialValues.put(KEY_LONGITUDE, lng);
		initialValues.put(KEY_TIME, time);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular row---
	public boolean deleteRow(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// ---retrieves all the rows---
	public Cursor getAllRows() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_LATITUDE, KEY_LONGITUDE, KEY_TIME }, null, null, null, null, null);
	}

	// ---retrieves all the rows---
	public Cursor getRowsOnPeriod(Date from, Date to) {
		String[] result_columns = new String[] { KEY_ROWID, KEY_LATITUDE, KEY_LONGITUDE, KEY_TIME };
		String where = KEY_TIME + " BETWEEN ? AND ? ";
		String[] timePeriod = { "" + from.getTime(), "" + to.getTime() };
		return db.query(DATABASE_TABLE, result_columns, where, timePeriod, null, null, KEY_TIME);// + " DESC"
	}

	// ---retrieves a particular row---
	public Cursor getRow(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_LATITUDE, KEY_LONGITUDE, KEY_TIME }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a row---
	public boolean updateRow(long rowId, double lat, double lng, long time) {
		ContentValues args = new ContentValues();
		args.put(KEY_LATITUDE, lat);
		args.put(KEY_LONGITUDE, lng);
		args.put(KEY_TIME, time);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
