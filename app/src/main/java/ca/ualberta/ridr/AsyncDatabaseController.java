package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by mackenzie on 09/11/16.
 * TO impliment database tasks for a given rider
 */
public class AsyncDatabaseController extends AsyncTask<String, Void, Class> {
    private Class c;
    private static JestDroidClient client;
    private static String databaseLink
            = "https://search-ridr-3qapqm6n4kj3r37pbco5esgwrm.us-west-2.es.amazonaws.com/";

    // Constructor for controller
    public AsyncDatabaseController(Class c) {
        this.c = c;
    }

    /**
     * Queries elastic search for an object with matching UUID
     *
     * @param search_parameters
     * @return
     */
    @Override
    protected Class doInBackground(String... search_parameters) {
        verifySettings();
        String typeString = c.getClass().toString().toLowerCase();

        String search_string =
                "{\"query\": { \"bool\": { \"must\": { \"match\": { \"id\":\"" + search_parameters[0] + "\"}}}}}";

        Search search = new Search.Builder(search_string)
                .addIndex("cmput301f16t04")
                .addType(typeString)
                .build();
        try {
            SearchResult result = client.execute(search);
            if (result.isSucceeded()) return (Class) result.getFirstHit(Object.class).source;
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

    }

public  class asycOperation<T> {
    public class get(String type, String id) {
        return AsyncDatabaseController.execute(type, id);
    }
}
