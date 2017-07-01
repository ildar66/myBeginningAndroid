package ru.ildar66.location.diary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class GoogleMapActivity extends MapActivity {
	private static final String TAG = "GoogleMapActivity";
	private static final int ZOOM = 18;
	public static final String PATH_KEY = "path";

	private MapView mapView;
	private MapController mapController;
	MapOverlay positionOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		initMap();
		addOverlayToMap();

		@SuppressWarnings("unchecked")
		ArrayList<DiaryRow> pathList = (ArrayList<DiaryRow>) getIntent().getExtras().get(PATH_KEY);
		updateWithNewLocation(pathList);

	}

	private void updateWithNewLocation(List<DiaryRow> pathList) {
		if (pathList != null && pathList.size() > 0) {
			DiaryRow last = pathList.get(pathList.size() - 1);
			// Update my location marker and pathList
			positionOverlay.setRoot(pathList);

			// Update the map location.
			GeoPoint point = new GeoPoint(last.geoLat, last.geoLng);
			mapController.animateTo(point);
			
			//Update Title:
			setTitle("Yor Root on " + new Date(last.time));
		}
	}

	private void initMap() {
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		// mapView.setSatellite(true);
		// mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(ZOOM);
	}

	private void addOverlayToMap() {
		positionOverlay = new MapOverlay();
		List<Overlay> overlays = mapView.getOverlays();
		overlays.add(positionOverlay);
	}

	@Override
	/** --This method must return true if your Activity is displaying driving directions.-- */
	protected boolean isRouteDisplayed() {
		return false;
	}

}
