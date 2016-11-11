package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by mackenzie on 09/11/16.
 * TO impliment database tasks for a given rider
 */
public class AsyncDatabaseController extends AsyncTask<String, Void, JsonObject> {
    private static JestDroidClient client;
    private String action;
    private static String databaseLink
            = "https://search-ridr-3qapqm6n4kj3r37pbco5esgwrm.us-west-2.es.amazonaws.com/";
    private static String databaseName = "ridr";

    // Constructor for controller
    public AsyncDatabaseController(String action) {
        this.action = action;
    }

    /**
     * Queries elastic search for an object with matching UUID
     *
     * @return
     */
    @Override
    protected JsonObject doInBackground(String... parameters) {
        verifySettings();

        //search string should work, is searching for the name, only returns 1 result

        try {
            if(action == "get") {
                return getRequest(parameters[0], parameters[1]).getJsonObject();
            } else if(action == "create"){
                return createRequest(parameters[0], parameters[1]);
            } else if(action == "getAllFromIndex"){
                return extractFirstElement((SearchResult) getRequest(parameters[0], parameters[1]));
            }
        } catch (Exception e) {
            Log.i(e.toString(),
                    "Something went wrong when we tried to communicate with the elasticsearch  server!");
        }

        return null;
    }

    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(databaseLink);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }


    @Nullable
    private JestResult getRequest(String type, String  searchString) throws IOException {
        Search search = new Search.Builder(searchString)
                .addIndex(databaseName)
                .addType(type)
                .build();
        JestResult result = client.execute(search);
        if (result.isSucceeded()) {
            return result;
        }
        else {
            Log.i("Error", "The search query failed to find the Class that matched.");
            return null;
        }
    }

    @Nullable
    private JsonObject createRequest(String type, Object value) throws IOException {
        Index index = new Index.Builder(value).index(databaseName).type(type).build();
        DocumentResult result = client.execute(index);
        if (result.isSucceeded()) {
            return result.getJsonObject();
        } else {
            Log.i("Error", "Elastic search was not able to add the driver, as the result did not succeed.");
            return null;
        }
    }

}

