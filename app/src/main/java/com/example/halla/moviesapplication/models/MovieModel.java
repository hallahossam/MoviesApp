package com.example.halla.moviesapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Halla on 25/12/2016.
 */
public class MovieModel implements Parcelable {
    private String mMoviePoster;
    private String mMovieTitle;
    private String mMovieOverview;
    private String mMovieRating;
    private String mMovieReleaseDate;
    private String mMovieID;

    public MovieModel(){};

    public MovieModel(Parcel in) {
        mMoviePoster = in.readString();
        mMovieTitle = in.readString();
        mMovieOverview = in.readString();
        mMovieRating = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieID = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    public String getmMovieID() {
        return mMovieID;
    }

    public void setmMovieID(String mMovieID) {
        this.mMovieID = mMovieID;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMoviePoster);
        parcel.writeString(mMovieTitle);
        parcel.writeString(mMovieOverview);
        parcel.writeString(mMovieRating);
        parcel.writeString(mMovieReleaseDate);
        parcel.writeString(mMovieID);
    }
}
