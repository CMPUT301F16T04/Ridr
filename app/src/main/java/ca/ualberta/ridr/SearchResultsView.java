package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The view that shows keyword search results for a driver
 * Also allows new keyword searches to be performed
 */
public class SearchResultsView extends Activity {

    private ArrayList<Request> requestList = new ArrayList<>();
    final RequestController requestController = new RequestController();
    private String keyword;
    private ListView searchResults;
    private RequestAdapter requestAdapter;
    private EditText bodyText;

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
        searchResultsByKeyword(keyword);
        searchResults = (ListView) findViewById(R.id.search_results);
        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //implement when putting together for on click items
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*Intent intent = new Intent(SearchResultsView.this, .class);
                intent.putExtra("Habit", position);
                startActivity(intent);*/
            }
        });

        bodyText = (EditText) findViewById(R.id.searchRequestsText);
        Button searchButton = (Button) findViewById(R.id.searchRequestsButton);

        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = bodyText.getText().toString();
                searchResultsByKeyword(text);
                requestAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAdapter = new RequestAdapter(this, requestList);
        searchResults.setAdapter(requestAdapter);
    }

    /**
     * Calls on the request controller method searchRequestsKeyword
     * Updates the requestList without making it a new object (Clear and re add)
     * @param keyword
     */
    protected void searchResultsByKeyword(String keyword) {
        if(keyword != null) {
            ArrayList<Request> tempRequestList = requestController.searchRequestsKeyword(keyword);
            requestList.clear();
            requestList.addAll(tempRequestList);
        }
    }
}

