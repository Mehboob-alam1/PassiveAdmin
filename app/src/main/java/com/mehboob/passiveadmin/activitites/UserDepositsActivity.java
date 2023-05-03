package com.mehboob.passiveadmin.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.passiveadmin.adapters.DepositAdapter;
import com.mehboob.passiveadmin.databinding.ActivityUserDepositsBinding;
import com.mehboob.passiveadmin.models.Deposit;

import java.util.ArrayList;

public class UserDepositsActivity extends AppCompatActivity {
    private ActivityUserDepositsBinding binding;
    private String userId;
    private DatabaseReference databaseReference;
    private ArrayList<Deposit> deposits;
    private DepositAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDepositsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId = getIntent().getStringExtra("uid");
        databaseReference = FirebaseDatabase.getInstance().getReference("Deposit");
deposits= new ArrayList<>();

        fetchUserDeposits();


    }

    private void fetchUserDeposits() {
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.txtNoDeposit.setVisibility(View.GONE);
                    binding.recyclerDeposits.setVisibility(View.VISIBLE);

                    deposits.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {

                        Deposit deposit = snap.getValue(Deposit.class);
                        deposits.add(new Deposit(deposit.getScreenShot(), deposit.getUserId(), deposit.getTimeStamp(),
                                deposit.getDepositBalance(), deposit.isApproved(),
                                deposit.getPushId(), deposit.getTotalBalance(),
                                deposit.getDepositAccount()));

                    }
                    adapter = new DepositAdapter(UserDepositsActivity.this, deposits);
                    binding.recyclerDeposits.setLayoutManager(new LinearLayoutManager(UserDepositsActivity.this));
                    binding.recyclerDeposits.setAdapter(adapter);

                } else {
                    binding.txtNoDeposit.setVisibility(View.VISIBLE);
                    binding.recyclerDeposits.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.txtNoDeposit.setVisibility(View.VISIBLE);
                binding.recyclerDeposits.setVisibility(View.GONE);
            }
        });

    }
}