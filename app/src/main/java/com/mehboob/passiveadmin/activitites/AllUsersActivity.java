package com.mehboob.passiveadmin.activitites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mehboob.passiveadmin.databinding.ActivityAllUsersBinding;
import com.mehboob.passiveadmin.models.User;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {
private ActivityAllUsersBinding binding;
private ArrayList<User> listUser;
private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAllUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listUser= new ArrayList<>();

        userRef= FirebaseDatabase.getInstance().getReference("Users");
        
        
        
        fetchUsers();
        


    }

    private void fetchUsers() {
    }
}