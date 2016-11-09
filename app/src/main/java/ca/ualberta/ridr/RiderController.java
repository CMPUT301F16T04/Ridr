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
    public void addRequest(Request request) {
        ArrayList<Request> requestList = rider.getRequestArrayList();
        requestList.add(request);
        rider.setRequestArrayList(requestList);

    }

    /**
     * Sets all matching requests owned by the rider to true
     */
    public void acceptRequest(Request request) {
        // Needs to be made MVC

    }

    /**
     * Returns an arrayList of all open requests based on the rider's request list
     */
    public ArrayList<Request> getOpenRequests(){
        ArrayList<Request> openRequests = rider.getRequestArrayList();

        for (int i = 0; i < rider.getRequestArrayList().size(); i++) {
            Request request = openRequests.get(i);
            if (request.isAccepted()) {
                openRequests.remove(i);
            }
        }
        return openRequests;
    }

    public void updateView(){
        //riderView.update();
    }

}

