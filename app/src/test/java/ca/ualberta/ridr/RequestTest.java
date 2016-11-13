package ca.ualberta.ridr;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Justin on 2016-11-03.
 */

public class RequestTest {
    @Test
    public void testRequestEquals(){
        Rider rider = null;
        Request request = new Request(rider,"Edmonton", "Timbuktu", "some day", "later");
        assertTrue(request.equals(request));
    }
}
