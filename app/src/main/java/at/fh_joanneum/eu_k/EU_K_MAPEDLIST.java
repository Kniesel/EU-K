package at.fh_joanneum.eu_k;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.R.id.list;

public class EU_K_MAPEDLIST extends FragmentActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private Marker helper;
    LatLng listLatLng = null;
    String listMarker = null;
    PolylineOptions lineToDraw;
    Polyline helperForRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maped_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ParseJSON jsonParser = new ParseJSON();
        //Log.i("DEBUG JSON", parseJSON());
        InputStream is;
        try {
            is = getAssets().open("WC.json");
            populateView(jsonParser.handleJson(jsonParser.parseJSON(is)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void populateView(ArrayList<JSONObject> list) {
        final ArrayList<JSONObject> innerHelper = list;
        final Intent i = new Intent(this, EU_K_MAP.class);
        ListView fillList = (ListView)findViewById(R.id.displayListView);

        ListAdapter adapter = new ListAdapter(this,R.layout.eu_k_onlylist,R.id.txtLocation,list);

        fillList.setAdapter(adapter);
        fillList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    if(!(helper == null)){
                        helper.remove();
                    }
                    if(helperForRemove != null){
                        helperForRemove.remove();
                    }

                    listLatLng = new LatLng(innerHelper.get(position).getDouble("lat"),innerHelper.get(position).getDouble("long"));
                    listMarker = innerHelper.get(position).getString("location") + ", " + innerHelper.get(position).getString("street") + ", " + innerHelper.get(position).getString("object") + ", Rating: " + innerHelper.get(position).getInt("rating");
                    markeradd();
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.setInfoWindowAdapter(this);
    }

    public void markeradd(){
        helper = mMap.addMarker(new MarkerOptions().position(listLatLng).title(listMarker));
        helper.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng, 12));
        drawline();
    }

    public void drawline(){
        lineToDraw = new PolylineOptions().add(listLatLng, EU_K_MAIN.myLatLng).width(5).color(Color.BLUE).geodesic(true);

        helperForRemove = mMap.addPolyline(lineToDraw);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.info_window, null);
        String[] aHelper = listMarker.split(", ");
        TextView tVHelper = (TextView) view.findViewById(R.id.txtWindowLocation);
        tVHelper.setText(aHelper[0]);
        tVHelper = (TextView) view.findViewById(R.id.txtWindowStreet);
        tVHelper.setText(aHelper[1]);
        tVHelper = (TextView) view.findViewById(R.id.txtWindowObject);
        tVHelper.setText(aHelper[2]);
        tVHelper = (TextView) view.findViewById(R.id.txtWindowRating);
        tVHelper.setText(aHelper[3]);
        return view;
    }
}
