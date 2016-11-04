package ca.ualberta.ridr;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Justin on 2016-11-03.
 */

public class RequestTest {
    public void testRequestEquals(){

        Request request = new Request("Edmonton", "Timbuktu");
        assertTrue(request.equals(request));
    }
}
