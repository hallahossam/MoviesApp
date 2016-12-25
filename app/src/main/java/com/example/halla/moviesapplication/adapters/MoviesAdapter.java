package com.example.halla.moviesapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Halla on 25/12/2016.
 */
public class MoviesAdapter extends BaseAdapter {

    private ArrayList<MovieModel>  mMovieModels;
    private Context mContext;

    public MoviesAdapter(ArrayList<MovieModel> movieModel, Context context){
        mMovieModels = movieModel;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mMovieModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mMovieModels.get(i).getmMoviePoster();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.moviePoster = (ImageView) view.findViewById(R.id.img_movie);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+mMovieModels.get(i).getmMoviePoster()).into(viewHolder.moviePoster);
        return view;
    }

    public class ViewHolder{
        private ImageView moviePoster;
    }
}
