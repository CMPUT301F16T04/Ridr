package ca.ualberta.ridr;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.Assert;

import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by jferris on 04/11/16.
 */
public class SearchTest {
   @Test
    public void searchTest() throws Exception {
        Gson gson = new Gson();
        Context context = null;
        RequestController requestController = new RequestController(context);
        Request request = new Request("Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        String jsonString = gson.toJson(request);
        JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
        assertTrue(requestController.doesJsonContainKeyword("Justin",jsonElement));
        assertTrue(requestController.doesJsonContainKeyword("Alberta",jsonElement));
        assertTrue(requestController.doesJsonContainKeyword("Edmonton",jsonElement));
    }
}
