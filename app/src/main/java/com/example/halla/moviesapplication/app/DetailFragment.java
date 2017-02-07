package com.example.halla.moviesapplication.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.adapters.ReviewsAdapter;
import com.example.halla.moviesapplication.adapters.TrailersAdapter;
import com.example.halla.moviesapplication.models.movieItem;
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
 * Created by Halla on 26/01/2017.
 */
public class DetailFragment extends Fragment {

    private TextView movieTitle, movieOverview, movieRate, movieReleaseDate;
    private ImageView moviePoster;
    private MovieModel movieModel;
    private String API_KEY = "";
    private String mMovieID = "";
    boolean onOff ;

    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    private ExpandableHeightListView reviewList;
    private ExpandableHeightListView trailerList;
    Bundle bundle;
    private ImageView addToFavs;
    private ArrayList<MovieTrailers> movieTrailersArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_details,container, false);
        bundle = getArguments();
        if(bundle != null) {
            movieModel = (MovieModel) bundle.getParcelable("MovieObject");
            onOff = bundle.getBoolean("OnlineOrOffline");

            movieTitle = (TextView) view.findViewById(R.id.tv_movie_title);
            movieRate = (TextView) view.findViewById(R.id.tv_movie_rating);
            movieReleaseDate = (TextView) view.findViewById(R.id.tv_movie_release_date);
            movieOverview = (TextView) view.findViewById(R.id.tv_movie_overview);
            moviePoster = (ImageView) view.findViewById(R.id.img_selected_movie);
            addToFavs = (ImageView) view.findViewById(R.id.img_add_fav);
            reviewList = (ExpandableHeightListView) view.findViewById(R.id.listview_reviews);
            trailerList = (ExpandableHeightListView) view.findViewById(R.id.listview_trailers);

            settingMovieDetails();

            trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(null, Uri.parse("https://www.youtube.com/watch?v=" + movieTrailersArrayList.get(i).getTrailerKey()));
                    startActivity(intent);
                }
            });
            final movieItem movieItem = new movieItem();
            if(movieItem.findMovie(movieModel.getmMovieTitle()) != null){
                addToFavs.setVisibility(View.INVISIBLE);
            }
            addToFavs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    movieItem.mMovieOverview = movieModel.getmMovieOverview();
                    movieItem.mMoviePoster = movieModel.getmMoviePoster();
                    movieItem.mMovieRating = movieModel.getmMovieRating();
                    movieItem.mMovieReleaseDate = movieModel.getmMovieReleaseDate();
                    movieItem.mMovieTitle = movieModel.getmMovieTitle();
                    movieItem.save();
                    addToFavs.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"Added to favourites",Toast.LENGTH_LONG).show();


                }
            });
        }
        return view;
    }

    //This function is for setting the movie details in the views
    public void settingMovieDetails(){
        movieTitle.setText(movieModel.getmMovieTitle());
        movieOverview.setText(movieModel.getmMovieOverview());
        movieReleaseDate.setText(movieModel.getmMovieReleaseDate());
        movieRate.setText(movieModel.getmMovieRating());
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movieModel.getmMoviePoster())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(moviePoster);


        if(onOff == true) {
            mMovieID = movieModel.getmMovieID();
            API_KEY = bundle.getString("API_KEY");
            trailers_call("http://api.themoviedb.org/3/movie/" + mMovieID + "/videos?api_key=" + API_KEY);
            Reviews_call("http://api.themoviedb.org/3/movie/" + mMovieID + "/reviews?api_key=" + API_KEY);
        }
    }

    public void trailers_call(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                movieTrailersArrayList = JsonTrailerParse(response);
                trailersAdapter = new TrailersAdapter(movieTrailersArrayList,getActivity());
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
                reviewsAdapter = new ReviewsAdapter(JsonReviewParse(response),getActivity());
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
