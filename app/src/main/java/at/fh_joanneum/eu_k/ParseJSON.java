package at.fh_joanneum.eu_k;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by User on 02.12.2016.
 */

public class ParseJSON {
    public ArrayList<JSONObject> handleJson(String jsonString) {
        ArrayList<JSONObject> aList=new ArrayList<JSONObject>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray ja = jsonObject.getJSONArray("restroom");

            if (ja != null) {
                for (int i = 0; i < ja.length(); i++) {
                    aList.add(ja.getJSONObject(i));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  aList;
    }

    public String parseJSON(InputStream inStream){
        String json = null;
        try {

            int size = inStream.available();
            byte[] buffer = new byte[size];
            inStream.read(buffer);
            inStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
