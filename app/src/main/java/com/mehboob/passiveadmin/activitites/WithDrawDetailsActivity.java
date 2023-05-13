package com.mehboob.passiveadmin.activitites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.passiveadmin.databinding.ActivityWithDrawDetailsBinding;
import com.mehboob.passiveadmin.models.Withdraw;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WithDrawDetailsActivity extends AppCompatActivity {
private ActivityWithDrawDetailsBinding binding;
private Withdraw withdraw;
private String userWithDraw;
private DatabaseReference withDrawRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWithDrawDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        withDrawRef= FirebaseDatabase.getInstance().getReference("Withdraws");
        userWithDraw = getIntent().getStringExtra("withdraw");
        Gson gson = new Gson();
        Type type = new TypeToken<Withdraw>() {
        }.getType();
        withdraw = gson.fromJson(userWithDraw, type);

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.imgCopyAccountNumber.setOnClickListener(v -> {
            copyTextToClipboard(binding.txtAccountNumberWithdraw.getText().toString());
        });


        binding.txtWithdrawThrough.setText(withdraw.getWithDrawAccountName());
        binding.txtBalanceWithdraw.setText(withdraw.getWithDrawAmount());
binding.txtAccountNumberWithdraw.setText(withdraw.getWithDrawAccountNumber());
        Date date = new Date(Long.parseLong(withdraw.getTimeStamp()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(date);
        binding.txtDateWithdraw.setText(dateString);

        binding.txtWithdrawThroughAccount.setText(withdraw.getAccountWithDraw());


        if (withdraw.isVerified()){
            binding.txtVerified.setVisibility(View.VISIBLE);
            binding.txtNotVerified.setVisibility(View.GONE);
            binding.btnVerifyWithDraw.setText("Approved");
        }else {
            binding.txtNotVerified.setVisibility(View.VISIBLE);
            binding.txtVerified.setVisibility(View.GONE);
        }


        binding.btnVerifyWithDraw.setOnClickListener(v -> {
            if (withdraw.isVerified()) {
                Toast.makeText(this, "User payment is approved", Toast.LENGTH_SHORT).show();
            } else {


verifyPayments();
            }
        });


    }

    private void copyTextToClipboard(String text) {
        // Get the text from the TextView


        // Get the ClipboardManager
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // Create a new ClipData object to store the text
        ClipData clipData = ClipData.newPlainText("label", text);

        // Set the ClipData object as the clipboard data
        clipboardManager.setPrimaryClip(clipData);

        // Show a toast message to indicate that the text has been copied
        Toast.makeText(this, "Text copied to clipboard " + text, Toast.LENGTH_SHORT).show();
    }
    private void verifyPayments() {
        withDrawRef.child(withdraw.getUserId())

                .child("verified")
                .setValue(true)
                .addOnCompleteListener(task -> {


                    if (task.isComplete() && task.isSuccessful()) {
                        binding.txtVerified.setVisibility(View.VISIBLE);
                        binding.txtNotVerified.setVisibility(View.GONE);

                      binding.btnVerifyWithDraw.setText("Approved");

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
        if (withdraw.isVerified()){
            binding.txtVerified.setVisibility(View.VISIBLE);
            binding.txtNotVerified.setVisibility(View.GONE);
            binding.btnVerifyWithDraw.setText("Approved");
        }else {
            binding.txtNotVerified.setVisibility(View.VISIBLE);
            binding.txtVerified.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (withdraw.isVerified()){
            binding.txtVerified.setVisibility(View.VISIBLE);
            binding.txtNotVerified.setVisibility(View.GONE);
            binding.btnVerifyWithDraw.setText("Approved");
        }else {
            binding.txtNotVerified.setVisibility(View.VISIBLE);
            binding.txtVerified.setVisibility(View.GONE);
        }

    }
}