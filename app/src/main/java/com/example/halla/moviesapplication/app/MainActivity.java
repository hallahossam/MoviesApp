package com.example.halla.moviesapplication.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.halla.moviesapplication.R;

public class MainActivity extends AppCompatActivity implements MainFragment.callBack{

    private boolean mTwoBane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        if(findViewById(R.id.fragment_details_container) != null){
            mTwoBane = true;
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_details_container, new DetailFragment()).commit();
            }
        }
        else{
            mTwoBane = false;
        }
    }

    @Override
    public void onItemSelected(Bundle data) {
        //if it's a tablet then we are going to replace the fragment and send a bundle
        if(mTwoBane == true){
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details_container, fragment)
                    .commit();
        }
        //else we will be sending an intent carrying the bundle to the activity
        else{
            Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
            intent.putExtra("Bundle",data);
            startActivity(intent);
        }

    }
}
