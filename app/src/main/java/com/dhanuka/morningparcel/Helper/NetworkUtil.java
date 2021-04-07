/**
 * @author Jenish Khanpara
 */

package com.dhanuka.morningparcel.Helper;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}
	
	/**
	 * Check... Is phone connected to internet
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectedToNetwork(Context context) {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			isConnected = networkInfo.isConnected();
		}

		return isConnected;
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = NetworkUtil.getConnectivityStatus(context);
		String status = null;
		if (conn == NetworkUtil.TYPE_WIFI) {
			status = "Wifi enabled";
		} else if (conn == NetworkUtil.TYPE_MOBILE) {
			status = "Mobile data enabled";
		} else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
		}
		return status;
	}


    public static boolean getWifiStatus(Context context){
        int conn = NetworkUtil.getConnectivityStatus(context);

        if (conn == NetworkUtil.TYPE_WIFI) {
            return true;
        }
        return  false;
    }

    public static boolean getMobileDataStatus(Context context){
        int conn = NetworkUtil.getConnectivityStatus(context);

        if (conn == NetworkUtil.TYPE_MOBILE) {
            return true;
        }
        return  false;
    }

    public static boolean getGPSStatus(Context context){
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return  statusOfGPS;
    }

}