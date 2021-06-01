package com.khedr.ecommerce.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ItemCategoriesBinding;
import com.khedr.ecommerce.pojo.categories.GetCategoriesInnerData;
import com.khedr.ecommerce.ui.activites.CategoryProductsActivity;
import com.khedr.ecommerce.utils.UiUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    Context context;
    ArrayList<GetCategoriesInnerData> categoriesList = new ArrayList<>();
    private static final String TAG = "ProductsAdapter";

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CategoriesAdapter.CategoriesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(ItemCategoriesBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoriesAdapter.CategoriesViewHolder holder, int position) {

        UiUtils.getImageViaUrl(context, categoriesList.get(position).getImage(), holder.b.ivCategory, holder.b.progressCategories);
        holder.b.tvCategoryName.setText(categoriesList.get(position).getName());
        holder.b.layoutCategory.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryProductsActivity.class);
            intent.putExtra(context.getString(R.string.category_name), categoriesList.get(position).getName());
            intent.putExtra(context.getString(R.string.category_id), categoriesList.get(position).getId());
            context.startActivity(intent);
        });

    }

    public void setCategoriesList(ArrayList<GetCategoriesInnerData> categoriesList) {
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding b;

        public CategoriesViewHolder(@NonNull @NotNull ItemCategoriesBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
