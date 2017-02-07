package com.example.halla.moviesapplication.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.halla.moviesapplication.models.movieItem;
import com.example.halla.moviesapplication.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Halla on 26/01/2017.
 */
public class MainFragment extends Fragment {

    //TODO: please add the API Key in this Variable
    private String API_KEY = "";

    private String baseUrl = "http://api.themoviedb.org/3/movie/";
    private GridView mGridView;
    private MoviesAdapter mMoviesAdapter;
    private ArrayList<MovieModel> mResult;
    private boolean mSelectionFlag;
    private boolean onlineOfflineMovie;
    private com.example.halla.moviesapplication.models.movieItem movieItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main,container,false);
        ActiveAndroid.initialize(getContext());

        mSelectionFlag = true;
        onlineOfflineMovie = true;

        mGridView = (GridView) view.findViewById(R.id.grid_movies);

        apiCall(baseUrl + "popular" + "?api_key=" + API_KEY);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sendingBundle(i);
            }
        });

        return view;
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
                mMoviesAdapter = new MoviesAdapter(mResult, getContext());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.most_popular:
                if(mSelectionFlag != true || onlineOfflineMovie == false ){
                    mSelectionFlag = true;
                    onlineOfflineMovie = true;
                    apiCall(baseUrl + "popular" + "?api_key=" + API_KEY);
                }
                else{
                    Toast.makeText(getContext(),"Popular movies is already chosen",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.top_rated:
                if(mSelectionFlag != false || onlineOfflineMovie == false){
                    onlineOfflineMovie = true;
                    mSelectionFlag = false;
                    apiCall(baseUrl + "top_rated" + "?api_key=" + API_KEY);
                }
                else{
                    Toast.makeText(getContext(),"Top rated movies is already chosen",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.favourites:
                movieItem = new movieItem();
                List<com.example.halla.moviesapplication.models.movieItem> movieItemList = movieItem.findAll();
                int length = movieItemList.size();
                if(length == 0){
                    onlineOfflineMovie = true;
                    break;
                }
                else {
                    onlineOfflineMovie = false;
                    ArrayList<MovieModel> movieModels = new ArrayList<>();

                    for (int i = 0; i < length; i++) {
                        MovieModel movieModel = new MovieModel();
                        movieModel.setmMovieTitle(movieItemList.get(i).mMovieTitle);
                        movieModel.setmMovieOverview(movieItemList.get(i).mMovieOverview);
                        movieModel.setmMoviePoster(movieItemList.get(i).mMoviePoster);
                        movieModel.setmMovieRating(movieItemList.get(i).mMovieRating);
                        movieModel.setmMovieReleaseDate(movieItemList.get(i).mMovieReleaseDate);

                        movieModels.add(movieModel);
                    }

                    mResult = movieModels;

                    mMoviesAdapter = new MoviesAdapter(mResult, getContext());
                    mGridView.setAdapter(mMoviesAdapter);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public interface callBack{
        void onItemSelected(Bundle data);
    }

    public void sendingBundle(int position){
        Bundle args = new Bundle();
        args.putString("API_KEY",API_KEY);
        args.putBoolean("OnlineOrOffline", onlineOfflineMovie);
        args.putParcelable("MovieObject",mResult.get(position));

        ((callBack) getActivity()).onItemSelected(args);
    }
}
