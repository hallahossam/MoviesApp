package com.example.halla.moviesapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.models.MovieModel;
import com.example.halla.moviesapplication.models.MovieTrailers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Halla on 22/01/2017.
 */
public class TrailersAdapter extends BaseAdapter{


        private ArrayList<MovieTrailers> mMovieTrailers;
        private Context mContext;

        public TrailersAdapter(ArrayList<MovieTrailers> movieTrailers, Context context){
            mMovieTrailers = movieTrailers;
            mContext = context;
        }


        @Override
        public int getCount() {
            return mMovieTrailers.size();
        }

        @Override
        public Object getItem(int i) {
            return mMovieTrailers.get(i).getTrailerName();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.movieTrailerImg = (ImageView) view.findViewById(R.id.img_trailer);
                viewHolder.movieTrailerName = (TextView) view.findViewById(R.id.tv_trailer_name);
                view.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.movieTrailerName.setText(mMovieTrailers.get(i).getTrailerName());
            return view;
        }

        public class ViewHolder{
            private ImageView movieTrailerImg;
            private TextView movieTrailerName;
        }


}


