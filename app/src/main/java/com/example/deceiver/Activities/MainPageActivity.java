package com.example.deceiver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.deceiver.Fragments.LogInFragment;
import com.example.deceiver.Fragments.MainPageFragment;
import com.example.deceiver.R;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMainPage, new MainPageFragment());
        ft.commit();
    }
}