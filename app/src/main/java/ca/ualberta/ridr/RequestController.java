package ca.ualberta.ridr;

/**
 * Created by jferris on 22/10/16.
 * Worked on by Marc-O and Kristy on 03/11/2016
 */
public class RequestController {
    RequestController(){}
    private Request request;

    public void createRequest(Rider rider, String pickup, String dropoff, String date, String time){
        request = new Request(rider, pickup, dropoff, date, time);
        rider.addRequest(request);
    }

    public float getFareEstimate(float distance){
        return request.estimateFare(distance);
    }

    public void updateFare(float fare){
        request.setFare(fare);
    }
}
