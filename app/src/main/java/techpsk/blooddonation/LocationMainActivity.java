package techpsk.blooddonation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LocationMainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_SETTINGS = 1;
    private static final int REQUEST_WIFI_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSettings();
    }

    private boolean locationTrackingEnabled() {
        final LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Determine wifi connectivity.
     *
     * @return boolean indicating wifi connectivity. True for connected.
     */
    private boolean internetConnectivity() {
        final ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connManager.getActiveNetworkInfo();
        return wifi == null ? false : wifi.isConnected();
    }

    private boolean checkSettings() {
        // Is GPS enabled?
        final boolean gpsEnabled = locationTrackingEnabled();
        // Is there internet connectivity?
        final boolean internetConnected = internetConnectivity();

        if (!permissionBoolean()) {
            requestPermission();
            return false;
        } else if (!gpsEnabled) {
            final Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            showDialog(gpsIntent, REQUEST_LOCATION_SETTINGS, getString(R.string.location_tracking_off));
            return false;
        } else if (!internetConnected) {
            final Intent internetIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            showDialog(internetIntent, REQUEST_WIFI_SETTINGS, getString(R.string.wireless_off));
            return false;
        }
        return true;
    }
   public void nearby(View view)
   {  if(!checkSettings())
       return;

       Intent I = new Intent(this, MapsActivity.class);
       startActivity(I);

   }
    private void showDialog(final Intent intent, final int requestCode, final String message) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                //
                startActivityForResult(intent, requestCode);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                finish();
            }
        });
        alertDialog.create().show();
    }
    public boolean permissionBoolean() {
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET};

        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public void requestPermission() {
        //Requesting permissions
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET};

        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        PERMISSIONS,
                        1);
            }
        }


    }



}