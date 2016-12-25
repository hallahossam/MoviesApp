package com.example.halla.moviesapplication.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Halla on 25/12/2016.
 */
public class MovieModel implements Serializable {
    private String mMoviePoster;
    private String mMovieTitle;
    private String mMovieOverview;
    private String mMovieRating;
    private String mMovieReleaseDate;

    public String getmMovieOverview() {
        return mMovieOverview;
    }

    public void setmMovieOverview(String mMovieOverview) {
        this.mMovieOverview = mMovieOverview;
    }

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public void setmMoviePoster(String mMoviePoster) {
        this.mMoviePoster = mMoviePoster;
    }

    public String getmMovieRating() {
        return mMovieRating;
    }

    public void setmMovieRating(String mMovieRating) {
        this.mMovieRating = mMovieRating;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public void setmMovieReleaseDate(String mMovieReleaseDate) {
        this.mMovieReleaseDate = mMovieReleaseDate;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public void setmMovieTitle(String mMovieTitle) {
        this.mMovieTitle = mMovieTitle;
    }
}
