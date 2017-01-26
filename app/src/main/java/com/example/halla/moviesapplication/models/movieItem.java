package com.example.halla.moviesapplication.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Halla on 22/01/2017.
 */
@Table(name = "MovieItem", id = "_id")
public class MovieItem extends Model {


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



    public MovieItem(){
        super();
    };

    public  static MovieItem findByID(String id){
        return new Select().from(MovieItem.class).where(id + " = ?", id).executeSingle();
    }

    public static List<MovieItem> findAll(){
        return new Select().from(MovieItem.class).execute();
    }
}
