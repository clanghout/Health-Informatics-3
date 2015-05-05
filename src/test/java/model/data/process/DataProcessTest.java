package model.data.process;

import model.data.DataModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * The test suite for DataProcess
 *
 * Created by Boudewijn on 5-5-2015.
 */
public class DataProcessTest {

    private DataProcess process;

    @Before
    public void setUp() throws Exception {
        process = mock(DataProcess.class);
    }

    @Test
    public void testProcess() throws Exception {
        DataModel output = new DataModel();

        when(process.doProcess()).thenReturn(output);

        process.process();
        verify(process).doProcess();
        assertEquals(output, process.getOutput());
    }

    @Test
    public void testSetInput() throws Exception {
        DataModel model = new DataModel();

        process.setInput(model);
        assertEquals(model, process.getInput());
    }
}