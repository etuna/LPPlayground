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

    @Test
    public void objectiveTest() throws IloException {
        assertEquals(Main.ILPResult.getObjective().doubleValue(),Main.ILPResult.getObjective().doubleValue(), Main.IPLEXResult.getObjValue());
    }

    /**
     *
     * @throws IloException
     */
    @Test
    public void equalYTest() throws IloException {
        assertEquals(Main.ilpResults, Main.iplexResults);
    }

    /**
     *
     * @throws IloException
     */
    @Test
    public void notEqualYTest() throws IloException {
        assertNotEquals(Main.ilpResults, Main.iplexResults);
    }
}
