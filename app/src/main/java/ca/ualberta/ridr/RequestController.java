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
                if (p.matcher(s).matches() && !requestsKeyword.contains(request)) {
                    requestsKeyword.add(request);
                }
            }

        }
        return requestsKeyword;
    }

}
