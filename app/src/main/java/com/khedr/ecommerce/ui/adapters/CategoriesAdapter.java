package com.khedr.ecommerce.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.pojo.categories.GetCategoriesInnerData;
import com.khedr.ecommerce.ui.CategoryProductsActivity;
import com.khedr.ecommerce.operations.UiOperations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    Context context;
    ArrayList<GetCategoriesInnerData> categoriesList=new ArrayList<>();
    private static final String TAG = "ProductsAdapter";

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    public void setCategoriesList(ArrayList<GetCategoriesInnerData> categoriesList) {
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public CategoriesAdapter.CategoriesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoriesAdapter.CategoriesViewHolder holder, int position) {

        UiOperations.getImageViaUrl(context,categoriesList.get(position).getImage(),holder.ivCategory,TAG,holder.progressbar);
        holder.tvCategory.setText(categoriesList.get(position).getName());
        holder.catLayout.setOnClickListener(v -> {
            Intent intent=new Intent(context, CategoryProductsActivity.class);
            intent.putExtra(context.getString(R.string.category_name),categoriesList.get(position).getName());
            intent.putExtra(context.getString(R.string.category_id),categoriesList.get(position).getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategory, progressbar;
        TextView tvCategory;
        ConstraintLayout catLayout;

        public CategoriesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.iv_category);
            tvCategory = itemView.findViewById(R.id.tv_category_name);
            catLayout=itemView.findViewById(R.id.layout_category);
            progressbar =itemView.findViewById(R.id.progress_categories);
        }
    }
}
