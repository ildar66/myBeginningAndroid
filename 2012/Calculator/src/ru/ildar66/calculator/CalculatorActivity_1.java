package ru.ildar66.calculator;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Shafigullin Ildar
 * 
 *         View for Calculator
 * 
 */
public class CalculatorActivity_1 extends Activity {
	private static final String SAVED_DISPLAY_KEY = "display_state";
	private static final String SAVED_HISTORY_KEY = "history_state";

	private TextView display = null;
	private TextView history = null;
	private Calculator_1 calc = new Calculator_1();

	/** Called when input digital-buttons is pushed */
	private View.OnClickListener insertClickListener = new View.OnClickListener() {
		public void onClick(View view) {
			String inputStr = ((Button) view).getText().toString();
			display.setText(calc.input(inputStr));
		}
	};

	/** Called when command-buttons is pushed */
	private View.OnClickListener commandClickListener = new View.OnClickListener() {
		public void onClick(View view) {
			String commandStr = ((Button) view).getText().toString();
			if (commandStr.equals(Calculator_1.EQUAL)) {
				history.setText(R.string._);
			} else {
				history.append(display.getText() + " " + commandStr + " ");
			}
			display.setText(calc.command(commandStr));
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();

		restoreUIState();
	}

	private void initUI() {
		setContentView(R.layout.activity_calculator);

		display = (TextView) findViewById(R.id.display);
		history = (TextView) findViewById(R.id.history);

		// Set the OnClickListener for the input digital-buttons.
		setOnClickListenerForInputButtons();

		// Set the OnClickListener for the command-buttons.
		setOnClickListenerForCommandButtons();

		// Set the OnClickListener for clear-button.
		Button btn = (Button) findViewById(R.id.clear);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				display.setText(calc.clear());
				history.setText(R.string._);
			}
		});

		// Set the OnClickListener for backSpace-button(C).
		btn = (Button) findViewById(R.id.backSpace);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				display.setText(calc.backSpace());
			}
		});
	}

	private void restoreUIState() {
		// ---load the SharedPreferences object---
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		// ---set the display to the previously saved values---
		if (prefs.contains(SAVED_DISPLAY_KEY)) {
			String displayStr = prefs.getString(SAVED_DISPLAY_KEY, "0");
			display.setText(calc.input(displayStr));
		}
		history.setText(prefs.getString(SAVED_HISTORY_KEY, " "));
	}

	private void setOnClickListenerForCommandButtons() {
		findViewById(R.id.division).setOnClickListener(commandClickListener);
		findViewById(R.id.multiply).setOnClickListener(commandClickListener);
		findViewById(R.id.minus).setOnClickListener(commandClickListener);
		findViewById(R.id.plus).setOnClickListener(commandClickListener);
		findViewById(R.id.result).setOnClickListener(commandClickListener);
	}

	private void setOnClickListenerForInputButtons() {
		findViewById(R.id.zero).setOnClickListener(insertClickListener);
		findViewById(R.id.one).setOnClickListener(insertClickListener);
		findViewById(R.id.two).setOnClickListener(insertClickListener);
		findViewById(R.id.three).setOnClickListener(insertClickListener);
		findViewById(R.id.four).setOnClickListener(insertClickListener);
		findViewById(R.id.five).setOnClickListener(insertClickListener);
		findViewById(R.id.six).setOnClickListener(insertClickListener);
		findViewById(R.id.seven).setOnClickListener(insertClickListener);
		findViewById(R.id.eight).setOnClickListener(insertClickListener);
		findViewById(R.id.nine).setOnClickListener(insertClickListener);
		findViewById(R.id.point).setOnClickListener(insertClickListener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);

		boolean handled = false;
		if ((event.getFlags() & KeyEvent.FLAG_SOFT_KEYBOARD) == 0) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_0:
				display.setText(calc.input("1"));
				break;
			case KeyEvent.KEYCODE_1:
				display.setText(calc.input("2"));
				handled = true;
				break;
			// Cases for each of the input and command keys
			}
		}
		return handled;
	}

	@Override
	protected void onStop() {
		saveUIState();
		super.onStop();
	}

	private void saveUIState() {
		// get the SharedPreferences object
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		// saves the result calculator
		editor.putString(SAVED_DISPLAY_KEY, display.getText().toString());
		editor.putString(SAVED_HISTORY_KEY, history.getText().toString());
		editor.commit();
	}

	@Override
	/** restore Calculator.State in bundle:*/
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		@SuppressWarnings("deprecation")
		Object state = getLastNonConfigurationInstance();
		if (state != null) {
			display.setText(calc.setState(state));
		}
	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// Log.d("State=", "onSaveInstanceState");
	// // outState.putSerializable(SAVED_STATE_KEY, calc.getState());
	// super.onSaveInstanceState(outState);
	// }

	/** save Calculator.State: */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return calc.getState();
	}
}
