package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.searchbox.annotations.JestId;

/**
 * Created by jferris on 22/10/16.
 * Worked on by Marc-O and Kristy on 03/11/2016
 *
 * This object represents a Rider, which extends a User.
 */
public class Rider extends User {
    @JestId
    private transient ArrayList<Request> requestArrayList;

    public Rider (String name, Date dateOfBirth, String creditCard, String email, String phoneNumber) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.requestArrayList = new ArrayList<Request>();
        super.setRiderStatus(true);
    }

    public Rider (User user){
        super(user);
        super.setRiderStatus(true);
    }
    public void setRiderStatus(boolean status) {
        super.setRiderStatus(status);
    }

    public ArrayList<Request> getRequests() {
        return requestArrayList;
    }

    public void setRequests(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
    }

    public void removeRequest(Request currentRequest) {
        requestArrayList.remove(currentRequest);
    }


}
