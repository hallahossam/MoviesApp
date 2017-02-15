package com.example.halla.moviesapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.halla.moviesapplication.models.MovieModel;

/**
 * Created by Halla on 15/02/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "MoviesDB";
    private DatabaseContract mDatabaseContract = new DatabaseContract();
    SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(mDatabaseContract.CREATE_TABLE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + mDatabaseContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addToDatabase(MovieModel movieModel){

        ContentValues contentValues = new ContentValues();
        contentValues.put(mDatabaseContract.MOVIE_OVERVIEW, movieModel.getmMovieOverview());
        contentValues.put(mDatabaseContract.MOVIE_POSTER, movieModel.getmMoviePoster());
        contentValues.put(mDatabaseContract.MOVIE_RATING, movieModel.getmMovieRating());
        contentValues.put(mDatabaseContract.MOVIE_RELEASE_DATE, movieModel.getmMovieReleaseDate());
        contentValues.put(mDatabaseContract.MOVIE_TITLE, movieModel.getmMovieTitle());

        mDatabase = getWritableDatabase();
        mDatabase.insert(mDatabaseContract.TABLE_NAME, null, contentValues);
        mDatabase.close();

    }
    public Cursor getAllMovies(){
        mDatabase = getReadableDatabase();
        Cursor mResult = mDatabase.rawQuery("select * from " + mDatabaseContract.TABLE_NAME, null);
        mResult.moveToFirst();
        mDatabase.close();
        return mResult;
    }

    public Cursor getMovie(String movieTitle){
        String [] arguments = new String [] {movieTitle};
        mDatabase = getReadableDatabase();
        Cursor mResult = mDatabase.rawQuery("select * from " + mDatabaseContract.TABLE_NAME
                + " where movieTitle like ?", arguments);
        mResult.moveToFirst();
        mDatabase.close();
        return mResult;
    }

    public boolean exists(String movieTitle){
        Cursor mResult = getMovie(movieTitle);
        if (mResult.getCount() == 0){
            return false;
        }
        else
            return true;
    }
}
