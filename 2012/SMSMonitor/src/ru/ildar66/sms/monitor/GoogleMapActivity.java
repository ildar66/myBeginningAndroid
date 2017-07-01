package ru.ildar66.sms.monitor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class GoogleMapActivity extends MapActivity {
	private static final String TAG = "GoogleMapActivity";
	private static final int ZOOM = 18;

	MapView mapView;
	MapController mc;
	GeoPoint p;

	class MapOverlay extends com.google.android.maps.Overlay {
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			super.draw(canvas, mapView, shadow);
			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);
			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pushpin);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			// --- show Address in Title ---
			//showAddress(p);
			return true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);

		// mapView.setSatellite(true);
		// mapView.setStreetView(true);

		mc = mapView.getController();
		mc.setZoom(ZOOM);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.d(TAG, "onNewIntent");
		Bundle extras = intent.getExtras();
		if (extras != null) {
			p = getLocation(extras);
			if (p != null) {
				mc.animateTo(p);
				addLocationMarker();
				mapView.invalidate();
				showAddressInTitle(p);
			} else {
				clearLocationMarker();
				alert(this, "Don't may resolve Location from SMS");
			}
		}
		super.onNewIntent(intent);
	}

	/** --- display the message--- */
	private void alert(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/** get Location GeoPoint from Bundle --- */
	private GeoPoint getLocation(Bundle extras) {
		try {
			String latStr = extras.getString("Latitude");
			String lngStr = extras.getString("Longitude");
			double lat = Double.parseDouble(latStr);
			double lng = Double.parseDouble(lngStr);
			return new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		} catch (NumberFormatException e) {
			Log.e(TAG, "getLocation", e);
			return null;
		}
	}

	/** ---Add a location marker--- */
	private void addLocationMarker() {
		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
	}

	private void clearLocationMarker() {
		mapView.getOverlays().clear();
	}

	private void showAddressInTitle(GeoPoint p) {
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, 1);
			String address = "";
			if (addresses.size() > 0) {
				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
					address += addresses.get(0).getAddressLine(i) + "\n";
			} else {
				address = "ADDRESS NONE";
			}
			// Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
			setTitle(address);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
