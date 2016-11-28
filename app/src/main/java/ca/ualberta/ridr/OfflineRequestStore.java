package ca.ualberta.ridr;

import java.util.ArrayList;

/**
 * Created by jferris on 26/11/16.
 */
//code from http://stackoverflow.com/questions/24234728/java-store-method-calls-in-an-array-and-execute-later
public class OfflineRequestStore {
    private ArrayList<Runnable> listOfMethods = new ArrayList<>();

    public void addMethod(Runnable methodCall) {
        listOfMethods.add(methodCall);
    }

    public void invokeMethods() {
        for (Runnable method: listOfMethods) {
            method.run();
        }
    }
}
