package com.example.halla.moviesapplication.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.adapters.ReviewsAdapter;
import com.example.halla.moviesapplication.adapters.TrailersAdapter;
import com.example.halla.moviesapplication.models.MovieItem;
import com.example.halla.moviesapplication.models.MovieModel;
import com.example.halla.moviesapplication.models.MovieReviews;
import com.example.halla.moviesapplication.models.MovieTrailers;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Halla on 25/12/2016.
 */
public class DetailsActivity extends AppCompatActivity {
    private TextView movieTitle, movieOverview, movieRate, movieReleaseDate;
    private ImageView moviePoster;
    private   MovieModel movieModel;
    private String API_KEY = "";
    private String mMovieID = "";
    boolean onOff ;

    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    private ExpandableHeightListView reviewList;
    private ExpandableHeightListView trailerList;

    private Button addToFavs;
    private ArrayList<MovieTrailers> movieTrailersArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        movieModel = (MovieModel) getIntent().getExtras().getParcelable("MovieObject");
        onOff = getIntent().getExtras().getBoolean("OnlineOrOffline");

        movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        movieRate = (TextView) findViewById(R.id.tv_movie_rating);
        movieReleaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        movieOverview = (TextView) findViewById(R.id.tv_movie_overview);
        moviePoster = (ImageView) findViewById(R.id.img_selected_movie);
        addToFavs = (Button) findViewById(R.id.btn_add_fav);
        reviewList = (ExpandableHeightListView) findViewById(R.id.listview_reviews);
        trailerList = (ExpandableHeightListView) findViewById(R.id.listview_trailers);

        settingMovieDetails();

        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(null, Uri.parse("https://www.youtube.com/watch?v=" +movieTrailersArrayList.get(i).getTrailerKey() ));
                startActivity(intent);
            }
        });

        addToFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieItem movieItem = new MovieItem();
                movieItem.mMovieOverview = movieModel.getmMovieOverview();
                movieItem.mMoviePoster = movieModel.getmMoviePoster();
                movieItem.mMovieRating = movieModel.getmMovieRating();
                movieItem.mMovieReleaseDate = movieModel.getmMovieReleaseDate();
                movieItem.mMovieTitle = movieModel.getmMovieTitle();
                movieItem.save();
            }
        });

    }

    //This function is for setting the movie details in the views
    public void settingMovieDetails(){
        movieTitle.setText(movieModel.getmMovieTitle());
        movieOverview.setText(movieModel.getmMovieOverview());
        movieReleaseDate.setText(movieModel.getmMovieReleaseDate());
        movieRate.setText(movieModel.getmMovieRating());
        Picasso.with(DetailsActivity.this).load("http://image.tmdb.org/t/p/w342/" + movieModel.getmMoviePoster())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(moviePoster);

        if(onOff == true) {
            mMovieID = movieModel.getmMovieID();
            API_KEY = getIntent().getExtras().getString("API_KEY");
            trailers_call("http://api.themoviedb.org/3/movie/" + mMovieID + "/videos?api_key=" + API_KEY);
            Reviews_call("http://api.themoviedb.org/3/movie/" + mMovieID + "/reviews?api_key=" + API_KEY);
        }
    }

    public void trailers_call(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                movieTrailersArrayList = JsonTrailerParse(response);
                trailersAdapter = new TrailersAdapter(movieTrailersArrayList,DetailsActivity.this);
                trailerList.setAdapter(trailersAdapter);
                trailerList.setExpanded(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error in Response", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void Reviews_call(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                reviewsAdapter = new ReviewsAdapter(JsonReviewParse(response),DetailsActivity.this);
                reviewList.setAdapter(reviewsAdapter);
                reviewList.setExpanded(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error in Response", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public ArrayList<MovieTrailers> JsonTrailerParse(JSONObject movieObject){

        ArrayList<MovieTrailers> movieTrailers = new ArrayList<>();
        try {
            JSONArray moviesArray = movieObject.getJSONArray("results");
            int movieArraySize = moviesArray.length();
            JSONObject singleTrailer;
            for (int i=0;i<movieArraySize;i++){
                MovieTrailers movieTrailer = new MovieTrailers();
                singleTrailer = moviesArray.getJSONObject(i);
                movieTrailer.setTrailerKey(singleTrailer.getString("key"));
                movieTrailer.setTrailerName(singleTrailer.getString("name"));
                movieTrailers.add(movieTrailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieTrailers;
    }

    public ArrayList<MovieReviews> JsonReviewParse(JSONObject movieObject){

        ArrayList<MovieReviews> movieReviews = new ArrayList<>();
        try {
            JSONArray moviesArray = movieObject.getJSONArray("results");
            int movieArraySize = moviesArray.length();
            JSONObject singleTrailer;
            for (int i=0;i<movieArraySize;i++){
                MovieReviews movieReview = new MovieReviews();
                singleTrailer = moviesArray.getJSONObject(i);
                movieReview.setReviewAuthor(singleTrailer.getString("author"));
                movieReview.setReviewContent(singleTrailer.getString("content"));
                movieReviews.add(movieReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieReviews;
    }
}
