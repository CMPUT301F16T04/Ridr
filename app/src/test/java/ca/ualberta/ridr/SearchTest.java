package ca.ualberta.ridr;

import org.junit.Test;
import org.junit.Assert;

import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by jferris on 04/11/16.
 */
public class SearchTest {
    @Test
    public void searchTest() throws Exception {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        RequestController rController = new RequestController();

        Request request = rider.requestRide("University of Alberta", "NAIT");
        Request request2 = rider.requestRide("Some place", "A different place");

        assertTrue(request.equals(rController.SearchRequestsKeyword("University of Alberta").get(0)));
        assertFalse(request2.equals(rController.SearchRequestsKeyword("University of Alberta").get(0)));
        assertFalse(rController.SearchRequestsKeyword("Steve").get(0).getRider().getName() == "Joe");
    }
}
