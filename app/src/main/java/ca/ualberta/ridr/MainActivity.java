package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {
    private ArrayList<Request> requestList = new ArrayList<>();
    final RequestController requestController = new RequestController();
    private String keyword;
    private ListView searchResults;
    private RequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider, "start", "end", new Date());
        request.setFare(8.76f);
        requestList.add(request);
        //requestList = requestController.searchRequestsKeyword(keyword);
        searchResults = (ListView) findViewById(R.id.search_results);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAdapter = new RequestAdapter(this, requestList);
        searchResults.setAdapter(requestAdapter);
    }
}
