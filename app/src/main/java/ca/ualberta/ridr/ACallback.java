package ca.ualberta.ridr;

/**
 * Created by Justin on 2016-11-11.
 */
// This is the laziest way I can think to write asynchronous code
public interface ACallback {
    /**This interface is used when a controller updates it's data
    * It will call this callback on whoever instantiated that controller
     */
    void update();
}
