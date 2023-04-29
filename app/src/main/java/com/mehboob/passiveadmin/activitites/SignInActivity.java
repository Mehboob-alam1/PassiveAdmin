package com.mehboob.passiveadmin.activitites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mehboob.passiveadmin.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
private ActivitySignInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        
        binding.btnLogin.setOnClickListener(view -> {
            checkCredential();
        });
    }

    private void checkCredential() {

        if (binding.etEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter admin email", Toast.LENGTH_SHORT).show();
        }else if (binding.etPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter admin password", Toast.LENGTH_SHORT).show();
        }else if (binding.etEmail.getText().toString().equals("mehboobcodes@gmail.com") && binding.etPassword.getText().toString().equals("newpassd")){
            login();
        }else {
            Toast.makeText(this, "Bad credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {

        startActivity(new Intent(SignInActivity.this,MainActivity.class));
        finish();
    }
}