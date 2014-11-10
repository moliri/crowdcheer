package com.scottallencambo.crowd_cheer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class LocationIntentService extends Service implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 1;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    // Define an object that holds accuracy and frequency parameters

    public static final String START_LOCATING = "START_LOCATING";
    private static final String START_URL = "http://crowdcheer.herokuapp.com/";
    public static final String LOCATION_ACTION = "LOC_UPDATE";
    LocationRequest mLocationRequest;
    LocationClient mLocationClient;
    Location mLocation;
    boolean mUpdatesRequested;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    Float speed;
    String lat;
    String lon;

    Long lastRecordedTime;
    String mPhoneNumber;
    Intent mServiceIntent;

    Intent mIntent;

    CCListener listener;



    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("location", "LocationIntentService.onCreate() called");
        mIntent = new Intent(LOCATION_ACTION);
            /*
     * Creates a new Intent containing a Uri object
     * BROADCAST_ACTION is a custom Intent action
     */
        Intent localIntent =
                new Intent("LOC_UPDATE")
                        // Puts the status into the Intent
                        .putExtra("START_SUCCESS", true);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            Log.d("heroku", "making request to heroku server");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                    Log.d("heroku", "response from heroku server: " + responseString);
                } else{
                    //Closes the connection.
                    Log.d("heroku", "response status from heroku was NOT OK : " + statusLine.getStatusCode());
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            if (MainActivity.START_LOCATING.equals(action)) {
                //let server know
                new RequestTask().execute(START_URL);
                mLocationRequest = LocationRequest.create();
                // Use high accuracy
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                // Set the update interval to 5 seconds
                mLocationRequest.setInterval(UPDATE_INTERVAL);


                mLocationClient = new LocationClient(this, this, this);
                // Start with updates turned on
                mUpdatesRequested = true;
                //Intent localIntent =
                //      new Intent("UPDATE_LATLON")
                //            // Puts the status into the Intent
                //          .putExtra("LATLON", "666, 666");

                // Broadcasts the Intent to receivers in this app.
                //LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
                Log.d("location", "Do The Thing");
                mLocationClient.connect();
            } else {
                Log.d("location", "no action set");
                //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                //final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                //handleActionBaz(param1, param2);
            }


        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.d("location", "LocationIntentService.onDestroy()");
        // If the client is connected
        ParseObject speedObject = new ParseObject("Speed");
        speedObject.put("status", "stopped");
        speedObject.saveInBackground();
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
            mLocationClient.removeLocationUpdates(listener);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
        super.onDestroy();
    }

    public class CCListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            Log.d("location", "Location changed");
            handleLocation(loc);
        }
    }



    /////////   GOOGLE PLAY SERVICES STUFF ////////////



    private boolean servicesConnected() {
        // Check that Google Play services is available
        Log.d("location", "servicesConnected()");
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            Log.d("location", "Problem with google play services");
            return false;
        }
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Log.d("location","Disconnected. Please re-connect.");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d("location", "onConnectionFailed() called");
        if (connectionResult.hasResolution()) {
            Log.d("location", "google play services error might have a resolution");
        } else {

            //commented the following out because it is not something that exists
            // have no idea why the android documentation for this is convinced
            // that it has been "pre-defined"
            //showErrorDialog(connectionResult.getErrorCode());
            Log.d("location", "Error : " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Log.d("location","Connected");
        // If already requested, start periodic updates
        if (mUpdatesRequested) {
            listener = new CCListener();
            Log.d("location", "mUpdatesRequested == true");
            mLocationClient.requestLocationUpdates(mLocationRequest, listener);
            mLocation = mLocationClient.getLastLocation();
            handleLocation(mLocation);
        } else {
            Log.d("location", "mUpdatesRequested == false");
        }
    }


    public void handleLocation(Location location){
        // Report to the UI that the location was updated
        Log.d("location", "handleLocation");
        mLocation = location;
        lat = Double.toString(location.getLatitude());
        lon = Double.toString(location.getLongitude());
        lastRecordedTime = System.currentTimeMillis();

        String msg = "Updated Location: " + lat + "," + lon;
        Log.d("location", msg);
        ParseObject speedObject = new ParseObject("Speed");



        speedObject.put("time", lastRecordedTime);
        speedObject.put("lat", lat);
        speedObject.put("lon", lon);

        Intent localIntent =
                new Intent("LOC_UPDATE");
        // Puts the status into the Intent
        localIntent.putExtra("LAT", lat);
        localIntent.putExtra("LON", lon);
        speedObject.put("status", "running");
        if (mLocation.hasSpeed()) {
            Log.d("location", "has speed");
            speed = mLocation.getSpeed();
            Double mph = msec_to_mph(speed);
            speedObject.put("speed", speed);

            localIntent.putExtra("SPEED", Float.toString(speed));
            localIntent.putExtra("MPH", mph);
            speedObject.put("mph", mph);
        } else {
            Log.d("location", "has no speed");
        }

        speedObject.saveInBackground();
        /**
        Intent localIntent =
                new Intent("UPDATE_LATLON")
                        // Puts the status into the Intent
                        .putExtra("LATLON", lat + "," + lon);

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        */


        sendBroadcast(mIntent);

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public Double msec_to_mph(Float msec){
        Double metersInMile = 0.000621371;
        return msec * metersInMile * 3600;
    }

}
