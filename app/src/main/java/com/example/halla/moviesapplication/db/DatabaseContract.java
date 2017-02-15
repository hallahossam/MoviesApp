package com.example.halla.moviesapplication.db;

/**
 * Created by Halla on 15/02/2017.
 */
public class DatabaseContract {

    public String TABLE_NAME = "Movies";

    /****** Database Columns *******/
    public String MOVIE_ID = "id";
    public String MOVIE_POSTER = "moviePoster";
    public  String MOVIE_TITLE = "movieTitle";
    public String MOVIE_OVERVIEW = "movieOverview";
    public String MOVIE_RATING = "movieRating";
    public String MOVIE_RELEASE_DATE = "movieReleaseDate";
    /*********************************************************/

    public String CREATE_TABLE_MOVIES = "create table " + TABLE_NAME + "(" + MOVIE_ID
            + " integer primary key autoincrement, " + MOVIE_POSTER + " text not null, "
            + MOVIE_TITLE + " text not null, " + MOVIE_OVERVIEW + " text not null, "
            + MOVIE_RATING + " text not null, " + MOVIE_RELEASE_DATE
            + " text not null)";

}
