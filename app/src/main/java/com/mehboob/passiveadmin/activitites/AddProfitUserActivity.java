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
import com.mehboob.passiveadmin.databinding.ActivityAddProfitUserBinding;
import com.mehboob.passiveadmin.models.User;
import com.mehboob.passiveadmin.models.Withdraw;

import java.util.ArrayList;

public class AddProfitUserActivity extends AppCompatActivity {
private ActivityAddProfitUserBinding binding;
private UserAdapter userAdapter;
private ArrayList<User> users;
private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddProfitUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        users= new ArrayList<>();


               fetchUsers();
    }

    private void fetchUsers() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    users.clear();


                    for (DataSnapshot snap : snapshot.getChildren()) {


                        User user = snap.getValue(User.class);

                        users.add(new User(user.getEmail(), user.getPassword(), user.getFirst_name(),
                                user.getSur_name(), user.getPhone_number(),
                                user.getReferral_id(), user.getAddress(),
                                user.getUser_id(), user.getUser_image(), user.getCnic_front(),
                                user.getCnic_back(), user.isBlock()));

                    }
                    userAdapter = new UserAdapter(AddProfitUserActivity.this,users,"Add");
                    binding.recyclerAddProfit.setLayoutManager(new LinearLayoutManager(AddProfitUserActivity.this));
                    binding.recyclerAddProfit.setAdapter(userAdapter);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProfitUserActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}