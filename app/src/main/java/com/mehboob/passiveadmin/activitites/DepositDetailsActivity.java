package com.mehboob.passiveadmin.activitites;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;


import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.passiveadmin.R;
import com.mehboob.passiveadmin.databinding.ActivityDepositDetailsBinding;
import com.mehboob.passiveadmin.models.Balance;
import com.mehboob.passiveadmin.models.Deposit;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DepositDetailsActivity extends AppCompatActivity {
    private ActivityDepositDetailsBinding binding;
    private String user;
    private Deposit deposit;
    private DatabaseReference userDeposit, userTotalBalanceRef;
    private String balance = "";
    private String userTotalBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepositDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDeposit = FirebaseDatabase.getInstance().getReference("Deposit");
        userTotalBalanceRef = FirebaseDatabase.getInstance().getReference("Balance");
        user = getIntent().getStringExtra("user");
        Gson gson = new Gson();
        Type type = new TypeToken<Deposit>() {
        }.getType();
        deposit = gson.fromJson(user, type);


        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        getUserTotalBalance();

        try {
            Glide.with(this)
                    .load(deposit.getScreenShot())
                    .into(binding.imgScreenShot);
        } catch (IllegalArgumentException e) {

        }

        binding.txtDepositThrough.setText(deposit.getDepositAccount());
        binding.txtBalanceDeposit.setText(deposit.getDepositBalance());
        binding.txtTotalBalance.setText(deposit.getTotalBalance());
        Date date = new Date(Long.parseLong(deposit.getTimeStamp()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(date);
        binding.txtDateDeposit.setText(dateString);

        checkIsApproved();


        binding.btnVerifyDeposit.setOnClickListener(v -> {
            if (deposit.isApproved()) {
                Toast.makeText(this, "User payment is approved", Toast.LENGTH_SHORT).show();
            } else {


                addBalanceDialog();
            }

        });
    }

    private void getUserTotalBalance() {
        userTotalBalanceRef.child(deposit.getUserId())

                .child("totalBalance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userTotalBalance = snapshot.getValue(String.class);
                        } else {
                            userTotalBalance = "0";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkIsApproved() {
        if (deposit.isApproved()) {
            binding.txtVerified.setVisibility(View.VISIBLE);
            binding.txtNotVerified.setVisibility(View.GONE);
            binding.btnVerifyDeposit.setText("Verified");
        } else {
            binding.txtVerified.setVisibility(View.GONE);
            binding.txtNotVerified.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("MissingInflatedId")
    private void addBalanceDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(DepositDetailsActivity.this, R.style.AppBottomSheetDialogTheme);
        View bottomsheetView = LayoutInflater.from(DepositDetailsActivity.this).
                inflate(R.layout.item_add__balance, (CardView) findViewById(R.id.cardBalance));
        dialog.setContentView(bottomsheetView);
        dialog.show();

        TextView button = bottomsheetView.findViewById(R.id.btnNextBalance);
        EditText editText = bottomsheetView.findViewById(R.id.etBalance);

        editText.setText(deposit.getDepositBalance());

        button.setOnClickListener(v -> {

            if (editText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter the balance", Toast.LENGTH_SHORT).show();
            } else {

                dialog.dismiss();
                verifyPayments(editText.getText().toString());
            }
        });

    }

    private void verifyPayments(String balance) {
        userDeposit.child(deposit.getUserId())
                .child(deposit.getPushId())
                .child("approved")
                .setValue(true)
                .addOnCompleteListener(task -> {


                    if (task.isComplete() && task.isSuccessful()) {



                        userDeposit.child(deposit.getUserId())
                                .child(deposit.getPushId())
                                .child("depositBalance")
                                .setValue(balance)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isComplete() && task1.isSuccessful()) {
                                      //  Balance userBalance = new Balance(balance,deposit.getUserId());
                                        userTotalBalanceRef.child(deposit.getUserId())
                                                .child("totalBalance")
                                                .setValue(String.valueOf(Integer.parseInt(userTotalBalance) + Integer.parseInt(balance)));
                                        userDeposit.child(deposit.getUserId())
                                                .child(deposit.getPushId())
                                                .child("totalBalance")
                                                .setValue(String.valueOf(Integer.parseInt(userTotalBalance) + Integer.parseInt(balance)));

                                        binding.btnVerifyDeposit.setText("Verified");
                                        binding.txtVerified.setVisibility(View.VISIBLE);
                                        binding.txtNotVerified.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(e -> {

                                });
                    } else {
                        binding.txtVerified.setVisibility(View.GONE);
                        binding.txtNotVerified.setVisibility(View.VISIBLE);
                    }


                }).addOnFailureListener(e -> {

                    Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    binding.txtVerified.setVisibility(View.GONE);
                    binding.txtNotVerified.setVisibility(View.VISIBLE);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIsApproved();
    }
}