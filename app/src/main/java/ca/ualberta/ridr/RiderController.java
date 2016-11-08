package ca.ualberta.ridr;

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
public class RiderController {
    private static JestDroidClient client;

    /**
     *  This class gets Riders by name string
     */
    public static class GetRiderByNameTask extends AsyncTask<String, Void, Rider> {
        @Override
        protected Rider doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"name\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the name, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
                    .addType("rider")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Rider foundRider = result.getFirstHit(Rider.class).source; //might not work
                    return foundRider;
                }
                else {
                    Log.i("Error", "The search query failed to find the rider that matched.");
                }
            }
            catch (Exception e) {
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch rider server!");
            }

            return null;
        }
    }

    /**
     * Gets rider by UUID string
     */
    public static class GetRiderByUUIDTask extends AsyncTask<String, Void, Rider> {
        @Override
        protected Rider doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"id\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the uuid, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
                    .addType("rider")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Rider foundRider = result.getFirstHit(Rider.class).source; //might not work
                    return foundRider;
                } else {
                    Log.i("Error", "The search query failed to find the rider that matched.");
                }
            } catch (Exception e) {
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch rider server!");
            }

            return null;
        }
    }


    /**
     * this class adds a Rider object
     */
    public static class AddRiderTask extends AsyncTask<Rider, Void, Void> {

        @Override
        protected Void doInBackground(Rider... riders) {
            verifySettings();

            for (Rider rider: riders) {
                Index index = new Index.Builder(rider).index("cmput301f16t04").type("rider").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        rider.setElasticID(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the rider, as the result did not succeed.");
                    }
                }
                catch (Exception e) {
                    Log.i(e.toString(), "We failed to add a rider to elastic search, because of an exception!");
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
     *  This class gets Riders by name string, is a test
     */
    public static class GetRiderByNameTaskTest extends AsyncTask<String, Void, Rider> {
        @Override
        protected Rider doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"name\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the name, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
                    .addType("ridertest")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Rider foundRider = result.getFirstHit(Rider.class).source; //might not work
                    return foundRider;
                } else {
                    Log.i("Error", "The search query failed to find the rider that matched.");
                }
            } catch (Exception e) {
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch rider server!");
            }

            return null;
        }
    }

    /**
     *  This class gets Riders by UUID string, is a test
     */
    public static class GetRiderByUUIDTaskTest extends AsyncTask<String, Void, Rider> {
        @Override
        protected Rider doInBackground(String... search_parameters) {
            verifySettings();

            String search_string = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"id\":\"" + search_parameters[0] + "\"}}}}}";
            //search string should work, is searching for the uuid, only returns 1 result

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t04")
                    .addType("ridertest")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Rider foundRider = result.getFirstHit(Rider.class).source; //might not work
                    return foundRider;
                } else {
                    Log.i("Error", "The search query failed to find the rider that matched.");
                }
            } catch (Exception e) {
                Log.i(e.toString(), "Something went wrong when we tried to communicate with the elasticsearch rider server!");
            }

            return null;
        }
    }

    /**
     * this class adds a Rider object
     */
    public static class AddRiderTaskTest extends AsyncTask<Rider, Void, Void> {
        @Override
        protected Void doInBackground(Rider... riders) {
            verifySettings();

            for (Rider rider : riders) {
                Index index = new Index.Builder(rider).index("cmput301f16t04").type("ridertest").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        rider.setElasticID(result.getId());
                    } else {
                        Log.i("Error", "Elastic search was not able to add the rider, as the result did not succeed.");
                    }
                } catch (Exception e) {
                    Log.i(e.toString(), "We failed to add a rider to elastic search, because of an exception!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Deletes a rider in the rider tests, using ElasticID as a parameter
     * @param ID
     */
    public void deleteRiderTests(String ID){
        verifySettings();
        try {
            client.execute(new Delete.Builder(ID)
                    .index("cmput301f16t04")
                    .type("ridertest")
                    .build());
        } catch (Exception e){
            Log.i("ERROR", "Couldn't delete previous rider test objects from elastic search");
        }
    }
}
