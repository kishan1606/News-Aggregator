package com.example.newsaggregator.Loader;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.News.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class NewsLoader {

    private final MainActivity mainActivity;
    private static String source;
    private static final String URL = "https://newsapi.org/v2/top-headlines";       //url to use
    private static final String APIKey = "API KEY";        //Key

    public NewsLoader(MainActivity mainActivity) {

        this.mainActivity = mainActivity;
    }
    public static void setSource(String l) {
        //Setting location for finding using url
        source = l;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getdata(MainActivity mainActivity) {
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("sources", source);
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
                        return headers;
                    }
                };
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void handleResults(MainActivity mainActivity, String s) {
        if (s == null) {
            return;
        }
        final ArrayList<News> newslist = parseJSON(s);
        if (newslist == null)
            mainActivity.showToast("Error in loading data");
        mainActivity.updatenewsData(newslist);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static ArrayList<News> parseJSON(String s) {
        ArrayList<News> newslist = new ArrayList<>();
        int c = 1;
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray newsarticles = jObjMain.getJSONArray("articles");

            for (int i = 0; i < newsarticles.length(); i++) {
                JSONObject art = (JSONObject) newsarticles.get(i);

                String title = art.has("title") ? art.getString("title") : null;
                String date = art.has("publishedAt") ? dtFormatter(art.getString("publishedAt")) : null;
                String author = art.has("author") ? art.getString("author") : null;
                String imgurl = art.has("urlToImage") ? art.getString("urlToImage") : null;
                String description = art.has("description") ? art.getString("description") : null;
                String url = art.has("url") ? art.getString("url") : null;

                newslist.add(new News(title,date, author, imgurl, description,url, c++));
            }
            return newslist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String dtFormatter(String date){          //date formatter from zulu time
            String s = "MMM dd, yyyy HH:mm";        //required format
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(s, Locale.ENGLISH);
            return ZonedDateTime.parse(date).format(dateTimeFormatter);
    }
}
