package com.mehboob.passiveadmin.activitites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.passiveadmin.databinding.ActivityUserDetailBinding;

public class UserDetailActivity extends AppCompatActivity {
private ActivityUserDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}