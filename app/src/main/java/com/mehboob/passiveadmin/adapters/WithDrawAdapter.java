package com.mehboob.passiveadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mehboob.passiveadmin.R;
import com.mehboob.passiveadmin.activitites.DepositDetailsActivity;
import com.mehboob.passiveadmin.activitites.WithDrawDetailsActivity;
import com.mehboob.passiveadmin.models.Withdraw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WithDrawAdapter extends RecyclerView.Adapter<WithDrawAdapter.WithDrawHolder> {
    private Context context;
    private ArrayList<Withdraw> withdraws;

    public WithDrawAdapter(Context context, ArrayList<Withdraw> withdraws) {
        this.context = context;
        this.withdraws = withdraws;
    }

    @NonNull
    @Override
    public WithDrawHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_withdraw, parent, false);
        return new WithDrawHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithDrawHolder holder, int position) {

        Withdraw withdraw = withdraws.get(position);
        Date date = new Date(Long.parseLong(withdraw.getTimeStamp()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(date);

        holder.txtWithDrawRequestDate.setText(dateString);
        holder.txtWithDrawRequestBalance.setText(withdraw.getWithDrawAmount());
        holder.txtWithDrawRequestAccount.setText(withdraw.getWithDrawAccountName());

        if (withdraw.isVerified()){
            holder.txtWithDrawRequestVerified.setVisibility(View.VISIBLE);
            holder.txtWithDrawRequestNotVerified.setVisibility(View.GONE);
        }else{
            holder.txtWithDrawRequestVerified.setVisibility(View.GONE);
            holder.txtWithDrawRequestNotVerified.setVisibility(View.VISIBLE);
        }

        holder.btnWithDrawRequestViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, WithDrawDetailsActivity.class);
            Gson gson = new Gson();
            String jsonUser = gson.toJson(withdraw);
            intent.putExtra("withdraw",jsonUser);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return withdraws.size();
    }

    public class WithDrawHolder extends RecyclerView.ViewHolder {
        private TextView txtWithDrawRequestBalance, txtWithDrawRequestDate, txtWithDrawRequestAccount, txtWithDrawRequestNotVerified, txtWithDrawRequestVerified, btnWithDrawRequestViewDetails;

        public WithDrawHolder(@NonNull View itemView) {
            super(itemView);

            txtWithDrawRequestBalance = itemView.findViewById(R.id.txtWithDrawRequestBalance);
            txtWithDrawRequestDate = itemView.findViewById(R.id.txtWithDrawRequestDate);
            txtWithDrawRequestAccount = itemView.findViewById(R.id.txtWithDrawRequestAccount);
            txtWithDrawRequestNotVerified = itemView.findViewById(R.id.txtWithDrawRequestNotVerified);
            txtWithDrawRequestVerified = itemView.findViewById(R.id.txtWithDrawRequestVerified);
            btnWithDrawRequestViewDetails = itemView.findViewById(R.id.btnWithDrawRequestViewDetails);
        }
    }
}
