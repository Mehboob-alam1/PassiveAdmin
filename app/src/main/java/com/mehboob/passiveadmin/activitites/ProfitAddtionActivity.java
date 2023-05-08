package com.mehboob.passiveadmin.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.passiveadmin.databinding.ActivityProfitAddtionBinding;

public class ProfitAddtionActivity extends AppCompatActivity {
    private ActivityProfitAddtionBinding binding;
    private String uid;
    private DatabaseReference balanceRef;
    private String userTotalBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfitAddtionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        uid = getIntent().getStringExtra("uid");
        balanceRef = FirebaseDatabase.getInstance().getReference("Balance");

        fetchUserBalance();

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.btnAdd.setOnClickListener(v -> {
            if (binding.etAddProfit.getText().toString().isEmpty())
                Toast.makeText(this, "No balance added", Toast.LENGTH_SHORT).show();
            else {
                balanceRef.child(uid)
                        .child("totalBalance")
                        .setValue(String.valueOf(Integer.parseInt(userTotalBalance) + Integer.parseInt(binding.etAddProfit.getText().toString())))
                        .addOnCompleteListener(task -> {
                            if (task.isComplete() && task.isSuccessful()) {
                                Toast.makeText(ProfitAddtionActivity.this, "Profit added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ProfitAddtionActivity.this, "Something went wrong ! Try again", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

        });

    }

    private void fetchUserBalance() {
        balanceRef.child(uid)
                .child("totalBalance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userTotalBalance = snapshot.getValue(String.class);
                        binding.txtProfitAddition.setText(userTotalBalance);
                        if(userTotalBalance!=null) {
                            if (Integer.parseInt(userTotalBalance) > 0) {
                                binding.txtYes.setVisibility(View.VISIBLE);
                                binding.txtNo.setVisibility(View.GONE);
                            } else {
                                binding.txtYes.setVisibility(View.GONE);
                                binding.txtNo.setVisibility(View.VISIBLE);
                                binding.btnAdd.setVisibility(View.GONE);
                            }
                        }else{
                            binding.txtYes.setVisibility(View.GONE);
                            binding.txtNo.setVisibility(View.VISIBLE);
                            binding.btnAdd.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}