package com.dhanuka.morningparcel.Helper;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mr.Mad on 12/12/2014.
 */
public class FusedLocationService implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final long INTERVAL = 1000 * 30;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long ONE_MIN = 1000 * 60;
    private static final long REFRESH_TIME = ONE_MIN * 5;
    private static final float MINIMUM_ACCURACY = 50.0f;
    AppCompatActivity locationActivity;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location location;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private LocationGetCallbacks listner;

    public FusedLocationService(AppCompatActivity locationActivity, LocationGetCallbacks listner) {
        this.listner = listner;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        this.locationActivity = locationActivity;

        googleApiClient = new GoogleApiClient.Builder(locationActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Location currentLocation = fusedLocationProviderApi.getLastLocation(googleApiClient);
        if (currentLocation != null && currentLocation.getTime() > REFRESH_TIME) {
            location = currentLocation;
            listner.onLocationReceived(location.getLatitude(), location.getLongitude());
            Log.e("Locstionss","location receiver fusedlocation service=="+location.getLatitude()+"longitude=="+location.getLongitude());
  /*          PreferenceHelper prefs = new PreferenceHelper(locationActivity);
            prefs.setGPSLocation(location.getLatitude(), location.getLongitude());
  */      } else {
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            // Schedule a Thread to unregister location listeners
            Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    fusedLocationProviderApi.removeLocationUpdates(googleApiClient,
                            FusedLocationService.this);
                }
            }, ONE_MIN, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //if the existing location is empty or
        //the current location accuracy is greater than existing accuracy
        //then store the current location
        if (null == this.location || location.getAccuracy() < this.location.getAccuracy()) {
            this.location = location;
            listner.onLocationReceived(location.getLatitude(), location.getLongitude());
            //if the accuracy is not better, remove all location updates for this listener
            if (this.location.getAccuracy() < MINIMUM_ACCURACY) {
                fusedLocationProviderApi.removeLocationUpdates(googleApiClient, this);
            }
        }
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public interface LocationGetCallbacks {
        void onLocationReceived(double lat, double lng);
    }
}