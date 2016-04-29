package com.svadhera.friendlocator.friendlocator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by svadhera on 4/7/2016.
 */
public class JsonHelper {

    protected static double getLat (String jsonString) {
        return extract(jsonString, "lat");
    }

    protected static double getLng (String jsonString) {
        return extract(jsonString, "lng");
    }

    protected static String getId (String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            return jsonObj.getString("id");
        } catch (JSONException jse) {
            System.out.println(jse.getCause() + jse.getMessage());
            return "error";
        }
    }

    private static double extract (String jsonString, String token) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONObject coordinates = jsonObj.getJSONObject("coordinate");
            return coordinates.getDouble(token);
        } catch (JSONException jse) {
            System.out.println(jse.getCause() + jse.getMessage());
            return -999;
        }
    }
}
