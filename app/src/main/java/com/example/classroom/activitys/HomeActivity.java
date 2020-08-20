package com.example.classroom.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.classroom.R;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  finish();
       // setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  finish();
        onBackPressed();
    }
}