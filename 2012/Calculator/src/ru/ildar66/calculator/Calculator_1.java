package ru.ildar66.calculator;

/**
 * @author Shafigullin Ildar
 * 
 *         Simply Model for Calculator
 * 
 */
public class Calculator_1 {

	public static final String EQUAL = "=";
	public static final String DIVISION = "/";
	public static final String MULTIPLY = "*";
	public static final String MINUS = "-";

	private class State {

		private boolean start;
		private String lastCommand;
		private double result;
		private String display;

		private State() {
			clear();
		}

		private String clear() {
			start = true;
			lastCommand = Calculator_1.EQUAL;
			result = 0;
			display = "0";
			return display;
		}
	}

	private State state = new State();

	/**
	 * Input for calculation operations Precondition: Valid input only for digital and point key.
	 * 
	 * @param input
	 * @return
	 */
	public String input(String input) {
		if (state.start) {
			state.display = "";
			state.start = false;
		}
		state.display = state.display + input;
		return state.display;
	}

	/**
	 * Command for calculation operations. Precondition: Valid input only for operation(+, -, /, *, =)
	 * 
	 * @param command
	 * @return
	 */
	public String command(String command) {
		if (state.start) {
			state.lastCommand = command;
		} else {
			try {
				calculate(Double.parseDouble(state.display));
				state.lastCommand = command;
				state.start = true;
			} catch (NumberFormatException e) {
				state.clear();
				state.display = "Error! Please type \"Clear\"";
			}
		}
		return state.display;
	}

	/**
	 * @param x
	 */
	private void calculate(double x) {
		if (state.lastCommand.equals("+"))
			state.result += x;
		else if (state.lastCommand.equals(MINUS))
			state.result -= x;
		else if (state.lastCommand.equals(MULTIPLY))
			state.result *= x;
		else if (state.lastCommand.equals(DIVISION))
			state.result /= x;
		else if (state.lastCommand.equals(EQUAL))
			state.result = x;
		state.display = "" + state.result;
	}

	/**
	 * remove last character from display
	 * 
	 * @return
	 */
	public String backSpace() {
		if (state.display.length() > 0) {
			state.display = state.display.substring(0, state.display.length() - 1);
		}
		return state.display;
	}

	public Object getState() {
		return state;
	}

	public String setState(Object obj) {
		state = (State) obj;
		return state.display;
	}

	public String clear() {
		return state.clear();
	}

}
