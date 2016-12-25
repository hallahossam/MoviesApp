package com.example.halla.moviesapplication.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.adapters.MoviesAdapter;
import com.example.halla.moviesapplication.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //TODO: please add the API Key in this Variable
    private String API_KEY = "";
    private String toggle = "popular";
    private String mUrl = "http://api.themoviedb.org/3/movie/" + toggle + "?api_key=" + API_KEY;
    private GridView mGridView;
    private MoviesAdapter mMoviesAdapter;
    private ArrayList<MovieModel> mResult;
    private boolean mSelectionFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSelectionFlag = true;

        mGridView = (GridView) findViewById(R.id.grid_movies);
        apiCall(mUrl);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("MovieObject", mResult.get(i));
                startActivity(intent);
            }
        });

    }

    /* apiCall is for calling the moviedb.org API, it accepts one parameter
     which is the url..
     Used Volley Library to handle the JsonObjectRequest. */

    public void apiCall(String sentUrl){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, sentUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mResult = JsonParse(response);
                mMoviesAdapter = new MoviesAdapter(mResult, MainActivity.this);
                mGridView.setAdapter(mMoviesAdapter);
                       }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error in Response", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    /*JsonParse is for parsing the json object returned from the request.
     it accepts one parameter, which is the json object. */
    public ArrayList<MovieModel> JsonParse(JSONObject movieObject){

        ArrayList<MovieModel> movieModels = new ArrayList<>();
        try {
            JSONArray moviesArray = movieObject.getJSONArray("results");
            int movieArraySize = moviesArray.length();
            JSONObject singleMovie;
            for (int i=0;i<movieArraySize;i++){
                MovieModel movieModel = new MovieModel();
                singleMovie = moviesArray.getJSONObject(i);
                movieModel.setmMovieOverview(singleMovie.getString("overview"));
                movieModel.setmMoviePoster(singleMovie.getString("poster_path"));
                movieModel.setmMovieRating(singleMovie.getString("vote_average"));
                movieModel.setmMovieReleaseDate(singleMovie.getString("release_date"));
                movieModel.setmMovieTitle(singleMovie.getString("original_title"));

                movieModels.add(movieModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieModels;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.most_popular:
                if(mSelectionFlag != true){
                    mSelectionFlag = true;
                   toggle = "popular";
                    apiCall(mUrl);
                }
                break;

            case R.id.top_rated:
                if(mSelectionFlag != false){
                    mSelectionFlag = false;
                    toggle = "top_rated";
                    apiCall(mUrl);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
