package emad.whatsnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import emad.whatsnews.Adapters.HomeAdapter;
import emad.whatsnews.Model.Article;
import emad.whatsnews.Model.NewsResponse;
import emad.whatsnews.VolleyUtils.MySingleton;

public class MainActivity extends AppCompatActivity {

    CatLoadingView progessbar;
    Toolbar mToolbar;
    TextView titleText;
    ImageView imgHeaderItem;
    ImageView filterIcon;
    Button changeCategory;
    RecyclerView homeRecyclerView;
    NewsResponse newsResponse;
    HomeAdapter homeAdapter;
    Spinner countrySpinner;
    BottomSheetDialog bottomSheetDialog;
    ArrayAdapter spinnerAdapter;
    ArrayList<Article> articles = new ArrayList<>();
    String currentCategory = "business";
    int currentCategoryIndex = 0;
    int newCategoryIndex = 0;
    String[] categories = {"business","health", "sports", "technology"};
    String[] header = {"https://i.ibb.co/26j8Kyd/bump-collaboration-colleagues-1068523.jpg","https://i.ibb.co/SsqJG0T/care-check-checkup-905874.jpg", "https://i.ibb.co/k6BV1Gw/action-adventure-blue-skies-851095.jpg", "https://i.ibb.co/HKGgngz/alphabets-balloons-blue-background-1426708.jpg"};

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDialog();
        getData("sports");
        Picasso.get().load(header[0]).into(imgHeaderItem);

    }

    public void initViews(){
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,categories);
        progessbar = new CatLoadingView();
        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.ic_more_vert_black_24dp));
        titleText = findViewById(R.id.titleText);
        imgHeaderItem = findViewById(R.id.imgHeaderItem);
        filterIcon = findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });
        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeAdapter = new HomeAdapter(articles, getApplicationContext());
        homeRecyclerView.setNestedScrollingEnabled(false);
        homeRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        homeRecyclerView.setAdapter(homeAdapter);
    }

    public void getData(String category){
        progessbar.show(getSupportFragmentManager(), "");
        String url = "https://newsapi.org/v2/top-headlines?country=eg&category="+category+"&apiKey=" + getString(R.string.apiKey);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        articles.clear();
                        Log.d(TAG, "onResponse: RESPONSE " +response);
                       try {
                        newsResponse = new Gson().fromJson(response,NewsResponse.class);

                        articles = newsResponse.getArticles();
                        homeAdapter = new HomeAdapter(articles, getApplicationContext());
                        homeRecyclerView.setAdapter(homeAdapter);
                        Log.d(TAG, "onResponse:  " + articles.get(0).getTitle());
                        progessbar.dismiss();
                       }catch (Exception ex){
                           Log.d(TAG, "onResponse: " + ex.getMessage());
                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: "+error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInsance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    public String getSharedPreferences() {
//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("StoreData", Context.MODE_PRIVATE);
        String country = sharedPref.getString("country", "eg");
        return country;
    }

    public void addToSharedPreferences() {
//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("StoreData", Context.MODE_PRIVATE);        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("country", 1);
        editor.commit();
        editor.apply();

    }

    public void initDialog(){
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.filter_dialog);
        countrySpinner = bottomSheetDialog.findViewById(R.id.countrySpinner);
        countrySpinner.setAdapter(spinnerAdapter);
        changeCategory = bottomSheetDialog.findViewById(R.id.changeCategory);
    }
    public void filter(){
        countrySpinner.setSelection(currentCategoryIndex);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = categories[position];
                newCategoryIndex = position;
                Log.d(TAG, "onItemSelected:  " + currentCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        changeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: "+ countrySpinner.getSelectedItem());

                if (newCategoryIndex != currentCategoryIndex){
                    currentCategoryIndex =newCategoryIndex;
                    getData(currentCategory);
                    bottomSheetDialog.dismiss();
                    Picasso.get().load(header[currentCategoryIndex]).into(imgHeaderItem);

                }else bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
