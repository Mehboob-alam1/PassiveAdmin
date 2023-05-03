package com.mehboob.passiveadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mehboob.passiveadmin.R;
import com.mehboob.passiveadmin.activitites.UserDepositsActivity;
import com.mehboob.passiveadmin.activitites.UserDetailActivity;
import com.mehboob.passiveadmin.models.User;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private Context context;
    private ArrayList<User> list;
    private String whichActivity;

    public UserAdapter(Context context, ArrayList<User> list, String whichActivity) {
        this.context = context;
        this.list = list;
        this.whichActivity = whichActivity;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_users, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        User user = list.get(position);

        try {
            Glide.with(context)
                    .load(user.getUser_image())
                    .into(holder.imgUser);
        } catch (IllegalArgumentException e) {

        }

        holder.txtName.setText(user.getFirst_name() + " " + user.getSur_name());
        holder.txtContact.setText(user.getPhone_number());
        if (whichActivity.equals("All")) {
            Gson gson = new Gson();
            String userData = gson.toJson(user);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("user", userData);
                context.startActivity(intent);
            });
        } else {
            holder.itemView.setOnClickListener(v -> {

                Intent i = new Intent(context, UserDepositsActivity.class);

                i.putExtra("uid",user.getUser_id());
                context.startActivity(i);

            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtContact;
        private ImageView imgUser;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtUser);
            txtContact = itemView.findViewById(R.id.txtContactNumber);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }
}
