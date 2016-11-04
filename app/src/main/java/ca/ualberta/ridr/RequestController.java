package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController {
    private static JestDroidClient client;

    public static class SearchRequestsKeyword extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<Request>();

            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match\": {\"message\": \"" +search_parameters[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex("CMPUT301F16T04")
                    .addType("request")
                    .build();


            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Request> foundRequests = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);
                }
                else {
                    Log.i("Error", "The search query failed to find any requests that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elastic search server!");
            }

            return requests;
        }
    }

    //used from lonelyTwitter lab7
    private static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
