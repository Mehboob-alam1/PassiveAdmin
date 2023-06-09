package com.mehboob.passiveadmin.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.passiveadmin.databinding.ActivityAddPackagesBinding;
import com.mehboob.passiveadmin.models.Packages;

public class AddPackagesActivity extends AppCompatActivity {
    private ActivityAddPackagesBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPackagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseReference = FirebaseDatabase.getInstance().getReference("Packages");


        fetchBasicPackage();
        fetchStandardPackage();
        fetchPremium();
        binding.imgBack.setOnClickListener(v -> {
            finish();
        });


        binding.btnAddBasic.setOnClickListener(v -> {
            if (binding.etBasicProfitPercentage.getText().toString().isEmpty())
                Toast.makeText(this, "Enter profit percentage", Toast.LENGTH_SHORT).show();
            else if (binding.radioBasic.getCheckedRadioButtonId() == -1)
                Toast.makeText(this, "Select any option for daily or weekly", Toast.LENGTH_SHORT).show();
            else if (binding.etBasicStartRange.getText().toString().isEmpty())
                Toast.makeText(this, "Enter starting range", Toast.LENGTH_SHORT).show();
            else if (binding.etBasicLastRange.getText().toString().isEmpty())
                Toast.makeText(this, "Enter last range", Toast.LENGTH_SHORT).show();
            else {

                int selectedId = binding.radioBasic.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) findViewById(selectedId);

                uploadBasicPackage(binding.etBasicProfitPercentage.getText().toString(),
                        selectedButton.getText().toString()
                        , binding.etBasicStartRange.getText().toString(),
                        binding.etBasicLastRange.getText().toString());

            }


        });
        binding.btnAddPremium.setOnClickListener(v -> {
            if (binding.etPremiumProfitPercentage.getText().toString().isEmpty())
                Toast.makeText(this, "Enter profit percentage", Toast.LENGTH_SHORT).show();
            else if (binding.radioPremium.getCheckedRadioButtonId() == -1)
                Toast.makeText(this, "Select any option for daily or weekly", Toast.LENGTH_SHORT).show();
            else if (binding.etPremiumStartRange.getText().toString().isEmpty())
                Toast.makeText(this, "Enter starting range", Toast.LENGTH_SHORT).show();
            else if (binding.etPremiumLastRange.getText().toString().isEmpty())
                Toast.makeText(this, "Enter last range", Toast.LENGTH_SHORT).show();
            else {

                int selectedId = binding.radioPremium.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) findViewById(selectedId);

                uploadPremiumPackage(binding.etPremiumProfitPercentage.getText().toString(),
                        selectedButton.getText().toString()
                        , binding.etPremiumStartRange.getText().toString(),
                        binding.etPremiumLastRange.getText().toString());

            }
        });
        binding.btnAddStandard.setOnClickListener(v -> {
            if (binding.etStandardProfitPercentage.getText().toString().isEmpty())
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            else if (binding.radioStandard.getCheckedRadioButtonId() == -1)
                Toast.makeText(this, "Select any option for daily or weekly", Toast.LENGTH_SHORT).show();
            else if (binding.etStandardStartRange.getText().toString().isEmpty())
                Toast.makeText(this, "Enter starting range", Toast.LENGTH_SHORT).show();
            else if (binding.etStandardLastRange.getText().toString().isEmpty())
                Toast.makeText(this, "Enter last range", Toast.LENGTH_SHORT).show();
            else {

                int selectedId = binding.radioStandard.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) findViewById(selectedId);
                uploadStandardPackage(binding.etStandardProfitPercentage.getText().toString(),
                        selectedButton.getText().toString(),
                        binding.etStandardStartRange.getText().toString(),
                        binding.etStandardLastRange.getText().toString());


            }
        });
    }

    private void fetchPremium() {
        databaseReference.child("Premium").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Packages packages = snapshot.getValue(Packages.class);
                    binding.etPremiumProfitPercentage.setText(packages.getProfit());
                    if (packages.getPackageType().equals("daily"))
                        binding.radioPremiumDaily.setChecked(true);
                    else
                        binding.radioPremiumWeekly.setChecked(true);
                    binding.etPremiumStartRange.setText(packages.getStartRange());
                    binding.etPremiumLastRange.setText(packages.getLastRange());

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchStandardPackage() {
        databaseReference.child("Standard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Packages packages = snapshot.getValue(Packages.class);
                    binding.etStandardProfitPercentage.setText(packages.getProfit());
                    if (packages.getPackageType().equals("daily"))
                        binding.radioStandardDaily.setChecked(true);
                    else
                        binding.radioStandardWeekly.setChecked(true);
                    binding.etStandardStartRange.setText(packages.getStartRange());
                    binding.etStandardLastRange.setText(packages.getLastRange());

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchBasicPackage() {
        databaseReference.child("Basic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Packages packages = snapshot.getValue(Packages.class);
                    binding.etBasicProfitPercentage.setText(packages.getProfit());
                    if (packages.getPackageType().equals("daily"))
                        binding.radioBasicDaily.setChecked(true);
                    else
                        binding.radioBasicWeekly.setChecked(true);
                    binding.etBasicStartRange.setText(packages.getStartRange());
                    binding.etBasicLastRange.setText(packages.getLastRange());

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadPremiumPackage(String profit, String type, String startPrice, String endRate) {
        binding.textAddPremium.setVisibility(View.INVISIBLE);
        binding.progressAddPremium.setVisibility(View.VISIBLE);
        Packages packages = new Packages(profit, type, startPrice, endRate);
        databaseReference.child("Premium").setValue(packages)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        binding.textAddPremium.setVisibility(View.VISIBLE);
                        binding.progressAddPremium.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "Premium package added", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        binding.textAddPremium.setVisibility(View.VISIBLE);
                        binding.progressAddPremium.setVisibility(View.INVISIBLE);
                    }

                }).addOnFailureListener(e -> {
                    binding.textAddPremium.setVisibility(View.VISIBLE);
                    binding.progressAddPremium.setVisibility(View.INVISIBLE);

                });
    }

    private void uploadStandardPackage(String profit, String type, String startPrice, String endRate) {
        binding.textAddStandard.setVisibility(View.INVISIBLE);
        binding.progressAddStandard.setVisibility(View.VISIBLE);
        Packages packages = new Packages(profit, type, startPrice, endRate);
        databaseReference.child("Standard").setValue(packages)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        binding.textAddStandard.setVisibility(View.VISIBLE);
                        binding.progressAddStandard.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "Standard package added", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        binding.textAddStandard.setVisibility(View.VISIBLE);
                        binding.progressAddStandard.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(e -> {
                    binding.textAddStandard.setVisibility(View.VISIBLE);
                    binding.progressAddStandard.setVisibility(View.INVISIBLE);
                });
    }

    private void uploadBasicPackage(String profit, String type, String startPrice, String endRate) {
        binding.textAddBasic.setVisibility(View.INVISIBLE);
        binding.progressAddBasic.setVisibility(View.VISIBLE);
        Packages packages = new Packages(profit, type, startPrice, endRate);

        databaseReference.child("Basic").setValue(packages)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        binding.textAddBasic.setVisibility(View.VISIBLE);
                        binding.progressAddBasic.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "Basic package added", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        binding.textAddBasic.setVisibility(View.VISIBLE);
                        binding.progressAddBasic.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(e -> {
                    binding.textAddBasic.setVisibility(View.VISIBLE);
                    binding.progressAddBasic.setVisibility(View.INVISIBLE);
                });

    }
}