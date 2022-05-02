package com.example.newsaggregator.Loader;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.NewsSources;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsSourcesLoader {

    private final MainActivity mainActivity;
    private static final String URL = "https://newsapi.org/v2/sources";         //url to use
    private static final String APIKey = "API KEY";        //key

    public NewsSourcesLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static void getdata(MainActivity mainActivity) {
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("apiKey", APIKey);
        String urlToUse = buildURL.build().toString();          //final url by adding key and location

        Response.Listener<JSONObject> listener =
                response -> handleResults(mainActivity, response.toString());

        Response.ErrorListener error =
                error1 ->  handleResults(mainActivity, null);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET,
                        urlToUse,
                        null,
                        listener,
                        error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers; }
        };
        queue.add(jsonObjectRequest);
    }

    private static void handleResults(MainActivity mainActivity, String s) {
        if (s == null) {
            return;
        }
        final ArrayList<NewsSources> newslist = parseJSON(s);
        if (newslist == null)
            mainActivity.showToast("Error in loading data");
        mainActivity.updateData(newslist);
    }

    private static ArrayList<NewsSources> parseJSON(String s) {
        ArrayList<NewsSources> newssourceslist = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray newssources = jObjMain.getJSONArray("sources");

            for (int i = 0; i < newssources.length(); i++) {
                JSONObject src = (JSONObject) newssources.get(i);

                String id = src.has("id") ? src.getString("id") : null;
                String name = src.has("name") ? src.getString("name") : null;
                String category = src.has("category") ? src.getString("category") : null;

                newssourceslist.add(new NewsSources(id,name,category));
            }
            return newssourceslist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
