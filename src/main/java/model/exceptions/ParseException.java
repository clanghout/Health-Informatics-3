package model.exceptions;

import org.parboiled.errors.ParseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Boudewijn on 11-6-2015.
 */
public class ParseException extends Exception {

	private final List<ParseError> errors;

	public ParseException(String message) {
		super(message);
		errors = Collections.emptyList();
	}

	public ParseException(String message, List<ParseError> errors) {
		super(message);
		this.errors = new ArrayList<>(errors);
	}

	public List<ParseError> getParseErrors() {
		return Collections.unmodifiableList(errors);
	}
}
