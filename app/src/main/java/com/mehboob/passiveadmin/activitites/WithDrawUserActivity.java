package com.mehboob.passiveadmin.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.passiveadmin.adapters.UserAdapter;
import com.mehboob.passiveadmin.databinding.ActivityWithDrawUserBinding;
import com.mehboob.passiveadmin.models.User;

import java.util.ArrayList;

public class WithDrawUserActivity extends AppCompatActivity {
private ActivityWithDrawUserBinding binding;
    private UserAdapter adapter;
    private ArrayList<User> users;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWithDrawUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        users = new ArrayList<>();
        fetchUsers();
    }

    private void fetchUsers() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                if (snapshot.exists()) {



                    for (DataSnapshot snap : snapshot.getChildren()) {
                        User user = snap.getValue(User.class);
                        users.add(new User(user.getEmail(), user.getPassword(), user.getFirst_name(), user.getSur_name(),
                                user.getPhone_number(), user.getReferral_id(), user.getAddress(),
                                user.getUser_id(), user.getUser_image(), user.getCnic_front(),
                                user.getCnic_back(), user.isBlock()));
                    }
                    adapter = new UserAdapter(WithDrawUserActivity.this, users, "wit");
                    binding.recyclerWithdrawUser.setLayoutManager(new LinearLayoutManager(WithDrawUserActivity.this));
                    binding.recyclerWithdrawUser.setAdapter(adapter);
                } else {
                    Toast.makeText(WithDrawUserActivity.this, "No users", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WithDrawUserActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}