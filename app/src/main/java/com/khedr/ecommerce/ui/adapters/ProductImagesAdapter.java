package com.khedr.ecommerce.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemProductImagesBinding;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ImagesViewHolder> {

    private static final String TAG = "ProductImagesAdapter";
    Context context;
    ArrayList<String> imagesList;


    public ProductImagesAdapter(Context context, ArrayList<String> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @NotNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ImagesViewHolder(ItemProductImagesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductImagesAdapter.ImagesViewHolder holder, int position) {
        UiUtils.getImageViaUrl(context, imagesList.get(position), holder.b.ivItemImages, TAG, holder.b.progressRvImagesIv);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class ImagesViewHolder extends RecyclerView.ViewHolder {

        ItemProductImagesBinding b;

        public ImagesViewHolder(@NonNull @NotNull ItemProductImagesBinding b) {
            super(b.getRoot());
            this.b = b;
        }


    }
}
