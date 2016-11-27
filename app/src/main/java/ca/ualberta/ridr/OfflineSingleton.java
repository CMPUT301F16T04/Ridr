package ca.ualberta.ridr;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * A singleton to store the things that need to be done once
 *  back online
 */

public class OfflineSingleton {
    private static final OfflineSingleton instance =
            new OfflineSingleton();
    private ArrayList<Request> riderRequests;
    private ArrayList<Request> driverRequests;

    private OfflineSingleton() {
        this.riderRequests = new ArrayList<>();
        this.driverRequests = new ArrayList<>();
    };

    public static OfflineSingleton getInstance() {
        return instance;
    }

    public void addRiderRequest(Request request) {
        riderRequests.add(request);
    }

    public ArrayList<Request> getRiderRequests () {
        return riderRequests;
    }

    public void clearRiderRequests() {
        riderRequests.clear();
    }

    public void addDriverAcceptance(Request request) {
        driverRequests.add(request);
    }

    public boolean isPendingAcceptance() {
        return driverRequests.size() > 0;
    }

    public ArrayList<Request> getDriverRequests() {
        return driverRequests;
    }

    public void clearDriverRequests() {
        driverRequests.clear();
    }
}
