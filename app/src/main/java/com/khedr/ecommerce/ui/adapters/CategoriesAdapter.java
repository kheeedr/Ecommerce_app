package com.khedr.ecommerce.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ItemCategoriesBinding;
import com.khedr.ecommerce.pojo.categories.GetCategoriesInnerData;
import com.khedr.ecommerce.ui.activities.categories.CategoryProductsActivity;
import com.khedr.ecommerce.utils.UiUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    Context context;
    ArrayList<GetCategoriesInnerData> categoriesList = new ArrayList<>();
    OnItemClickListener mOnItemClickListener;

    public CategoriesAdapter(Context context) {
        this.context = context;
        this.mOnItemClickListener=(OnItemClickListener)context;
    }

    @NonNull
    @NotNull
    @Override
    public CategoriesAdapter.CategoriesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(ItemCategoriesBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false),mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoriesAdapter.CategoriesViewHolder holder, int position) {

        UiUtils.getImageViaUrl(context, categoriesList.get(position).getImage(), holder.b.ivCategory, holder.b.progressCategories);
        holder.b.tvCategoryName.setText(categoriesList.get(position).getName());

    }

    public void setCategoriesList(ArrayList<GetCategoriesInnerData> categoriesList) {
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }

    public ArrayList<GetCategoriesInnerData> getList() {
        return categoriesList;
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemCategoriesBinding b;
        OnItemClickListener onItemClickListener;
        public CategoriesViewHolder(@NonNull @NotNull ItemCategoriesBinding b, OnItemClickListener onItemClickListener) {
            super(b.getRoot());
            this.b = b;
            this.onItemClickListener=onItemClickListener;
            b.itemCategoryParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
