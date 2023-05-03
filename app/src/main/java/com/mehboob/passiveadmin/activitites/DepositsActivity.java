package com.mehboob.passiveadmin.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.passiveadmin.adapters.UserAdapter;
import com.mehboob.passiveadmin.databinding.ActivityDepositsBinding;
import com.mehboob.passiveadmin.models.User;

import java.util.ArrayList;

public class DepositsActivity extends AppCompatActivity {
    private ActivityDepositsBinding binding;
    private UserAdapter adapter;
    private ArrayList<User> users;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDepositsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRef = FirebaseDatabase.getInstance().getReference("Users");
users= new ArrayList<>();

        fetchDepositedUser();


    }

    private void fetchDepositedUser() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                if (snapshot.exists()) {
                    binding.recyclerDepositUser.setVisibility(View.VISIBLE);
                    binding.txtNoDeposit.setVisibility(View.GONE);


                    for (DataSnapshot snap : snapshot.getChildren()) {
                        User user = snap.getValue(User.class);
                        users.add(new User(user.getEmail(),user.getPassword(),user.getFirst_name(),user.getSur_name(),
                                user.getPhone_number(),user.getReferral_id(),user.getAddress(),
                                user.getUser_id(),user.getUser_image(),user.getCnic_front(),
                                user.getCnic_back(),user.isBlock()));
                    }
                    adapter= new UserAdapter(DepositsActivity.this,users,"Dep");
                    binding.recyclerDepositUser.setLayoutManager(new LinearLayoutManager(DepositsActivity.this));
                    binding.recyclerDepositUser.setAdapter(adapter);
                } else {
                    binding.recyclerDepositUser.setVisibility(View.GONE);
                    binding.txtNoDeposit.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.recyclerDepositUser.setVisibility(View.GONE);
                binding.txtNoDeposit.setVisibility(View.VISIBLE);
            }
        });
    }
}