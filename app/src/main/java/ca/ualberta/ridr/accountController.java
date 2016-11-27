package ca.ualberta.ridr;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Justin on 2016-11-24.
 */
/*
 * A controller for the creation of new users and for logging them into the system
 */

public class AccountController {
    private ACallback cbInterface;
    private User currentUser;
    private AsyncController controller;

    public AccountController(ACallback cbInterface){
        this.cbInterface = cbInterface;
        controller = new AsyncController();
        currentUser = null;
    }

    public void loginUser(String username){
        try {
            currentUser = new Gson().fromJson(controller.get("user", "name", username), User.class);
            Log.i("User", currentUser.getName());
        } catch(Exception e){
            Log.i("Invalid User", e.toString());
            currentUser = null;
        }
        cbInterface.update();
    }

    @Nullable
    public User getUser(){
        return currentUser;
    }


}
