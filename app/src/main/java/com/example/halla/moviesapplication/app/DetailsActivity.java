package com.example.halla.moviesapplication.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.models.MovieModel;
import com.squareup.picasso.Picasso;

/**
 * Created by Halla on 25/12/2016.
 */
public class DetailsActivity extends AppCompatActivity {
    private TextView movieTitle, movieOverview, movieRate, movieReleaseDate;
    private ImageView moviePoster;
    private   MovieModel movieModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        movieModel = (MovieModel) getIntent().getSerializableExtra("MovieObject");

        movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        movieRate = (TextView) findViewById(R.id.tv_movie_rating);
        movieReleaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        movieOverview = (TextView) findViewById(R.id.tv_movie_overview);
        moviePoster = (ImageView) findViewById(R.id.img_selected_movie);

        settingMovieDetails();

    }

    //This function is for setting the movie details in the views
    public void settingMovieDetails(){
        movieTitle.setText(movieModel.getmMovieTitle());
        movieOverview.setText(movieModel.getmMovieOverview());
        movieReleaseDate.setText(movieModel.getmMovieReleaseDate());
        movieRate.setText(movieModel.getmMovieRating());
        Picasso.with(DetailsActivity.this).load("http://image.tmdb.org/t/p/w342/" + movieModel.getmMoviePoster())
                .into(moviePoster);

    }
}
