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
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.adapters.MoviesAdapter;
import com.example.halla.moviesapplication.models.MovieItem;
import com.example.halla.moviesapplication.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //TODO: please add the API Key in this Variable
    private String API_KEY = "b08c49af044bc54f2d9e7a5208b72ac0";

    private String baseUrl = "http://api.themoviedb.org/3/movie/";
    private GridView mGridView;
    private MoviesAdapter mMoviesAdapter;
    private ArrayList<MovieModel> mResult;
    private boolean mSelectionFlag;
    private boolean onlineOfflineMovie;
    private MovieItem movieItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(getApplication());

        mSelectionFlag = true;
        onlineOfflineMovie = true;

        mGridView = (GridView) findViewById(R.id.grid_movies);

        apiCall(baseUrl + "popular" + "?api_key=" + API_KEY);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("MovieObject", mResult.get(i));
                intent.putExtra("API_KEY", API_KEY);
                intent.putExtra("OnlineOrOffline", onlineOfflineMovie);
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
                movieModel.setmMovieID(singleMovie.getString("id"));

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
                    onlineOfflineMovie = true;
                    apiCall(baseUrl + "popular" + "?api_key=" + API_KEY);
                }
                else{
                    Toast.makeText(MainActivity.this,"Popular movies is already chosen",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.top_rated:
                if(mSelectionFlag != false){
                    onlineOfflineMovie = true;
                    mSelectionFlag = false;
                    apiCall(baseUrl + "top_rated" + "?api_key=" + API_KEY);
                }
                else{
                    Toast.makeText(MainActivity.this,"Top rated movies is already chosen",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.favourites:
                onlineOfflineMovie = false;
                movieItem = new MovieItem();
                List<MovieItem> movieItemList = movieItem.findAll();
                ArrayList<MovieModel> movieModels = new ArrayList<>();
                int length = movieItemList.size();

                for(int i=0; i<length; i++){
                    MovieModel movieModel = new MovieModel();
                    movieModel.setmMovieTitle(movieItemList.get(i).mMovieTitle);
                    movieModel.setmMovieOverview(movieItemList.get(i).mMovieOverview);
                    movieModel.setmMoviePoster(movieItemList.get(i).mMoviePoster);
                    movieModel.setmMovieRating(movieItemList.get(i).mMovieRating);
                    movieModel.setmMovieReleaseDate(movieItemList.get(i).mMovieReleaseDate);

                    movieModels.add(movieModel);
                }

                mResult = movieModels ;

                mMoviesAdapter = new MoviesAdapter(mResult, MainActivity.this);
                mGridView.setAdapter(mMoviesAdapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
