package ca.ualberta.ridr;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController {
    RequestController(){}

    public void accept(Request request){
        request.setAccepted(Boolean.TRUE);
    }

    public void addDriver(Request request, Driver driver){
        request.addAccepted(driver);
    }
}
