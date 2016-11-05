package ca.ualberta.ridr;

import java.util.ArrayList;

/**
 * Created by jferris on 22/10/16.
 * Edited by Mackenzie on 05/11/16
 */
public class RiderController {
    private Rider rider;
    private RiderRequestView riderView;

    public RiderController(Rider rider) {
        this.rider = rider;
    }

    /**
     * Adds a request to the rider from textual input
     */
    public void addRequest(String s, String s1) {
        Request request = new Request(rider, s, s1);
        rider.getRequestArrayList().add(request);
    }

    /**
     * Sets all matching requests owned by the rider to true
     */
    public void acceptRequest(Request request) {
        // Needs to be made MVC
        for (int i = 0; i < rider.getRequestArrayList().size(); i++) {
            if (rider.getRequestArrayList().get(i) == request) {
                rider.getRequestArrayList().get(i).setAccepted(true);
                break;
            }
        }
    }

    /**
     * Returns an arrayList of all open requests based on the rider's request list
     */
    public ArrayList<Request> getOpenRequests(){
        ArrayList<Request> openRequests = new ArrayList<>();
        for (int i = 0; i < rider.getRequestArrayList().size(); i++) {
            Request request = rider.getRequestArrayList().get(i);
            if (request.isAccepted()) {
                // Need to make this MVC
                openRequests.add(request);
            }
        }
        return openRequests;
    }

    public void updateView(){
        //riderView.update();
    }

}

