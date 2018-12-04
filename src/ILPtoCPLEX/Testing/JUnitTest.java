package ILPtoCPLEX.Testing;

import ILPtoCPLEX.Main;
import ilog.concert.IloException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JUnitTest {

    /**
     *
     * @throws IloException
     */
    @Test
    public void mainTest() throws IloException {
        Main.main(null);
    }


    /**
     *
     * @throws IloException
     */
    @Test
    public void equalTest() throws IloException {
        assertEquals(Main.ilpResults, Main.iplexResults);
    }

    /**
     *
     * @throws IloException
     */
    @Test
    public void notEqualTest() throws IloException {
        assertNotEquals(Main.ilpResults, Main.iplexResults);
    }
}
