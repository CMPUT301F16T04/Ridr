package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by jferris on 22/10/16.
 */
public class DriverController {
    private static JestDroidClient client;

    //this class gets Drivers!
    public static class GetDriverTask extends AsyncTask<String, Void, Driver> {
        @Override
        protected Driver doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"from\": 0, \"size\": 1, \"query\": {\"match\": {\"name\": \"" + search_parameters[0] + "\"}}}}";
            //search string should work, is searching for the name, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("CMPUT301F16T04")
                    .addType("driver")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Driver foundDriver = result.getFirstHit(Driver.class).source; //might not work
                    return foundDriver;
                }
                else {
                    Log.i("Error", "The search query failed to find the driver that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }


    //this class adds a Driver!
    public static class AddDriverTask extends AsyncTask<Driver, Void, Void> {

        @Override
        protected Void doInBackground(Driver... drivers) {
            verifySettings();

            for (Driver driver: drivers) {
                Index index = new Index.Builder(driver).index("CMPUT301F16T04").type("driver").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        driver.setDriverID(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the driver, as the result did not succeed.");
                    }
                }
                catch (Exception e) {
                    Log.i("Exception", "We failed to add a driver to elastic search, because of an exception!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    //used from lonelyTwitter lab7
    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }







    //TEST METHODS - FOR TESTS ONLY
        public Driver GetDriverTaskTest (String... search_parameters){
            verifySettings();

            String search_string = "{\"from\": 0, \"size\": 1, \"query\": {\"match\": {\"name\": \"" + search_parameters[0] + "\"}}}}";
            //search string should work, is searching for the name, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("CMPUT301F16T04")
                    .addType("driver")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Driver foundDriver = result.getFirstHit(Driver.class).source; //might not work
                    return foundDriver;
                }
                else {
                    Log.i("Error", "The search query failed to find the driver that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }

    public void AddDriverTaskTest(Driver... drivers){
        verifySettings();

        for (Driver driver: drivers) {
            Index index = new Index.Builder(driver).index("CMPUT301F16T04").type("driver").build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    driver.setDriverID(result.getId());
                }
                else {
                    Log.i("Error", "Elastic search was not able to add the driver, as the result did not succeed.");
                }
            }
            catch (Exception e) {
                Log.i("Exception", "We failed to add a driver to elastic search, because of an exception!");
                e.printStackTrace();
            }
        }
    }
}
