package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    /**
     * Uses a keyword to search through the queryable fields for requests
     * Returns the requests containing the keyword in one or more of the fields
     * Does not return duplicates of the same request if multiple instances of keyword
     * @param keyword
     * @return ArrayList<Request>
     */
    public ArrayList<Request> searchRequestsKeyword(String keyword) {
        jsonArray = new AsyncController().getAllFromIndex("request");
        ArrayList<Request> requestsKeyword = new ArrayList<>();
        Request request;

        for (JsonElement element: jsonArray) {
            if(doesJsonContainKeyword(keyword, element)) {
                try {
                    request = new Request(element.getAsJsonObject().getAsJsonObject("_source"));
                    requestsKeyword.add(request);
                } catch(Exception e) {
                    Log.i("Error returning keyword", e.toString());
                }
            }
        }
        return requestsKeyword;
    }

    /**
     * Checks if the JsonElement for request contains the string keyword
     * @param keyword
     * @param jsonElement
     * @return Boolean
     */
    public Boolean doesJsonContainKeyword(String keyword, JsonElement jsonElement) {
        ArrayList<String> stringArray;
        keyword = keyword.toLowerCase();
        Pattern p = Pattern.compile(keyword);
        Request request;
        try {
            request = new Request(jsonElement.getAsJsonObject().getAsJsonObject("_source"));
            stringArray = request.queryableRequestVariables();
            for (String s : stringArray) {
                s = s.toLowerCase();
                if (p.matcher(s).find()) {
                    return true;
                }
            }
        } catch(Exception e) {
            Log.i("Error searching keyword", e.toString());
        }

        return false;
    }

}
