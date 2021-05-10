package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.khedr.ecommerce.databinding.ItemSearchSuggestionBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchSuggestionsAdapter extends RecyclerView.Adapter<SearchSuggestionsAdapter.SuggestionsViewHolder> {
    Context context;
    ArrayList<String> suggestionsList = new ArrayList<>();

    public void setSuggestionsList(ArrayList<String> suggestionsList) {
        this.suggestionsList = suggestionsList;
    }

    public SearchSuggestionsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SuggestionsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SuggestionsViewHolder(ItemSearchSuggestionBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchSuggestionsAdapter.SuggestionsViewHolder holder, int position) {

        holder.b.tvSearchSuggestion.setText(position + "- " + holder.b.tvSearchSuggestion.getText());
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
