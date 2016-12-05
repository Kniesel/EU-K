package at.fh_joanneum.eu_k;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class EU_K_MAIN extends AppCompatActivity implements LocationListener {

    static LatLng myLatLng = null;
    static boolean ownLocation = false;

    static LatLng cityLatLng = null;
    static String citySearched = null;
    static boolean citySearch = false;

    /*static LatLng listLatLng = null;
    static boolean listLocation = false;
    static String listMarker = null;*/



    Button helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_eu__k__main);

        helper = (Button)findViewById(R.id.btn_ownLocation);
        helper.setEnabled(false);
        LocationManager locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickMyLocation(View v){
        ownLocation = true;

        Intent i = new Intent(this, EU_K_MAP.class);
        startActivity(i);
    }

    public void onClickSendCity(View v) throws IOException {

        String givenCity = ((EditText)findViewById(R.id.tf_City)).getText().toString();
            if (givenCity.isEmpty()){
                String msg = this.getResources().getString(R.string.emptyField);
                Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                return;
            }
        citySearch = true;

        Geocoder geocoder = new Geocoder(this);

        List<Address> myList = geocoder.getFromLocationName(givenCity, 1);

        Log.i("DEBUG LOCATION",myList.get(0).toString());

        cityLatLng = new LatLng(myList.get(0).getLatitude(), myList.get(0).getLongitude());
        Log.i("DEBUG LOCATION",cityLatLng.toString());
        citySearched = myList.get(0).getCountryName()+", " + myList.get(0).getLocality();


        Intent i = new Intent(this, EU_K_MAP.class);
        startActivity(i);
    }

    public void onClickShowAll(View v){
        Intent i = new Intent(this, EU_K_MAPEDLIST.class);
        startActivity(i);
    }


    @Override
    public void onLocationChanged(Location location) {
        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (myLatLng != null){
            helper.setEnabled(true);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
