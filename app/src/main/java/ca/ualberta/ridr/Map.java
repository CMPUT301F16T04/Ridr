package ca.ualberta.ridr;

/**
 * Created by jferris on 13/10/16.
 */
public class Map {
    String startPinLocation;
    String endPinLocation;

    public Map() {
        this.startPinLocation = "";
        this.endPinLocation =  "";
    }

    public void setStartPinLocation(String startPinLocation) {
        this.startPinLocation = startPinLocation;
    }

    public void setEndPinLocation(String endPinLocation) {
        this.endPinLocation = endPinLocation;
    }

    public String getEndPinLocation() {
        return endPinLocation;
    }

    public String getStartPinLocation() {
        return startPinLocation;
    }
}
