package ILPtoCPLEX.Testing;

import ILPtoCPLEX.Main;
import ilog.concert.IloException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JUnitTest {

    @Test
    public void mainTest() throws IloException {
        Main.main(null);
    }


    @Test
    public void equalTest() throws IloException {
        assertEquals(Main.ilpResults, Main.iplexResults);
    }

    @Test
    public void notEqualTest() throws IloException {
        assertNotEquals(Main.ilpResults, Main.iplexResults);
    }
}
