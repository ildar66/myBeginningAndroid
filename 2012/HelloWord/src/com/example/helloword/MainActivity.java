package com.example.helloword;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		usinContentProviderFromAnotherProgect();
	}

	private void usinContentProviderFromAnotherProgect() {
		// using Content provider from net.learn2develop.ContentProviders.* project:
		ContentValues editedValues = new ContentValues();
		editedValues.put("title", "Android Tips and Tricks from Hello word");
		getContentResolver().update(Uri.parse("content://net.learn2develop.provider.Books/books/1"), editedValues, null, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
