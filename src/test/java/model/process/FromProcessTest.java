package model.process;

import model.language.Identifier;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 2-6-2015.
 */
public class FromProcessTest {

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyIdentifiers() throws Exception {
		FromProcess process = new FromProcess(new Identifier[0]);
	}
}