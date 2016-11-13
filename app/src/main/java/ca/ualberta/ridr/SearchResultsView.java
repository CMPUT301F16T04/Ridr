package ca.ualberta.ridr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsView extends AppCompatActivity {

    private ArrayList<Request> requestList = new ArrayList<>();
    final RequestController requestController = new RequestController();
    private String keyword;
    private ListView searchResults;
    private RequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        Intent intent= getIntent();
        Bundle extras = intent.getExtras();

        if(extras!=null)
        {
            keyword = extras.getString("keyword");
        }

        requestList = requestController.searchRequestsKeyword(keyword);
        searchResults = (ListView) findViewById(R.id.search_results);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAdapter = new RequestAdapter(this, requestList);
        searchResults.setAdapter(requestAdapter);
    }
}

