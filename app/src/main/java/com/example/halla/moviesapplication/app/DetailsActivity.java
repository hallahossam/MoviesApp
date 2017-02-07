package com.example.halla.moviesapplication.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.halla.moviesapplication.R;

/**
 * Created by Halla on 25/12/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            DetailFragment detailsActivityFragment = new DetailFragment();
            //sending the bundle to the fragment
            detailsActivityFragment.setArguments(getIntent().getBundleExtra("Bundle"));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_details_container,
                    detailsActivityFragment).commit();
        }
    }
}
