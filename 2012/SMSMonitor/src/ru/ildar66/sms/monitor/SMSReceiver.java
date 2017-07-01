package ru.ildar66.sms.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	private static final String TAG = "SMSReceiver";

	private static final String KEY_REQUEST_LOCATION_MSG = "WHERE ARE YOU?";
	private static final String KEY_RESPONS_LOCATION_MSG = "MY LOCATION IS:";

	private static final int MIN_TIME = 60 * 1000; // milliseconds
	private static final int MIN_DISTANCE = 100; // meters

	LocationManager lm = null;
	SMS sms = null;

	private Criteria bestCriteria = new Criteria();
	{
		bestCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestCriteria.setPowerRequirement(Criteria.POWER_LOW);
		bestCriteria.setAltitudeRequired(false);
		bestCriteria.setBearingRequired(false);
		bestCriteria.setSpeedRequired(false);
		// bestCriteria.setCostAllowed(true);
	}

	private LocationListener listener = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onLocationChanged(Location location) {
			sendSMS(sms.from, location);
			lm.removeUpdates(this);
		}

		private void sendSMS(String phoneNumber, Location lc) {
			SmsManager sm = SmsManager.getDefault();
			String info = (lc != null) ? lc.getLongitude() + ":" + lc.getLatitude() : "NONE";
			sm.sendTextMessage(phoneNumber, null, KEY_RESPONS_LOCATION_MSG + info, null, null);
			Log.d(TAG, "send SMS to " + sms.from + "\n info: " + KEY_RESPONS_LOCATION_MSG + info);
		}
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			sms = SMS.create(bundle);
			if (sms.msg.contains(KEY_REQUEST_LOCATION_MSG)) {
				lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
				String bestProvider = lm.getBestProvider(bestCriteria, true);
				lm.requestLocationUpdates(bestProvider, MIN_TIME, MIN_DISTANCE, listener);
				abortBroadcast();
			} else if (sms.msg.contains(KEY_RESPONS_LOCATION_MSG)) {
				launchMap(context, sms);
			}
		}
	}

	/** ---launch the MapActivity--- */
	private void launchMap(Context context, SMS sms) {
		String[] arrMsg = sms.msg.split(":");
		if (arrMsg.length > 2) {
			Intent mapActivityIntent = new Intent(context, GoogleMapActivity.class);
			Bundle extras = new Bundle();
			extras.putString("Longitude", arrMsg[1]);
			extras.putString("Latitude", arrMsg[2]);
			mapActivityIntent.putExtras(extras);
			mapActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mapActivityIntent);
		}
	}
}
