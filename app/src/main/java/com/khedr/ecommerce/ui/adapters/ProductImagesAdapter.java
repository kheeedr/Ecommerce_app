package com.khedr.ecommerce.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.ui.operations.UiOperations;

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
        return new ImagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductImagesAdapter.ImagesViewHolder holder, int position) {
        UiOperations.getImageViaUrl(context, imagesList.get(position), holder.imageView, TAG, holder.progressBar);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView progressBar;

        public ImagesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_itemImages);
            progressBar = itemView.findViewById(R.id.progress_rvImages_iv);
        }
    }
}
