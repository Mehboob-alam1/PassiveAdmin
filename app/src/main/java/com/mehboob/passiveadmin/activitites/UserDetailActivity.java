package com.mehboob.passiveadmin.activitites;

import static com.mehboob.passiveadmin.R.drawable.ic_baseline_not_verified_alt_24;
import static com.mehboob.passiveadmin.R.drawable.ic_baseline_verified_24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.passiveadmin.R;
import com.mehboob.passiveadmin.databinding.ActivityUserDetailBinding;
import com.mehboob.passiveadmin.models.User;

import java.lang.reflect.Type;

public class UserDetailActivity extends AppCompatActivity {
    private ActivityUserDetailBinding binding;
    private User user;
    private Gson gson;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gson = new Gson();
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        String jsonObj = getIntent().getStringExtra("user");
        Type type = new TypeToken<User>() {
        }.getType();
        user = gson.fromJson(jsonObj, type);


        binding.imgBack.setOnClickListener(v -> {
            finish();
        });
        try {
            Glide.with(this)
                    .load(user.getUser_image())
                    .into(binding.imgUserProfile);
        } catch (IllegalArgumentException e) {

        }

        if (user.isBlock()) {
            binding.btnBlockUser.setText("Unblock User");
            binding.imgVerification.setImageResource(ic_baseline_not_verified_alt_24);
        } else {
            binding.imgVerification.setImageResource(ic_baseline_verified_24);
            binding.btnBlockUser.setText("Block User");

        }

        binding.txtUserName.setText(user.getFirst_name() + " " + user.getSur_name());
        binding.txtUserAddress.setText(user.getAddress());
        binding.txtUserEmail.setText(user.getEmail());
        binding.txtUserPhoneNumber.setText(user.getPhone_number());
        binding.txtUserReferralID.setText(user.getReferral_id());
        binding.txtUserPassword.setText(user.getPassword());

        try {
            Glide.with(this)
                    .load(user.getCnic_front())
                    .into(binding.imgCnicFront);

        } catch (IllegalArgumentException e) {

        }
        try {
            Glide.with(this)
                    .load(user.getCnic_back())
                    .into(binding.imgCnicBack);

        } catch (IllegalArgumentException e) {

        }


        binding.btnBlockUser.setOnClickListener(v -> {
            blockUnblock(user.getUser_id(), user.isBlock());
        });


    }

    @SuppressLint("MissingInflatedId")
    private void blockUnblock(String user_id, boolean block) {


        if (block) {
            // unblock


            userRef.child(user_id).child("block").setValue(false).addOnCompleteListener(task -> {
                if (task.isComplete() && task.isSuccessful()) {

                    Toast.makeText(UserDetailActivity.this, "User unblocked", Toast.LENGTH_SHORT).show();
                    binding.btnBlockUser.setText("Block User");
                    userRef.child(user_id).child("reasonBlocked").setValue("");

                    binding.imgVerification.setImageResource(ic_baseline_verified_24);

                } else {

                    Toast.makeText(UserDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    binding.btnBlockUser.setText("Unblock User");
                    binding.imgVerification.setImageResource(ic_baseline_not_verified_alt_24);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(UserDetailActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.btnBlockUser.setText("Unblock User");
                binding.imgVerification.setImageResource(ic_baseline_not_verified_alt_24);
            });
        } else {
            //block
            BottomSheetDialog dialog = new BottomSheetDialog(UserDetailActivity.this, R.style.AppBottomSheetDialogTheme);
            View bottomsheetView = LayoutInflater.from(UserDetailActivity.this).
                    inflate(R.layout.bottom_sheet_balance, (CardView) findViewById(R.id.cardBalance));
            dialog.setContentView(bottomsheetView);
            dialog.show();

            EditText editText = bottomsheetView.findViewById(R.id.etBlock);
            AppCompatButton button = bottomsheetView.findViewById(R.id.btnNextBlock);

            button.setOnClickListener(v -> {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter the reason", Toast.LENGTH_SHORT).show();
                } else {
                    String reason = editText.getText().toString();
                    dialog.dismiss();
                    userRef.child(user_id).child("block").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete() && task.isSuccessful()) {
                                userRef.child(user_id).child("reasonBlocked").setValue(reason);
                                Toast.makeText(UserDetailActivity.this, "User unblocked", Toast.LENGTH_SHORT).show();
                                binding.btnBlockUser.setText("Unblock User");
                                binding.imgVerification.setImageResource(ic_baseline_not_verified_alt_24);


                            } else {

                                Toast.makeText(UserDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                binding.btnBlockUser.setText("Block User");
                                binding.imgVerification.setImageResource(ic_baseline_verified_24);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserDetailActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            binding.btnBlockUser.setText("Block User");
                            binding.imgVerification.setImageResource(ic_baseline_verified_24);
                        }
                    });

                }
            });
        }
    }
}