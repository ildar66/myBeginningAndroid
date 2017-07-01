package ru.ildar66.calculator;

/**
 * @author Shafigullin Ildar
 * 
 *         Simply Model for Calculator
 * 
 */
public class Calculator_0 {

	private static final String EQUAL = "=";
	private static final String DIVISION = "/";
	private static final String MULTIPLY = "*";
	private static final String MINUS = "-";

	private boolean start = true;
	private String lastCommand = EQUAL;
	private double result = 0;

	private String display = "0";

	/**
	 * Input for calculation operations Precondition: Valid input only for digital and point key.
	 * 
	 * @param input
	 * @return
	 */
	public String input(String input) {
		if (start) {
			display = "";
			start = false;
		}
		display = display + input;
		return display;
	}

	/**
	 * Command for calculation operations. Precondition: Valid input only for operation(+, -, /, *, =)
	 * 
	 * @param command
	 * @return
	 */
	public String command(String command) {
		if (start) {
			if (command.equals(MINUS)) {
				display = command;
				start = false;
			} else
				lastCommand = command;
		} else {
			try {
				calculate(Double.parseDouble(display));
				lastCommand = command;
				start = true;
			} catch (NumberFormatException e) {
				clear();
				display = "Error! Please type \"Clear\"";
			}
		}
		return display;
	}

	/**
	 * @param x
	 */
	private void calculate(double x) {
		if (lastCommand.equals("+"))
			result += x;
		else if (lastCommand.equals(MINUS))
			result -= x;
		else if (lastCommand.equals(MULTIPLY))
			result *= x;
		else if (lastCommand.equals(DIVISION))
			result /= x;
		else if (lastCommand.equals(EQUAL))
			result = x;
		display = "" + result;
	}

	/**
	 * reset calculation
	 * 
	 * @return
	 */
	public String clear() {
		start = true;
		lastCommand = EQUAL;
		result = 0;
		display = "0";
		return display;
	}

	/**
	 * remove last character from display
	 * 
	 * @return
	 */
	public String backSpace() {
		if (display.length() > 0) {
			display = display.substring(0, display.length() - 1);
		}
		return display;
	}

}
