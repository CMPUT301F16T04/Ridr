package ca.ualberta.ridr;

/**
 * Created by jferris on 22/10/16.
 * Worked on by Marc-O and Kristy on 03/11/2016
 */
public class RequestController {
    RequestController(){}

    public void createRequest(Rider rider, String pickup, String dropoff){
        Request request = new Request(rider, pickup, dropoff);
        rider.addRequest(request);
    }
}
