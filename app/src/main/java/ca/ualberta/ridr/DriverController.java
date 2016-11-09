package ca.ualberta.ridr;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;


import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by jferris on 22/10/16.
 */
public class DriverController {
    private static JestDroidClient client;

    /**
     * This class gets Drivers by name string
     */
    public static class GetDriverByNameTask extends AsyncTask<String, Void, Driver> {
        @Override
        protected Driver doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"name\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the name, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
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
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch driver server!");
            }

            return null;
        }
    }

    /**
     * This class gets Drivers by a UUID string
     */
    public static class GetDriverByUUIDTask extends AsyncTask<String, Void, Driver> {
        @Override
        protected Driver doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"id\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the UUID, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
                    .addType("driver")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Driver foundDriver = result.getFirstHit(Driver.class).source; //might not work
                    return foundDriver;
                } else {
                    Log.i("Error", "The search query failed to find the driver that matched.");
                }
            } catch (Exception e) {
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch driver server!");
            }
            return null;
        }
    }


    /**
     * This class adds a Driver object
     */
    public static class AddDriverTask extends AsyncTask<Driver, Void, Void> {

        @Override
        protected Void doInBackground(Driver... drivers) {
            verifySettings();

            for (Driver driver: drivers) {
                Index index = new Index.Builder(driver).index("cmput301f16t04").type("driver").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        driver.setElasticID(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the driver, as the result did not succeed.");
                    }
                }
                catch (Exception e) {
                    Log.i(e.toString(), "We failed to add a driver to elastic search, because of an exception!");
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
            //DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("https://search-ridr-3qapqm6n4kj3r37pbco5esgwrm.us-west-2.es.amazonaws.com/");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }







    //TEST CLASSES - FOR TESTS ONLY
    /**
     * This class gets Drivers by name string, is used as a test
     */
    public static class GetDriverByUUIDTaskTest extends AsyncTask<String, Void, Driver> {
        @Override
        protected Driver doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"id\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the UUID, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
                    .addType("drivertest")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Driver foundDriver = result.getFirstHit(Driver.class).source; //might not work
                    return foundDriver;
                } else {
                    Log.i("Error", "The search query failed to find the driver that matched.");
                }
            } catch (Exception e) {
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch driver server!");
            }
            return null;
        }
    }

    /**
     * This class gets Drivers by UUID as string, is used as a test
     */
    public static class GetDriverByNameTaskTest extends AsyncTask<String, Void, Driver> {
        @Override
        protected Driver doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"name\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the name, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
                    .addType("drivertest")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Driver foundDriver = result.getFirstHit(Driver.class).source; //might not work
                    return foundDriver;
                } else {
                    Log.i("Error", "The search query failed to find the driver that matched.");
                }
            } catch (Exception e) {
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch driver server!");
            }
            return null;
        }
    }

    /**
     * This class adds a Driver object, is a test
     */
    public static class AddDriverTaskTest extends AsyncTask<Driver, Void, Void> {
        @Override
        protected Void doInBackground(Driver... drivers) {
            verifySettings();

            for (Driver driver : drivers) {
                Index index = new Index.Builder(driver).index("cmput301f16t04").type("drivertest").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        driver.setElasticID(result.getId());
                    } else {
                        Log.i("Error", "Elastic search was not able to add the driver, as the result did not succeed.");
                    }
                } catch (Exception e) {
                    Log.i(e.toString(), "We failed to add a driver to elastic search, because of an exception!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Deletes a driver in the driver tests, using ElasticID as a parameter
     * @param ID
     */
    public void deleteDriverTests(String ID){
        verifySettings();
        try {
            client.execute(new Delete.Builder(ID)
                    .index("cmput301f16t04")
                    .type("drivertest")
                    .build());
        } catch (Exception e){
            Log.i("ERROR", "Couldn't delete previous driver test objects from elastic search");
        }
    }
}
