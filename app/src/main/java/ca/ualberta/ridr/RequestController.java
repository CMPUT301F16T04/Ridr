package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController {
    private static JestDroidClient client;
    private ArrayList<Request> requests = new ArrayList<>();
    private JsonArray jsonArray;

    public RequestController(){}

    //Hard to test
    //Should return arraylist of requests that match the keyword in their queryable fields
    //Queryable fields include rider name, email, phone and request start and end location
    public ArrayList<Request> searchRequestsKeyword(String keyword) {
        jsonArray = new AsyncController().getAllFromIndex("request");
        Gson gson = new Gson();
        ArrayList<String> stringArray;
        ArrayList<Request> requestsKeyword = new ArrayList<>();
        Request request;

        Pattern p = Pattern.compile(keyword);

        for (int i = 0; i < jsonArray.size(); ++i) {
            request = gson.fromJson(jsonArray.get(i).getAsJsonObject(), Request.class);
            stringArray = request.queryableRequestVariables();
            for (String s: stringArray) {
                if (p.matcher(s).matches()) {
                    requestsKeyword.add(request);
                }
            }

        }
        return requestsKeyword;
    }

    public static class SearchRequestsKeyword extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<Request>();

            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match\": {\"message\": \"" +search_parameters[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
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
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("https://search-ridr-3qapqm6n4kj3r37pbco5esgwrm.us-west-2.es.amazonaws.com/");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
