package model.input.file;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for the XlsFile class.
 * @author pablo
 *
 */
public class XlsxFileTest {

	private XlsxFile xlsxFile;
	
	@Before
	public void setUp() {
		String file = getClass().getResource("/model/input/xlsxfile.xlsx").getFile();
		xlsxFile = new XlsxFile(file);
	}
	
}
