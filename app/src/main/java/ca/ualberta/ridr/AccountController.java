package ca.ualberta.ridr;

import android.content.Context;
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
    private Context context;
    private AsyncController controller;

    public AccountController(ACallback cbInterface, Context context){
        this.context = context;
        this.cbInterface = cbInterface;
        controller = new AsyncController(context);
        currentUser = null;
    }

    public void loginUser(String username){
        try {
            currentUser = new Gson().fromJson(controller.get("user", "name", username), User.class);
            Log.i("User", String.valueOf(currentUser.isDriver()));
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
