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
import com.mehboob.passiveadmin.adapters.WithDrawAdapter;
import com.mehboob.passiveadmin.databinding.ActivityUserWithdrawBinding;
import com.mehboob.passiveadmin.models.Withdraw;

import java.util.ArrayList;

public class UserWithdrawActivity extends AppCompatActivity {
    private ActivityUserWithdrawBinding binding;
    private String withDrawUser;
    private DatabaseReference databaseReference;
    private ArrayList<Withdraw> withdraws;
    private WithDrawAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("Withdraws");
        withDrawUser = getIntent().getStringExtra("uid");
        withdraws = new ArrayList<>();

        fetchUserWithDraws();




    }

    private void fetchUserWithDraws() {
        databaseReference.child(withDrawUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.txtNoDeposit.setVisibility(View.GONE);
                    binding.recyclerDeposits.setVisibility(View.VISIBLE);

                    withdraws.clear();


                        Withdraw withdraw = snapshot.getValue(Withdraw.class);
                        withdraws.add(new Withdraw(withdraw.getUserId(),withdraw.getPushId(),withdraw.isVerified(),
                                withdraw.getWithDrawAmount(),withdraw.getWithDrawAccountNumber(),withdraw.getWithDrawAccountName(),
                                withdraw.getTimeStamp(),withdraw.getAccountWithDraw()));


                    adapter = new WithDrawAdapter(UserWithdrawActivity.this, withdraws);
                    binding.recyclerDeposits.setLayoutManager(new LinearLayoutManager(UserWithdrawActivity.this));
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