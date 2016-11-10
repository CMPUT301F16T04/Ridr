package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by mackenzie on 09/11/16.
 * TO impliment database tasks for a given rider
 */
public class AsyncDatabaseController extends AsyncTask<String, Void, JsonObject> {
    private static JestDroidClient client;
    private static String databaseLink
            = "https://search-ridr-3qapqm6n4kj3r37pbco5esgwrm.us-west-2.es.amazonaws.com/";

    // Constructor for controller
    public AsyncDatabaseController() {
        //this.c = c;
    }

    /**
     * Queries elastic search for an object with matching UUID
     *
     * @param search_parameters
     * @return
     */
    @Override
    protected JsonObject doInBackground(String... parameters) {
        verifySettings();

        String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \""+ parameters[0]+"\":\"" + parameters[1] + "\"}}}}}";
        //search string should work, is searching for the name, only returns 1 result

        Search search = new Search.Builder(search_string)
                .addIndex("cmput301f16t04")
                .addType("driver")
                .build();
        try {
            SearchResult result = client.execute(search);
            if (result.isSucceeded()) {
                return extractFirstElement(result);
            }
            else {
                Log.i("Error", "The search query failed to find the Class that matched.");
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

    private JsonObject extractFirstElement(SearchResult result){
        return result.getJsonObject().getAsJsonObject("hits").getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source");
    }

}