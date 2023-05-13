package com.mehboob.passiveadmin.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.passiveadmin.databinding.ActivityAddAccountsBinding;

import com.mehboob.passiveadmin.models.Accounts;

public class AddAccountsActivity extends AppCompatActivity {
    private ActivityAddAccountsBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAccountsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");


        binding.btnAddDetails.setOnClickListener(v -> {
            validate();
        });

        fetchData();

    }

    private void fetchData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Accounts accounts = snapshot.getValue(Accounts.class);
                    binding.etBankAccountName.setText(accounts.getBankAccountName());
                    binding.etBankAccountNumber.setText(accounts.getBankAccountNumber());
                    binding.etJazzCashAccountNumber.setText(accounts.getJazzAccountNumber());
                    binding.etJazzCashAccountName.setText(accounts.getJazzAccountName());
                    binding.etEasyPaisaAccountName.setText(accounts.getEasyAccountName());
                    binding.etEasyPaisaAccountNumber.setText(accounts.getEasyAccountNumber());
                    binding.etBankName.setText(accounts.getBankName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void validate() {


        if (binding.etBankAccountNumber.getText().toString().isEmpty())
            Toast.makeText(this, "Enter bank account number", Toast.LENGTH_SHORT).show();
        else if (binding.etBankAccountName.getText().toString().isEmpty())
            Toast.makeText(this, "Enter bank account name", Toast.LENGTH_SHORT).show();
        else if (binding.etJazzCashAccountNumber.getText().toString().isEmpty())
            Toast.makeText(this, "Enter jazz cash account number", Toast.LENGTH_SHORT).show();
        else if (binding.etJazzCashAccountName.getText().toString().isEmpty())
            Toast.makeText(this, "Enter jazz cash account name", Toast.LENGTH_SHORT).show();
        else if (binding.etEasyPaisaAccountNumber.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Easy Paisa account number", Toast.LENGTH_SHORT).show();
        else if (binding.etEasyPaisaAccountName.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Easy Paisa account name", Toast.LENGTH_SHORT).show();
        else if (binding.etBankName.getText().toString().isEmpty())
            Toast.makeText(this, "Enter bank name", Toast.LENGTH_SHORT).show();

        else
            addAccount(binding.etBankAccountNumber.getText().toString(), binding.etBankAccountName.getText().toString(),
                    binding.etBankName.getText().toString(),
                    binding.etJazzCashAccountNumber.getText().toString(), binding.etJazzCashAccountName.getText().toString(),
                    binding.etEasyPaisaAccountNumber.getText().toString(), binding.etEasyPaisaAccountName.getText().toString());
    }

    private void addAccount(String bankAccountNumber, String bankAccountName, String bankName,String jazzAccountNumber, String jazzAccountName, String easyAccount, String easyName) {
        binding.textAddDetails.setVisibility(View.INVISIBLE);
        binding.progressAddDetails.setVisibility(View.VISIBLE);
        Accounts accounts = new Accounts(bankAccountNumber, bankAccountName,bankName, jazzAccountNumber, jazzAccountName, easyAccount, easyName);
        databaseReference.setValue(accounts).addOnCompleteListener(task -> {
            if (task.isComplete() && task.isSuccessful()) {
                binding.textAddDetails.setVisibility(View.VISIBLE);
                binding.progressAddDetails.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Details Added", Toast.LENGTH_SHORT).show();

            } else {
                binding.textAddDetails.setVisibility(View.INVISIBLE);
                binding.progressAddDetails.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(e -> {
            binding.textAddDetails.setVisibility(View.VISIBLE);
            binding.progressAddDetails.setVisibility(View.INVISIBLE);
        });

    }
}