package model.input.file;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for the PlainTextFile class.
 * @author Paul
 *
 */
public class PlainTextFileTest {

	private PlainTextFile textFile;
	
	@Before
	public void setUp() {
		String file = getClass().getResource("/model/input/plaintext.txt").getFile();
		textFile = new PlainTextFile(file);
	}
	

}
