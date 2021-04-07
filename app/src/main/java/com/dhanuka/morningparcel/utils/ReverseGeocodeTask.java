package com.dhanuka.morningparcel.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.StrictMode;


import com.dhanuka.morningparcel.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class ReverseGeocodeTask extends AsyncTask<Double, Void, String> {
    private final Context context;
    private final ReverseGeocodeCallbacks listner;

    public ReverseGeocodeTask(Context context, ReverseGeocodeCallbacks listner) {
        this.context = context;
        this.listner = listner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Double... doubles) {
        String result = getAddressFromLatLng(doubles[0], doubles[1]);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(null);
        listner.onReverseGeocodeTaskFinished(result);
    }


    protected String getAddressFromLatLng(Double lat, Double lng) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            if (geocoder != null) {
                if (geocoder.isPresent()) {

                    // Create a list to contain the result address
                    List<Address> addresses = null;

                    // Try to get an address for the current location. Catch IO or network problems.
                    try {
                        addresses = geocoder.getFromLocation(lat, lng, 1);

                        // Catch network or other I/O problems.
                    } catch (IOException exception1) {

                        return getAddressFromLatLong(lat, lng);

                        // Catch incorrect latitude or longitude values
                    } catch (IllegalArgumentException exception2) {

                        return getAddressFromLatLong(lat, lng);

                    }
                    // If the reverse geocode returned an address
                    if (addresses != null && addresses.size() > 0) {
                        // Get the first address
                        Address address = addresses.get(0);
                        StringBuilder sb = new StringBuilder();

                        String city = address.getLocality();
                        String  state = address.getAdminArea();
                        String  country = address.getCountryName();

                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            if (i == address.getMaxAddressLineIndex()) {
                                sb.append(address.getAddressLine(i));
                            } else {
                                sb.append(address.getAddressLine(i) + ",");
                            }
                        }


                        // Return the text
                        return sb.toString() + "//" + city + "//" + state + "//" + country;
                        // If there aren't any addresses, post a message
                    } else {
                        return getAddressFromLatLong(lat, lng);
                    }
                } else {
                    return getAddressFromLatLong(lat, lng);
                }
            } else {
                return getAddressFromLatLong(lat, lng);
            }


        } catch (Exception e) {
            return getAddressFromLatLong(lat, lng);
        }


    }

    public String getAddressFromLatLong(Double lat, Double lng) {

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
            sb.append("latlng=" + lat + "," + lng);
            sb.append("&key=" + AppController.URL_API_KEY);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();


            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return "Address not found";
        } catch (IOException e) {
            return "Address not found";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        String str = "Address not found";
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            if (jsonObj.getString("status").equals("ZERO_RESULTS")) {
                return "Address not found";
            }
            str = jsonObj.getJSONArray("results").getJSONObject(0).getString("formatted_address");
        } catch (JSONException e) {
            // Log.e(AppController.TAG, "Cannot process JSON results", e);
        }

        return str;
    }



    public interface ReverseGeocodeCallbacks {
        void onReverseGeocodeTaskFinished(String address);
    }


}
