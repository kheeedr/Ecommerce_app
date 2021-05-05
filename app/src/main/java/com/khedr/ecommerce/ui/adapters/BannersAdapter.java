package com.khedr.ecommerce.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.pojo.homeapi.Banner;
import com.khedr.ecommerce.operations.UiOperations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BannersAdapter extends RecyclerView.Adapter<BannersAdapter.BannersViewHolder> {

    List<Banner>bannersList=new ArrayList<>();
    Context context;
    private static final String TAG="BannersAdapter";

    public BannersAdapter(Context context) {
        this.context = context;
    }

    public void setBannersList(List<Banner> bannersList) {
        this.bannersList = bannersList;
        notifyDataSetChanged();
    }
//    public BannersAdapter(List<String> bannersList) {
//        this.bannersList = bannersList;
//    }

    @NonNull
    @NotNull
    @Override
    public BannersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new BannersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BannersAdapter.BannersViewHolder holder, int position) {

        UiOperations.getImageViaUrl(context,bannersList.get(position).getImage(), holder.iv,TAG,holder.ivProgressbar);

    }



    @Override
    public int getItemCount() {
        return bannersList.size();
    }
    static class BannersViewHolder extends RecyclerView.ViewHolder{

        ImageView iv,ivProgressbar;
        public BannersViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv =(ImageView) itemView.findViewById(R.id.iv_banner_item);
            ivProgressbar=itemView.findViewById(R.id.progress_banner_iv);

        }
    }
}
