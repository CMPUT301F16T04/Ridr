package ca.ualberta.ridr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsView extends AppCompatActivity {

    private ArrayList<Request> requestList = new ArrayList<Request>();
    private ArrayAdapter<Request> adapter;
    final RequestController requestController = new RequestController();
    private TextView bodyText;
    private String keyword;

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

        String text = bodyText.getText().toString();
        RequestController.SearchRequestsKeyword searchRequestsKeyword = new RequestController.SearchRequestsKeyword();
        searchRequestsKeyword.execute(keyword);
        try {
            requestList = searchRequestsKeyword.get();
        }
        catch (Exception e) {
            Log.i("Error", "Failed to get the requests out of the async object.");
        }
        adapter.notifyDataSetChanged();
    }
}
