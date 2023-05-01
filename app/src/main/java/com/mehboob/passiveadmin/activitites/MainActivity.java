package com.mehboob.passiveadmin.activitites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mehboob.passiveadmin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.cardAccounts.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddAccountsActivity.class));
        });

        binding.cardPackages.setOnClickListener(v -> {

            startActivity(new Intent(MainActivity.this, AddPackagesActivity.class));

        });

        binding.cardUsers.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,AllUsersActivity.class));

        });
    }
}