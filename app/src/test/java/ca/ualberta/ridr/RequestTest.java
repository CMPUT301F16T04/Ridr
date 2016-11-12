package ca.ualberta.ridr;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Justin on 2016-11-03.
 */

public class RequestTest {
    @Test
    public void testRequestEquals(){
        Request request = new Request("Edmonton", "Timbuktu", pickupCoords, dropOffCoords, date);
        assertTrue(request.equals(request));
    }
}
