package ca.ualberta.ridr;

/**
 * Created by jferris on 22/10/16.
 */
public class DriverController {
    DriverController(){}

    //is this supposed to be a driver controller item or a request controller item... look at UML in the morning
    public void acceptRequest(Driver driver, Request request){
        driver.acceptRequest(request);
    }
}
