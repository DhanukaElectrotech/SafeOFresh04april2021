package com.dhanuka.morningparcel.Controllers;


//
//public class WifiConnect extends AppCompatActivity {
//    WifiManager wifiManager;
//    int a = 1;
//    int networkId;
//
//    String mySSID = "";
//    String myPWD = "";
//    TextView txtMsg;
//    ProgressDialog progressDialog;
//
//
//    private TextView tvTitle;
//
//    private Toolbar mToolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        setContentView(R.layout.activity_wifi_connect);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        txtMsg = (TextView) findViewById(R.id.txtMsg);
//        tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
//        tvTitle.setText(R.string.app_name);
//
//        Bundle b = getIntent().getExtras();
//        if (b != null) {
//            mySSID = getIntent().getStringExtra("ssid");
//            myPWD = getIntent().getStringExtra("pwds");
//
//            Log.e("MYSSID", mySSID + "\n" + myPWD);
//        } else {
//            Toast.makeText(WifiConnect.this, "SSID is not received.", Toast.LENGTH_SHORT).show();
//
//            finish();
//        }
//
//
//        totalPermission = new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
//                Manifest.permission.CHANGE_WIFI_MULTICAST_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//
//        PermissionModule permissionModule = new PermissionModule(WifiConnect.this);
//        permissionModule.checkPermissions();
//
//        txtMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onConnect();
//            }
//        });
//        onConnect();
//    }
//
//
//    public void onConnect() {
//
//
//        if (Build.VERSION.SDK_INT >= 28) {
//            if (isMobileDataEnabled()) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(WifiConnect.this);
//                builder.setMessage("Please Turn Off the data connection and try again")
//                        .setCancelable(false)
//                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Connnect();
//                            }
//                        }).setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                })
//                        .create()
//                        .show();
//            } else {
//                a = 0;
//                getAllpermisiion();
//            }
//        } else {
//            a = 0;
//            getAllpermisiion();
//        }
//
//    }
//
//
//    private String[] totalPermission;
//    private static final int PERMISSION_ALL = 200;
//
//    private void getAllpermisiion() {
//        if (!PermissionUtils.hasPermission(this, Manifest.permission.CHANGE_WIFI_STATE)) {
//            ActivityCompat.requestPermissions(this, totalPermission, PERMISSION_ALL);
//            PermissionModule permissionModule = new PermissionModule(WifiConnect.this);
//            permissionModule.checkPermissions();
//
//        } else {
//            if (a == 0) {
//                checkData();
//            }
//        }
//    }
//
//    public void checkData() {
//
//
//        if (CheckGpsStatus()) {
//
//            init();
//        } else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(WifiConnect.this);
//            builder.setMessage("Please Turn ON GPS and try again")
//                    .setCancelable(false)
//                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            checkData();
//                        }
//                    }).setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
//                }
//            })
//                    .create()
//                    .show();
//        }
//    }
//
//
//    public void forgetNetwork(int netId) {
//        if (netId < 0) {
//            return;
//        }
//        wifiManager.removeNetwork(netId);
//    }
//
//    LocationManager locationManager;
//    boolean GpsStatus;
//
//    public boolean CheckGpsStatus() {
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        return GpsStatus;
//    }
//
//    public Boolean isMobileDataEnabled() {
//        Object connectivityService = getSystemService(CONNECTIVITY_SERVICE);
//        ConnectivityManager cm = (ConnectivityManager) connectivityService;
//
//        try {
//            Class<?> c = Class.forName(cm.getClass().getName());
//            Method m = c.getDeclaredMethod("getMobileDataEnabled");
//            m.setAccessible(true);
//            return (Boolean) m.invoke(cm);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    public void init() {
//        try {  //Wifi Arduino Code
//            a = 1;
//            setMobileDataEnabled(WifiConnect.this, false);
//            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//            if (wifiManager.isWifiEnabled()) {
//                int netwrkId = wifiManager.getConnectionInfo().getNetworkId();
//                forgetNetwork(netwrkId);
//            } else {
//                wifiManager.setWifiEnabled(true);
//                int netwrkId = wifiManager.getConnectionInfo().getNetworkId();
//                forgetNetwork(netwrkId);
//
//            }
//
////      WifiConfiguration wc=      ApiCall.connectWifi(ssid ,pwds);
//
//            try {
//                int netId = wifiManager.addNetwork(ApiCall.connectWifi(mySSID, myPWD));                wifiManager.enableNetwork(netId, true);
//                wifiManager.setWifiEnabled(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            txtMsg.setText("Connected Successfully");
//            txtMsg.setTextColor(Color.parseColor("#FF07C130"));
//
//
//            progressDialog = new ProgressDialog(WifiConnect.this);
//
//            progressDialog.setMessage("Connecting...");
//            progressDialog.setTitle(R.string.app_name);
//            progressDialog.show();
//
//            loadHandler();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        if (wifiManager.isWifiEnabled()) {
//            final WifiInfo info = wifiManager.getConnectionInfo();
//
//            //String ssid = info.getSSID();
//            //Log.e("ssidssid", ssid);
//        }
//    }
//
//    public void loadHandler() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //  progressDialog.dismiss();
//                try {
//                    final WifiInfo info = wifiManager.getConnectionInfo();
//                    String ssid = info.getSSID();
//                    networkId = info.getNetworkId();
//
//
//                    if (ssid != null || ssid.contains(mySSID)) {
//
//                        txtMsg.setText("Connected Successfully");
//                        txtMsg.setTextColor(Color.parseColor("#FF07C130"));
//                        sendData();
//                    } else {
//
//
//                        try {
//                            progressDialog.dismiss();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            loadHandler();
//                        }
//
//                        txtMsg.setTextColor(Color.parseColor("#FFFF071F"));
//                        txtMsg.setText("Unable to Connect the Car Wifi, Try Again");
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    loadHandler();
//                }
//            }
//        }, 3000);
//
//    }
//
//    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        try {
//            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            final Class conmanClass = Class.forName(conman.getClass().getName());
//            final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
//            connectivityManagerField.setAccessible(true);
//            final Object connectivityManager = connectivityManagerField.get(conman);
//            final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
//            final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataDisabled", Boolean.TYPE);
//            setMobileDataEnabledMethod.setAccessible(true);
//
//            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendData() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new AsyncGetSlider().execute();
//
//            }
//
//        }, 3000);
//    }
//
//    public void Connnect() {
//
//        if (Build.VERSION.SDK_INT >= 28) {
//            if (isMobileDataEnabled()) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(WifiConnect.this);
//                builder.setMessage("Please Turn Off the data connection and try again")
//                        .setCancelable(false)
//                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Connnect();
//                            }
//                        }).setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                })
//                        .create()
//                        .show();
//            } else {
//
//                new AsyncGetSlider().execute();
//            }
//        } else {
//            new AsyncGetSlider().execute();
//
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Map<String, Integer> perms = new HashMap<String, Integer>();
//        // Initial
//        perms.put(Manifest.permission.ACCESS_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
//        perms.put(Manifest.permission.CHANGE_WIFI_MULTICAST_STATE, PackageManager.PERMISSION_GRANTED);
//        perms.put(Manifest.permission.CHANGE_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
//        for (int i = 0; i < permissions.length; i++)
//            perms.put(permissions[i], grantResults[i]);
//        if (perms.get(android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
//                && perms.get(android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE) == PackageManager.PERMISSION_GRANTED
//                && perms.get(android.Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
//        ) {
//            if (a == 0) {
//                checkData();
//                //    init();
//
//
//            }
//            // All Permissions Granted
//        }
//    }
//
//
//    class AsyncGetSlider extends AsyncTask<Void, Void, Void> {
//
//        private String response;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            try {
//                progressDialog.dismiss();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            progressDialog = new ProgressDialog(WifiConnect.this);
//
//            progressDialog.setMessage("Sending Command");
//            progressDialog.setTitle(R.string.app_name);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            response = ApiCall.httpPostNew();
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
//            if (response != null) {
//                try {
//                    JSONObject array = new JSONObject(response);
//                    JSONObject object = array.getJSONObject("data");
//                    if (object.getString("message").equalsIgnoreCase("success")) {
//
//                        Toast.makeText(WifiConnect.this, "Command Sent Successfully.", Toast.LENGTH_SHORT).show();
//                        txtMsg.setText("Command Sent Successfully");
//                        txtMsg.setTextColor(Color.parseColor("#FF07C130"));
//                        if (wifiManager != null) {
//                            wifiManager.setWifiEnabled(false);
//
//                            forgetNetwork(networkId);
//                            wifiManager.setWifiEnabled(true);
//
//                        }
//                        onBackPressed();
//                        // startActivity(new Intent(WifiConnectActivity.this, MainActivity.class));
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(WifiConnect.this, "Unable to Send Command.", Toast.LENGTH_SHORT).show();
//                    txtMsg.setText("Unable to Send Command, Please try again.");
//                    txtMsg.setTextColor(Color.parseColor("#FF000000"));
//
//                }
//            } else {
//                Toast.makeText(WifiConnect.this, "Unable to Send Command.", Toast.LENGTH_SHORT).show();
//                txtMsg.setText("Unable to Send Command, Please try again.");
//                txtMsg.setTextColor(Color.parseColor("#FF000000"));
//            }
//        }
//    }
//
//
//}