package ru.ildar66.location.diary;

import java.io.Serializable;

public class DiaryRow implements Serializable {

	private static final long serialVersionUID = 1L;
	final int geoLat;
	final int geoLng;
	final long time;

	DiaryRow(double lat, double lng, long time) {
		this.geoLat = (int) (lat * 1E6);
		this.geoLng = (int) (lng * 1E6);
		this.time = time;
	}
}
