package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The view that shows keyword search results for a driver
 * Also allows new keyword searches to be performed
 */
public class SearchResultsView extends Activity {

    private ArrayList<Request> requestList = new ArrayList<>();
    private Context context = this;
    private RequestController requestController = new RequestController(context);
    private RiderController riderController = new RiderController(context);
    private String keyword;
    private ListView searchResults;
    private RequestAdapter requestAdapter;
    private EditText bodyText;
    private String username;
    private Button mainMenu;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_driver);

        Intent intent= getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null)
        {
            username = extras.getString("username");
        }

        //main menu button
        mainMenu = (Button)findViewById(R.id.driverMainMenuButton);
        mainMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showMenu(v);
            }
        });

        searchResults = (ListView) findViewById(R.id.search_results);
        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //implement when putting together for on click items
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(SearchResultsView.this, AcceptRiderView.class);
                Request clickedRequest = (Request)searchResults.getItemAtPosition(position);
                intent.putExtra("RequestUUID", clickedRequest.getID().toString());
                intent.putExtra("userName", username);
                startActivity(intent);
            }
        });

        bodyText = (EditText) findViewById(R.id.searchRequestsText);
        Button searchButton = (Button) findViewById(R.id.searchRequestsButton);

        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = bodyText.getText().toString();
                searchResultsByKeyword(context, text);
                requestAdapter.notifyDataSetChanged();

                requestController.executeAllPending(username);
            }
        });

        Button switchGeoButton = (Button) findViewById(R.id.searchByGeoButton);

        switchGeoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(SearchResultsView.this, GeoView.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //get driver object from server, and display notification if there is one
        //we want this in OnStart, as every time we load up this activity we want to check for notifications
        if(isConnected()) {
            DriverController driverController = new DriverController(context);
            driver = driverController.getDriverFromServerUsingName(username);
            //check for notifications, display
            if (driver.getPendingNotification() != null) {
                Toast.makeText(this, driver.getPendingNotification(), Toast.LENGTH_LONG).show();
                driver.setPendingNotification(null);
                //update the user object in the database
                try {
                    AsyncController asyncController = new AsyncController(context);
                    asyncController.create("user", driver.getID().toString(), new Gson().toJson(driver));
                    //successful account updating
                } catch (Exception e) {
                    Log.i("Communication Error", "Could not communicate with the elastic search server");
                }
                driver = driverController.getDriverFromServerUsingName(username);
                //check for notifications, display
                if (driver.getPendingNotification() != null) {
                    Toast.makeText(this, driver.getPendingNotification(), Toast.LENGTH_LONG).show();
                    driver.setPendingNotification(null);
                    //update the user object in the database
                    try {
                        AsyncController asyncController = new AsyncController(context);
                        asyncController.create("user", driver.getID().toString(), new Gson().toJson(driver));
                        //successful account updating
                    } catch (Exception e) {
                        Log.i("Communication Error", "Could not communicate with the elastic search server");
                    }
                }
            }
        }

        requestAdapter = new RequestAdapter(this, requestList);
        searchResults.setAdapter(requestAdapter);

        //Executes any pending functions from offline functionality once online
        requestController.executeAllPending(username);
        riderController.pushPendingNotifications();
    }

    /**
     * Calls on the request controller method searchRequestsKeyword
     * Updates the requestList without making it a new object (Clear and re add)
     *
     * @param keyword the keyword
     */
    protected void searchResultsByKeyword(Context context, String keyword) {
        if(keyword != null) {
            ArrayList<Request> tempRequestList = requestController.searchRequestsKeyword(keyword);
            requestList.clear();
            requestList.addAll(tempRequestList);
        }
    }

    /**
     * Show menu.
     *
     * @param v the view
     */
    public void showMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.rider_main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item){
                switch(item.getItemId()){
                    case R.id.mainRiderMenuEditUserInfo:
                        Toast.makeText(SearchResultsView.this, "Edit User Info", Toast.LENGTH_SHORT).show();
                        Intent editInfoIntent = new Intent(SearchResultsView.this, EditProfileView.class);
                        editInfoIntent.putExtra("Name", username);
                        startActivity(editInfoIntent);
                        return true;
                    case R.id.mainRiderMenuViewRequests:
                        Toast.makeText(SearchResultsView.this, "View Requests", Toast.LENGTH_SHORT).show();
                        Intent viewRequestsIntent = new Intent(SearchResultsView.this, RequestsFromRidersView.class);
                        viewRequestsIntent.putExtra("Name", username);
                        startActivity(viewRequestsIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    private Boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

