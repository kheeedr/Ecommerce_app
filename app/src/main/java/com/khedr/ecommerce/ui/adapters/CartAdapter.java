package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCartBinding;
import com.khedr.ecommerce.operations.UiOperations;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartItems;
import com.khedr.ecommerce.operations.ProductOperations;
import com.khedr.ecommerce.operations.UserOperations;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
//import com.khedr.ecommerce.pojo.product.ProductId;
//import com.khedr.ecommerce.pojo.product.cart.post.PostCartResponse;
//import com.khedr.ecommerce.network.ApiInterface;
//import com.khedr.ecommerce.network.RetrofitInstance;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import static com.khedr.ecommerce.ui.CartActivity.total;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public static final String TAG = "CartAdapter";
    List<GetCartItems> cartItems = new ArrayList<>();
    Context context;
    SharedPreferences pref;
    ActivityCartBinding b;

    public CartAdapter(Context context, ActivityCartBinding b) {
        this.context = context;
        this.b = b;
        pref = UserOperations.getPref(context);
    }


    public void setCartItems(List<GetCartItems> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.CartViewHolder holder, int position) {

        //set product image
        UiOperations.getImageViaUrl(context,cartItems.get(position).getProduct().getImage(),holder.iv,TAG);

        //set product name and price;
        holder.tvProductPrice.setText("EGP " + cartItems.get(position).getProduct().getPrice());
        if (cartItems.get(position).getProduct().getDiscount() > 0) {
            holder.tvOldPrice.setText(String.valueOf(cartItems.get(position).getProduct().getOld_price()));
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText((int) cartItems.get(position).getProduct().getDiscount() + "%");
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
            holder.tvDiscount.setVisibility(View.GONE);
        }
        holder.tvProductName.setText(cartItems.get(position).getProduct().getName());
        holder.quantity.setText(String.valueOf(cartItems.get(position).getQuantity()));
//        holder.deleteFromCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.deleteFromCart.setVisibility(View.GONE);
//                holder.progressBar.setVisibility(View.VISIBLE);
//                b.layoutCartProgressBack.setVisibility(View.VISIBLE);
//
//                b.progressCart.setVisibility(View.VISIBLE);
//                deleteProductFromCart(position, holder);
//            }
//        });
        holder.btPLus.setOnClickListener(v -> {
            int oldQ = Integer.parseInt(holder.quantity.getText().toString());
            int newQ = oldQ + 1;
            holder.quantity.setText(String.valueOf(newQ));
            int id = cartItems.get(position).getId();
            ProductOperations.onClickUpdateQuantity(context,newQ, oldQ, id, holder.quantity,TAG,b.tvCartTotal);
        });
        holder.btMinus.setOnClickListener(v -> {
            int oldQ = Integer.parseInt(holder.quantity.getText().toString());
            if (oldQ > 1) {
                int newQ = oldQ - 1;
                holder.quantity.setText(String.valueOf(newQ));
                int id = cartItems.get(position).getId();
                ProductOperations.onClickUpdateQuantity(context,newQ, oldQ, id, holder.quantity,TAG,b.tvCartTotal);

            }

        });

    }


//    public void deleteProductFromCart(int position, CartViewHolder holder) {
//        if (UserOperations.isSignedIn(context)) {
//            int id = cartItems.get(position).getProduct().getId();
//            ProductId productId = new ProductId(id);
//            String token = pref.getString(context.getString(R.string.pref_user_token), "");
//            Call<PostCartResponse> call = RetrofitInstance.getRetrofitInstance()
//                    .create(ApiInterface.class).addToCart(token, productId);
//            call.enqueue(new Callback<PostCartResponse>() {
//                @Override
//                public void onResponse(Call<PostCartResponse> call, Response<PostCartResponse> response) {
//                    if (response.body().isStatus()) {
//                        cartItems.remove(position);
//                        notifyDataSetChanged();
//                        if (!cartItems.isEmpty()) {
//                            setCartItems(cartItems);
//                            b.layoutCartFilled.setVisibility(View.VISIBLE);
//                            b.layoutCartEmpty.setVisibility(View.GONE);
//                            total -= (response.body().getData().getQuantity()) * (response.body().getData().getProduct().getPrice());
//                            b.tvCartTotal.setText("Total: " + String.valueOf((int) total) + " EGP");
//                        } else {
//                            b.layoutCartFilled.setVisibility(View.GONE);
//                            b.layoutCartEmpty.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    holder.deleteFromCart.setVisibility(View.VISIBLE);
//                    holder.progressBar.setVisibility(View.GONE);
//                    b.layoutCartProgressBack.setVisibility(View.GONE);
//                    b.progressCart.setVisibility(View.GONE);
//                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<PostCartResponse> call, Throwable t) {
//                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
//                    holder.deleteFromCart.setVisibility(View.VISIBLE);
//                    holder.progressBar.setVisibility(View.GONE);
//                    b.layoutCartProgressBack.setVisibility(View.GONE);
//                    b.progressCart.setVisibility(View.GONE);
//
//                }
//            });
//        } else {
//            Toast.makeText(context, "you should login first", Toast.LENGTH_LONG).show();
//        }
//    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView iv, deleteFromCart;
        TextView tvProductPrice, tvOldPrice, tvProductName, tvDiscount;
        TextView quantity;
        LinearLayout btPLus, btMinus;
        ProgressBar progressBar;

        public CartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_cart_product);
            tvProductName = itemView.findViewById(R.id.tv_cart_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_cart_price);
            tvOldPrice = itemView.findViewById(R.id.tv_cart_old_price);
            tvDiscount = itemView.findViewById(R.id.tv_cart_discount);
            deleteFromCart = itemView.findViewById(R.id.iv_cart_delete);
            quantity = itemView.findViewById(R.id.tv_cart_editable_quantity);
            btPLus = itemView.findViewById(R.id.layout_cart_plus);
            btMinus = itemView.findViewById(R.id.layout_cart_negative);
            progressBar = itemView.findViewById(R.id.progress_cart_delete);
        }
    }
}

//        holder.deleteFromCart.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (v.getId() == R.id.layout_product_to_cart) {
//                    addProductToFavorite(position);
//                }
//                return false;
//            }
//        });

//    public void addProductToFavorite(int position) {
//
//        if (isSignedIn()) {
//            int id = SplashActivity.homeResponse.getData().getProducts().get(position).getId();
//            ProductId productId = new ProductId(id);
//            String token = pref.getString(context.getString(R.string.pref_user_token), "");
//            Call<PostFavoriteResponse> call = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).addToFavorite(token, productId);
//            call.enqueue(new Callback<PostFavoriteResponse>() {
//                @Override
//                public void onResponse(Call<PostFavoriteResponse> call, Response<PostFavoriteResponse> response) {
//
//                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onFailure(Call<PostFavoriteResponse> call, Throwable t) {
//                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//        } else {
//            Toast.makeText(context, "you should login first", Toast.LENGTH_LONG).show();
//        }
//    }