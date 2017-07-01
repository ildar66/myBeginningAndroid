package ru.ildar66.location.diary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class ServiceLocation extends Service implements OnSharedPreferenceChangeListener {

	private static final int MIN_TIME = 60 * 1000; // milliseconds
	private static final int MIN_DISTANCE = 100; // meters

	private LocationManager lm;
	private LocationListener locationListener;
	SharedPreferences appPrefs;

	public class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				alert("Location changed: \n Lat: " + location.getLatitude() + " \n Lng: " + location.getLongitude());
				((DiaryApp)getApplication()).insertLocation(location);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "ServiceLocation Started", Toast.LENGTH_LONG).show();

		// ---use the LocationManager class to obtain locations data---
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();

		appPrefs = getSharedPreferences("appPreferences", MODE_PRIVATE);
		appPrefs.registerOnSharedPreferenceChangeListener(this);
		
		requestLocationUpdate();
		// We want this service to continue running until it is explicitly stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(locationListener);
		Toast.makeText(this, "ServiceLocation Destroyed", Toast.LENGTH_LONG).show();
	}

	private void requestLocationUpdate() {
		int minDistance = MIN_DISTANCE;
		try {
			String minDistStr = appPrefs.getString("minDistance", "" + MIN_DISTANCE);
			minDistance = Integer.valueOf(minDistStr);
		} catch (Exception e) {
			alert("Wrong number for min.Distance");
		}
		int minTime = Integer.valueOf(appPrefs.getString("minTime", "" + MIN_TIME));
		alert("request LocationUpdate: \n min.Time=" + minTime / 1000 + "sec." + "\n min.Distance=" + minDistance);

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
	}

	void alert(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		lm.removeUpdates(locationListener);
		requestLocationUpdate();
	}

}
