package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.WebModel;

import java.util.ArrayList;
import java.util.List;

public class WebAdapter extends RecyclerView.Adapter<WebAdapter.ViewHolder> {
    List<WebModel> modelList = new ArrayList<>();
    OnWebListener onWebListener;

    int select = -1;
    int preSelect = -1;

    public void setOnWebListener(OnWebListener listener){
        onWebListener = listener;
    }

    public void setDate(List<WebModel> data){
        this.modelList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull WebAdapter.ViewHolder holder, int position) {
        WebModel webModel = modelList.get(position);

        holder.tvTitle.setText(webModel.getTitle());
        holder.tvLink.setText(webModel.getUrl());

        holder.itemView.setOnClickListener(v -> {
            if(onWebListener != null){
                onWebListener.onWebClick(webModel, position);
            }
        });

        if(position == select){
            holder.mask.setVisibility(View.VISIBLE);
        }else {
            holder.mask.setVisibility(View.GONE);
        }
    }

    public void setSelect(int i){
        preSelect = select;
        select = i;

        notifyItemChanged(preSelect);
        notifyItemChanged(select);
    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLink;
        View mask;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLink = itemView.findViewById(R.id.tvLink);
            mask = itemView.findViewById(R.id.mask);
        }
    }

    public interface OnWebListener{
        void onWebClick(WebModel webModel, int post);
    }
}
