package ru.ildar66.location.diary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	DatePicker datePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		datePicker = (DatePicker) findViewById(R.id.datePicker);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.pref_ID:
			startActivity(new Intent(this, AppPreferenceActivity.class));
			return true;
		case R.id.itemStartDiaryService:
			startService(new Intent(this, ServiceLocation.class));
			return true;
		case R.id.itemStopDiaryService:
			stopService(new Intent(this, ServiceLocation.class));
			return true;
		}
		return false;
	}

	public void onClick(View view) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, datePicker.getYear());
		calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		calendar.set(Calendar.MONTH, datePicker.getMonth());

		Date from = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth() + 1);
		Date to = calendar.getTime();

		ArrayList<DiaryRow> path = ((DiaryApp)getApplication()).createPathOnPeriod(from, to);
		if (path != null) {
			showPathOnGoogleMap(path);
		} else {
			alert("NO DATE ON PERIOD \n from:" + from + "\n to: " + to);
		}
	}

	void alert(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}

	private void showPathOnGoogleMap(ArrayList<DiaryRow> path) {
		Intent intent = new Intent("ru.ildar66.location.diary.GoogleMapActivity");
		Bundle extras = new Bundle();
		extras.putSerializable(GoogleMapActivity.PATH_KEY, path);
		intent.putExtras(extras);
		startActivity(intent);
	}

}
