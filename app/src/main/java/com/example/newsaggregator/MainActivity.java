package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newsaggregator.News.News;
import com.example.newsaggregator.Loader.NewsLoader;
import com.example.newsaggregator.Loader.NewsSourcesLoader;
import com.example.newsaggregator.News.NewsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<String> newslist = new ArrayList<>();
    private ArrayAdapter<NewsSources> arrayAdapter;
    private ArrayList<News> newsList;
    ArrayList<SpannableString> as = new ArrayList<SpannableString>();
    ArrayList<SpannableString> as123 = new ArrayList<SpannableString>();
    private final HashMap<String, ArrayList<String>> newstype = new HashMap<>();
    private final HashMap<String, String> idhm = new HashMap<>();
    private final HashMap<String, Integer> clr = new HashMap<>();
    private Menu opt_menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager2 viewPager2;
    private NewsAdapter newsAdapter;

    String c;
    private int[] colorarray;

//    public MainActivity() {
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.news_vp);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);
        colorarray = getResources().getIntArray(R.array.colorArray);

        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    SpannableString cc = as.get(position);
                    c = String.valueOf(cc);
                    String clkid = idhm.get(c);
                    //showToast(clkid);
                    NewsLoader newsLoader = new NewsLoader(this);
                    newsLoader.setSource(clkid);
                    newsLoader.getdata(this);           //load news data of selected source
                    mDrawerLayout.closeDrawer(mDrawerList);
                    viewPager2.setBackground(null);
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        );

        if (newstype.isEmpty()) {
            NewsSourcesLoader newsSourcesLoader = new NewsSourcesLoader(this);
            newsSourcesLoader.getdata(this);
        }
    }

    public void updateData(ArrayList<NewsSources> list) {
        ArrayList<String> cilist = new ArrayList<>();
        int k = 0;

        for (NewsSources c : list) {            //make hashmap of categories to their respective source list
            cilist.add(c.getName());
            if (!newstype.containsKey(c.getCategory())) {
                newstype.put(c.getCategory(), new ArrayList<>());
            }
            ArrayList<String> clist = newstype.get(c.getCategory());
            if (clist != null) {
                clist.add(c.getName());
            }
            if (!idhm.containsKey(c.getName())) {
                idhm.put(c.getName(),c.getId());
            }
        }

        setTitle("News gateway ("+list.size()+")");

        newstype.put("All", cilist);

        ArrayList<String> tempList = new ArrayList<>(newstype.keySet());
        Collections.sort(tempList);
        opt_menu.add(tempList.get(0));
        for (int i = 1; i <= tempList.size()-1; i++){           //make hashmap of categories to their respective colour
            String s = tempList.get(i);
            SpannableString spannableString = new SpannableString(s);
            spannableString.setSpan(new ForegroundColorSpan(colorarray[k]),0,spannableString.length(),0);
            opt_menu.add(spannableString);
            clr.put(s, colorarray[k++]);
        }

        //newslist.addAll(cilist);
        for (NewsSources c : list) {
            if(clr.containsKey(c.getCategory())){           //use colour hashmap to insert sources to drawer with respective category colour
                int a = clr.get(c.getCategory());
                SpannableString spannableString = new SpannableString(c.getName());
                spannableString.setSpan(new ForegroundColorSpan(a),0,c.getName().length(),0);
                as.add(spannableString);
            }
        }
        for(SpannableString s : as){
            as123.add(s);
        }
        arrayAdapter = new ArrayAdapter(this, R.layout.drawer_item, as);
        mDrawerList.setAdapter(arrayAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void updatenewsData(ArrayList<News> newsList) {
        this.newsList = newsList;
        setTitle(c);
        newsAdapter = new NewsAdapter(newsList,this);
        viewPager2.setAdapter(newsAdapter);
        viewPager2.setOrientation(viewPager2.ORIENTATION_HORIZONTAL);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        String itm = String.valueOf(item);
        as.clear();
        if(itm.equals("All")){
            as.addAll(as123);
        }
        else{
            ArrayList<String> clist = newstype.get(item.getTitle().toString());
            for(int i = 0; i< clist.size(); i++){
                String m = clist.get(i);
                int a = clr.get(itm);
                SpannableString spannableString = new SpannableString(m);
                spannableString.setSpan(new ForegroundColorSpan(a),0,m.length(),0);
                as.add(spannableString);
            }
        }
        setTitle(item.getTitle()+" ("+as.size()+")");
        arrayAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    public void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

        @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}