package com.example.halla.moviesapplication;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.halla.moviesapplication.adapters.MoviesAdapter;
import com.example.halla.moviesapplication.app.AppController;
import com.example.halla.moviesapplication.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String url = "http://api.themoviedb.org/3/movie/popular?api_key=b08c49af044bc54f2d9e7a5208b72ac0";
    private GridView mGridView;
    private MoviesAdapter moviesAdapter;
    ArrayList<MovieModel> Result;
    boolean selectionFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectionFlag = true;

        mGridView = (GridView) findViewById(R.id.grid_movies);
        API_Call(url);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("MovieObject", Result.get(i));
                startActivity(intent);
            }
        });

    }

    public void API_Call(String sentUrl){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, sentUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Result = JsonParse(response);
                moviesAdapter = new MoviesAdapter(Result, MainActivity.this);
                mGridView.setAdapter(moviesAdapter);
                Toast.makeText(MainActivity.this,"All Done",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error in Response", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

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
                if(selectionFlag != true){
                    selectionFlag = true;
                    url = "http://api.themoviedb.org/3/movie/popular?api_key=b08c49af044bc54f2d9e7a5208b72ac0";
                    API_Call(url);
                }
                break;

            case R.id.top_rated:
                if(selectionFlag != false){
                    selectionFlag = false;
                    url = "http://api.themoviedb.org/3/movie/top_rated?api_key=b08c49af044bc54f2d9e7a5208b72ac0";
                    API_Call(url);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
