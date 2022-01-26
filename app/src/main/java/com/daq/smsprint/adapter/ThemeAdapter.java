package com.daq.smsprint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daq.smsprint.databinding.ItemTemplateBinding;
import com.daq.smsprint.models.ThemeModel;

import java.util.ArrayList;
import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> {
    private Context mContext;
    private List<ThemeModel> mTheme = new ArrayList<>();
    private OnClickTheme onClickTheme;

    public ThemeAdapter(Context context, OnClickTheme onClickTheme, List<ThemeModel> themeModels) {
        this.mContext = context;
        this.onClickTheme = onClickTheme;
        this.mTheme = themeModels;
    }

    public void setData(List<ThemeModel> themeModels) {
        this.mTheme = themeModels;
        notifyDataSetChanged();
    }

    public interface OnClickTheme {
        void onClick(ThemeModel themeModel, int position);
    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTemplateBinding itemTemplateBinding = ItemTemplateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ThemeAdapter.ThemeViewHolder(itemTemplateBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        holder.binData(mTheme.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mTheme.size();
    }


    public class ThemeViewHolder extends RecyclerView.ViewHolder {
        ItemTemplateBinding binding;

        public ThemeViewHolder(@NonNull ItemTemplateBinding itemTemplateBinding) {
            super(itemTemplateBinding.getRoot());
            this.binding = itemTemplateBinding;
        }

        public void binData(ThemeModel themeModel, int position) {
            Glide.with(mContext).load(themeModel.getBackgroundDrawble()).into(binding.imgTheme);
            binding.imgTheme.setOnClickListener(v -> {
                onClickTheme.onClick(themeModel, position);
            });
        }

    }
}
