package ru.ildar66.location.diary;

import java.util.ArrayList;
import java.util.Date;

import android.app.Application;
import android.database.Cursor;
import android.location.Location;

public class DiaryApp extends Application {
	DiaryDBAdapter diaryDB;

	@Override
	public void onCreate() {
		super.onCreate();
		diaryDB = new DiaryDBAdapter(this);
	}

	void insertLocation(Location location) {
		diaryDB.open();
		diaryDB.insertRow(location.getLatitude(), location.getLongitude(), location.getTime());
		diaryDB.close();
	}

	ArrayList<DiaryRow> createPathOnPeriod(Date from, Date to) {
		ArrayList<DiaryRow> list = null;
		diaryDB.open();
		Cursor c = diaryDB.getRowsOnPeriod(from, to);
		if (c.moveToFirst()) {
			list = new ArrayList<DiaryRow>();
			do {
				// displayRow(c);
				int latColInd = c.getColumnIndex(DiaryDBAdapter.KEY_LATITUDE);
				int lngColInd = c.getColumnIndex(DiaryDBAdapter.KEY_LONGITUDE);
				int timeColInd = c.getColumnIndex(DiaryDBAdapter.KEY_TIME);
				list.add(new DiaryRow(c.getDouble(latColInd), c.getDouble(lngColInd), c.getLong(timeColInd)));
			} while (c.moveToNext());
		}
		diaryDB.close();
		return list;
	}

}
