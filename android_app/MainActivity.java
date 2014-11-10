package com.scottallencambo.crowd_cheer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.parse.Parse;


/**
 * TODO: figure out why this code works and the one that uses locationClient does not
 * http://stackoverflow.com/questions/18916273/locationclient-vs-locationmanager
 * TODO: update UI with speed and location, allow user to stop service using the button
 * TODO: convert mps to mph
 */

public class MainActivity extends FragmentActivity{

    public static final String START_LOCATING = "START_LOCATING";
    LocationRequest mLocationRequest;
    LocationClient mLocationClient;
    Location mLocation;
    boolean mUpdatesRequested;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    TextView status;
    TextView speedView;

    Float speed;
    String lat;
    String lon;

    Long lastRecordedTime;
    String mPhoneNumber;
    Intent mServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("location", "onCreate()");
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "QXRTROGsVaRn4a3kw4gaFnHGNOsZxXoZ8ULxwZmf", "gINJkaTkxsafobZ0QFZ0HAT32tjdx06aoF6b2VNQ");
        setContentView(R.layout.activity_main);
        status = (TextView)findViewById(R.id.status);
        speedView = (TextView)findViewById(R.id.speedView);
        //status.setVisibility(TextView.VISIBLE);

        // Set the fastest update interval to 1 second
        // not sure that we need to limit the fastest interval
        //mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        // Open the shared preferences
        mPrefs = getSharedPreferences("SharedPreferences",
                Context.MODE_PRIVATE);
        // Get a SharedPreferences editor
        mEditor = mPrefs.edit();
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();
        Log.d("location", "mPhoneNumber == " + mPhoneNumber);
        mUpdatesRequested = false;
        mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
        mEditor.putString("DEVICE_PHONE", mPhoneNumber);
        mEditor.commit();



        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter("LOC_UPDATE");

        // Instantiates a new DownloadStateReceiver
        LocationReceiver mLocationReceiver =
                new LocationReceiver();
        // Registers the LocationReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mLocationReceiver,
                mStatusIntentFilter);

        //setup intent for LocationService
        mServiceIntent = new Intent(this.getApplicationContext(), LocationIntentService.class);
        mServiceIntent.setAction(START_LOCATING);

    }

    public Boolean toggleLocating(){
        if (mUpdatesRequested){ // turning off locating
            mUpdatesRequested = false;
            stopService(mServiceIntent);
        } else { // turning on locating
            mUpdatesRequested = true;
            startService(mServiceIntent);
        }
        mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
        mEditor.commit();
        return mUpdatesRequested;
    }

    public class LocationReceiver extends BroadcastReceiver {
        public LocationReceiver() {
            Log.d("location", "LocationReceiver() called");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("location", "Location Receiver got intent!");
            status.setVisibility(TextView.VISIBLE);
            //status.setText("Reciever done!");
            if (intent.hasExtra("START_SUCCESS")){
                Boolean successful = intent.getBooleanExtra("START_SUCCESS", false);
                if (successful){
                    status.setText("Success! Get out there and run!");
                }
            }

            if (intent.hasExtra("LAT") && intent.hasExtra("LON")){
                Log.d("location", "UI recieved lat, lon");
                String lat = intent.getStringExtra("LAT");
                String lon = intent.getStringExtra("LON");

                status.setText(lat + "," + lon);

                if (intent.hasExtra("SPEED") && intent.hasExtra("MPH")){
                    Log.d("location", "UI Recieved speed");
                    String speed = intent.getStringExtra("SPEED");
                    String mph = Double.toString(intent.getDoubleExtra("MPH", 0));
                    speedView.setVisibility(TextView.VISIBLE);
                    speedView.setText(speed + " m/s , " + mph);

                }
            }
        }
    }

    @Override
    protected void onPause() {
        // Save the current setting for updates
        Log.d("location", "onPause()");
        mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
        mEditor.commit();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("location", "onStart()");
        //mLocationClient.connect();
    }

    @Override
    protected void onResume() {
        /*
         * Get any previous setting for location updates
         * Gets "false" if an error occurs
         */
        super.onResume();
        Log.d("location", "onResume()");
        if (mPrefs.contains("KEY_UPDATES_ON")) {
            Log.d("location", "Updates were set");
            mUpdatesRequested =
                    mPrefs.getBoolean("KEY_UPDATES_ON", false);

            // Otherwise, turn off location updates
        } else {
            Log.d("location", "Updates_ON was not set");
            mEditor.putBoolean("KEY_UPDATES_ON", false);
            mEditor.commit();
        }

        if (mPrefs.contains("DEVICE_PHONE")) {
            Log.d("location", "Phone were set");
            mPhoneNumber = mPrefs.getString("DEVICE_PHONE", "not set");

            // Otherwise, turn off location updates
        } else {
            Log.d("location", "Updates_ON was not set");
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            Log.d("location", "mPhoneNumber == " + mPhoneNumber);
            mEditor.putString("DEVICE_PHONE", mPhoneNumber);
            mEditor.commit();
        }
    }

    /*
    * Called when the Activity is no longer visible at all.
    * Stop updates and disconnect.
    */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("location", "onStop()");
        //stopService(mServiceIntent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonOnClick(View v){
        Button button = (Button) v;
        if (toggleLocating()){
            button.setText("Stop Crowd Cheer");
        } else {
            button.setText("Start Crowd Cheer");
        }
    }

}
