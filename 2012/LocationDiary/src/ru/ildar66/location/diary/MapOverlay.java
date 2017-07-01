package ru.ildar66.location.diary;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapOverlay extends Overlay {

	private List<DiaryRow> root = null;
	private final int mRadius = 5;

	// Setup the paint:
	Paint paint = new Paint();
	Paint backPaint = new Paint();
	{
		paint.setARGB(250, 255, 255, 255);
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);

		backPaint.setARGB(175, 255, 50, 50);
		backPaint.setAntiAlias(true);
		backPaint.setStrokeWidth(3);
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		return false;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();

		if (shadow == false && root != null && root.size() > 0) {
			drawMarker(canvas, projection);
			if (root.size() > 1) {
				drawPath(canvas, projection);
			}
		}
		super.draw(canvas, mapView, shadow);
	}

	private void drawPath(Canvas canvas, Projection projection) {
		Point last = null;
		for (DiaryRow row : root) {
			GeoPoint geoPoint = new GeoPoint(row.geoLat, row.geoLng);
			Point point = new Point();
			projection.toPixels(geoPoint, point);
			if (last == null) {
				last = point;
			} else {
				canvas.drawLine(last.x, last.y, point.x, point.y, backPaint);
				last = point;
			}
		}
	}

	private void drawMarker(Canvas canvas, Projection projection) {
		DiaryRow location = root.get(root.size() - 1);
		// Get the current location
		GeoPoint geoPoint = new GeoPoint(location.geoLat, location.geoLng);

		// Convert the location to screen pixels
		Point point = new Point();
		projection.toPixels(geoPoint, point);

		RectF oval = new RectF(point.x - mRadius, point.y - mRadius, point.x + mRadius, point.y + mRadius);

		RectF backRect = new RectF(point.x + 2 + mRadius, point.y - 3 * mRadius, point.x + 65, point.y + mRadius);

		// Draw the marker
		canvas.drawOval(oval, paint);
		canvas.drawRoundRect(backRect, 5, 5, backPaint);
		canvas.drawText("Here I'm", point.x + 2 * mRadius, point.y, paint);
	}

	void setRoot(List<DiaryRow> pathList) {
		root = pathList;
	}
}
