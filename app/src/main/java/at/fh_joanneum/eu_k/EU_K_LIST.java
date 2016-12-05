package at.fh_joanneum.eu_k;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EU_K_LIST extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eu__k__list);

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
                /*try{
                    EU_K_MAIN.listLocation = true;
                    EU_K_MAIN.listLatLng = new LatLng(innerHelper.get(position).getDouble("lat"),innerHelper.get(position).getDouble("long"));
                    EU_K_MAIN.listMarker = innerHelper.get(position).getString("location") + ", " + innerHelper.get(position).getString("street") + ", " + innerHelper.get(position).getString("object");
                    startActivity(i);
                }catch (JSONException e){
                    e.printStackTrace();
                }*/

            }
        });

    }
}
