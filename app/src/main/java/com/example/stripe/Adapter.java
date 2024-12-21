package com.example.stripe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    ArrayList<Data> arrayList;
    Context context;

    public Adapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
               Data data = arrayList.get(position);
               if (data.getSender()==Utile.SENDBYME){
                   holder.layoutai.setVisibility(View.GONE);
                   holder.user.setText(data.getMessage());
               }
               else{
                   holder.layoutuser.setVisibility(View.GONE);
                   holder.ai.setText(data.getMessage());
               }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ai,user;
        LinearLayout layoutai,layoutuser;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ai =itemView.findViewById(R.id.sendAi);
            user = itemView.findViewById(R.id.senduser);
            layoutai = itemView.findViewById(R.id.Ai);
            layoutuser = itemView.findViewById(R.id.User);

        }
    }
}
