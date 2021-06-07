package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemSearchSuggestionBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchSuggestionsAdapter extends RecyclerView.Adapter<SearchSuggestionsAdapter.SuggestionsViewHolder> {
    Context context;
    ArrayList<Product> suggestionsList = new ArrayList<>();

    public SearchSuggestionsAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchSuggestionsAdapter.SuggestionsViewHolder holder, int position) {
        holder.b.tvSearchSuggestion.setText(suggestionsList.get(position).getName());
        if(position==suggestionsList.size()){
            holder.b.dividerUnderSearchSuggestion.setVisibility(View.GONE);
        }
        holder.b.layoutSearchSuggestion.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("product", suggestionsList.get(position));
            context.startActivity(intent);
        });
    }

    public void setSuggestionsList(ArrayList<Product> suggestionsList) {
        this.suggestionsList = suggestionsList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SuggestionsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SuggestionsViewHolder(ItemSearchSuggestionBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }



    @Override
    public int getItemCount() {
        return suggestionsList.size();
    }

    public static class SuggestionsViewHolder extends RecyclerView.ViewHolder {
        ItemSearchSuggestionBinding b;

        public SuggestionsViewHolder(@NonNull @NotNull ItemSearchSuggestionBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
