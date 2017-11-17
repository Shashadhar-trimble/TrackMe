package hackathon.trimble.trackme;


//region import directives

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.util.Date;

import hackathon.trimble.trackme.model.Locator;
import hackathon.trimble.trackme.net.NetworkException;
import hackathon.trimble.trackme.net.NetworkResponse;
import hackathon.trimble.trackme.net.VolleyJavaCommunicator;

import static hackathon.trimble.trackme.TrackMeApplication.ID;

//endregion import directives

/**
 * Navigation drawer activity , acts as main activity
 * Created by Shashadhar on 17-Nov-2019
 */

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ScanQRCodeFragment.OnFragmentInteractionListener {

    //region constants

    private static final String TAG = NavDrawerActivity.class.getSimpleName();
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    //endregion constants


    //region members

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates = false;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    /**
     * Stores current location
     */
    private Location mCurrentLocation;

    /**
     * Fragment , used to store the reference of fragment inflated
     */
    private android.support.v4.app.Fragment fragment;

    /**
     * VolleyJavaCommunicator instance used for server api calls
     */
    private VolleyJavaCommunicator communicator = new VolleyJavaCommunicator(this);

    //endregion members


    //region lifecycle overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_nav_drawer);
            Toolbar toolbar =findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            findViewById(R.id.toolbar_title_text).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.toolbar_title_text)).setText("TrackMe");

            //Location related setup
            mRequestingLocationUpdates = false;
            mLastUpdateTime = "";
            setUpLocationRequest();

            //selecting the first menu default
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        try {
            int id = item.getItemId();

            if (id == R.id.nav_scan) {
                fragment = ScanQRCodeFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentLayout, fragment).commit();
                findViewById(R.id.toolbar_title_text).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.toolbar_title_text)).setText("Scan QR Code");

            } else if (id == R.id.nav_manage) {
                fragment = OrderDetailsFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentLayout, fragment).commit();
                findViewById(R.id.toolbar_title_text).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.toolbar_title_text)).setText("Order Details");
            } else if (id == R.id.nav_send) {
                fragment = AboutUsFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentLayout, fragment).commit();
                findViewById(R.id.toolbar_title_text).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.toolbar_title_text)).setText("About Us");
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove location updates to save battery.
        stopLocationUpdates();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        shouldStartLocationUpdates();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            switch (requestCode) {
                // Check for the integer request code originally supplied to startResolutionForResult().
                case REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            Log.i(TAG, "User agreed to make required location settings changes.");
                            break;
                        case Activity.RESULT_CANCELED:
                            Log.i(TAG, "User chose not to make required location settings changes.");
                            mRequestingLocationUpdates = false;
                            break;
                    }
                    break;

                default: {

                    //result of QR code scanning
                    String QRCodeResult;
                    super.onActivityResult(requestCode, resultCode, data);
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (resultCode == RESULT_OK && result != null) {
                        QRCodeResult = result.getContents();
                        if (result.getContents() != null) {
                            Log.e("Scan QR code", result.getContents());
                            if (fragment instanceof ScanQRCodeFragment) {
                                ((ScanQRCodeFragment) fragment).setContent(QRCodeResult);
                                //stores the id
                                ID = QRCodeResult;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region lifecycle overrides

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionStateCamera = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        return permissionState == PackageManager.PERMISSION_GRANTED && permissionStateCamera == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(NavDrawerActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(NavDrawerActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    public void setUpLocationRequest() {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mSettingsClient = LocationServices.getSettingsClient(this);

            // Kick off the process of building the LocationCallback, LocationRequest, and
            // LocationSettingsRequest objects.
            createLocationCallback();
            createLocationRequest();
            buildLocationSettingsRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLocationRequest() {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                try {
                    super.onLocationResult(locationResult);

                    mCurrentLocation = locationResult.getLastLocation();
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    Log.d(TAG, "Location call back" + mCurrentLocation.toString());
                    updateLocationUpdateToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void startPublishing() {
        mRequestingLocationUpdates = true;
        shouldStartLocationUpdates();
    }

    //Start publishing
    private void updateLocationUpdateToServer() {

        Locator locator = new Locator();
        locator.setLon(mCurrentLocation.getLongitude());
        locator.setLat(mCurrentLocation.getLatitude());
        locator.setTimestamp(mLastUpdateTime);
        communicator.publishData(ID, locator, new NetworkResponse.Listener() {
            @Override
            public void onResponse(Object result) {
                Log.d(TAG, "Location published to server successfully");
            }
        }, new NetworkResponse.ErrorListener() {
            @Override
            public void onError(NetworkException error, boolean isSessionInvalid) {
                Log.d(TAG, "Error in publishing location update to server");
            }
        }, "test");

    }


    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void shouldStartLocationUpdates() {
        try {
            if (mRequestingLocationUpdates && (checkPermissions())) {
                startLocationUpdates();
            } else if (!checkPermissions()) {
                requestPermissions();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                        //setButtonsEnabledState();
                    }
                });
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        try {
                            //noinspection MissingPermission
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(NavDrawerActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(NavDrawerActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        // updateUI();
                    }
                });
    }


}
