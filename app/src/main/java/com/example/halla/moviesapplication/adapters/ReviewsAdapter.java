package com.example.halla.moviesapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.halla.moviesapplication.R;
import com.example.halla.moviesapplication.models.MovieModel;
import com.example.halla.moviesapplication.models.MovieReviews;
import com.example.halla.moviesapplication.models.MovieTrailers;

import java.util.ArrayList;

/**
 * Created by Halla on 22/01/2017.
 */
public class ReviewsAdapter extends BaseAdapter {


    private ArrayList<MovieReviews> mMovieReviews;
    private Context mContext;

    public ReviewsAdapter(ArrayList<MovieReviews> movieReviews, Context context){
        mMovieReviews = movieReviews;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mMovieReviews.size();
    }

    @Override
    public Object getItem(int i) {
        return mMovieReviews.get(i).getReviewAuthor();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.review_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.movieReviewAuthor = (TextView) view.findViewById(R.id.tv_review_author);
            viewHolder.movieReviewContent = (TextView) view.findViewById(R.id.tv_review_content);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.movieReviewAuthor.setText(mMovieReviews.get(i).getReviewAuthor());
        viewHolder.movieReviewContent.setText(mMovieReviews.get(i).getReviewContent());

        return view;
    }

    public class ViewHolder{
        private TextView movieReviewAuthor;
        private TextView movieReviewContent;
    }

}
