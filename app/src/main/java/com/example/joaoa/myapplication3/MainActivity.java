package com.example.joaoa.myapplication3;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<Movie> lstMovie;
    RecyclerView myrv;
    RecycleViewAdapter myAdapter;
    Context present=this;
    OrientationEventListener mOrientationListener;
    MainActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        lstMovie=new ArrayList<>();

        new DataLoader().execute();

        activity=this;

        myrv=(RecyclerView) findViewById(R.id.recyclerView);


    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("tag", "config changed");
        super.onConfigurationChanged(newConfig);

        final Toast toast;

        int orientation = newConfig.orientation;
        toast = Toast.makeText(activity, ""+orientation, Toast.LENGTH_LONG);
       /* if (orientation == Configuration.ORIENTATION_PORTRAIT)
            toast = Toast.makeText(activity, "Vertical", Toast.LENGTH_LONG);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            toast = Toast.makeText(activity, "Horizontal", Toast.LENGTH_LONG);
        else
            toast = Toast.makeText(activity, "Example Toast", Toast.LENGTH_LONG);
*/
        toast.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

    protected void onUpdate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        lstMovie=new ArrayList<>();

        new DataLoader().execute();


        myrv=(RecyclerView) findViewById(R.id.recyclerView);

    }

    class DataLoader extends AsyncTask<Void, Void, String> {

        private Exception exception;

        private String API_KEY="b84f68ff07bfbb69c9b82d27f4b7d0b5";

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY+"&language=en-US&page=1");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }


            try {
                JSONObject result =  (JSONObject) new JSONTokener(response).nextValue();
                JSONArray list=result.getJSONArray("results");

                for (int i=0; i<list.length();i++){

                    String url=list.getJSONObject(i).getString("poster_path");
                    if(url!=null){
                        String poster_url = "http://image.tmdb.org/t/p/w185/"+url;
                        int id=list.getJSONObject(i).getInt("id");
                        String title=list.getJSONObject(i).getString("title");
                        String description=list.getJSONObject(i).getString("overview");
                        List<Integer> categories=new ArrayList<>();
                        JSONArray genders=list.getJSONObject(i).getJSONArray("genre_ids");
                        Log.d("v","array - "+genders);
                        for (int w=0; w<genders.length();w++){
                            categories.add(genders.getInt(w));
                        }

                        lstMovie.add(new Movie(id,title,categories,description,poster_url));

                    }



                }
                myAdapter = new RecycleViewAdapter(present,lstMovie);
                myrv.setLayoutManager(new GridLayoutManager(present,3));
                myrv.setItemAnimator(new DefaultItemAnimator());
                myrv.setAdapter(myAdapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


    }

}
