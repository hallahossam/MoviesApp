package com.example.halla.moviesapplication.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Halla on 22/01/2017.
 */
@Table(name = "movieItem", id = "_id")
public class movieItem extends Model {

    @Column(name = "moviePoster")
    public String mMoviePoster;

    @Column(name = "movieTitle")
    public String mMovieTitle;

    @Column(name = "movieOverview")
    public String mMovieOverview;

    @Column(name = "movieRating")
    public String mMovieRating;

    @Column(name = "movieReleaseDate")
    public String mMovieReleaseDate;


    public movieItem(){
        super();
    };

    public  List<movieItem> findAll(){
        return new Select().from(movieItem.class).execute();
    }

    public movieItem findMovie(String movieTitle){
        return new Select().from(movieItem.class).where("movieTitle =?", movieTitle).executeSingle();
    }
}
