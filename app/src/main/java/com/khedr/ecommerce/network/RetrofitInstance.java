package com.khedr.ecommerce.network;

import android.content.Context;

import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.pojo.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.pojo.product.ProductId;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.pojo.product.cart.post.PostCartResponse;
import com.khedr.ecommerce.pojo.product.cart.update.Quantity;
import com.khedr.ecommerce.pojo.product.cart.update.UpdateQuantityResponse;
import com.khedr.ecommerce.pojo.product.favorites.get.GetFavoritesResponse;
import com.khedr.ecommerce.pojo.product.favorites.post.PostFavoriteResponse;
import com.khedr.ecommerce.pojo.product.search.SearchRequest;
import com.khedr.ecommerce.pojo.product.search.SearchResponse;
import com.khedr.ecommerce.pojo.user.TokenModel;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.pojo.user.UserDataForLoginRequest;
import com.khedr.ecommerce.pojo.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.utils.UiUtils;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitInstance {
    private static final String BASE_URL = "https://student.valuxapps.com/api/";
    public static RetrofitInstance INSTANCE;
    private final ApiInterface apiInterface;

    public RetrofitInstance() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitInstance getRetrofitInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitInstance();
        }
        return INSTANCE;
    }

    public String getLang(Context context) {
        return UiUtils.getAppLang(context);
    }

    public Single<UserApiResponse> register(Context context, UserDataForRegisterRequest user) {

        return apiInterface.register(getLang(context), user);
    }

    public Single<UserApiResponse> login(Context context, UserDataForLoginRequest user) {
        return apiInterface.login(getLang(context), user);
    }

    public Single<UserApiResponse> logOut(Context context, String token, TokenModel tokenModel) {
        return apiInterface.logOut(getLang(context), token, tokenModel);
    }

    public Single<UserApiResponse> updateProfile(Context context, String token, UserDataForRegisterRequest user) {
        return apiInterface.updateProfile(getLang(context), token, user);
    }

    public Call<GetCartResponse> getCart(Context context, String token) {
        return apiInterface.getCart(getLang(context), token);
    }

    public Call<GetCategoriesResponse> getCategories(Context context) {
        return apiInterface.getCategories(getLang(context));
    }

    public Call<GetCategoryItemsResponse> getCategoryItems(Context context, String token, int id) {
        return apiInterface.getCategoryItems(getLang(context), token, id);
    }

    public Call<GetFavoritesResponse> getFavorites(Context context, String token) {
        return apiInterface.getFavorites(getLang(context), token);
    }

    public Single<HomePageApiResponse> getHomePage(Context context, String token) {
        return apiInterface.getHomePage(getLang(context), token);
    }


    public Call<PostCartResponse> addToCart(Context context, String token, ProductId product_id) {
        return apiInterface.addToCart(getLang(context), token, product_id);
    }

    public Call<UpdateQuantityResponse> updateQuantity(Context context, String token, int product_id, Quantity quantity) {
        return apiInterface.updateQuantity(getLang(context), token, product_id, quantity);
    }


    public Call<PostFavoriteResponse> addToFavorite(Context context, String token, ProductId id) {
        return apiInterface.addToFavorite(getLang(context), token, id);
    }


    public Call<SearchResponse> getSearchProducts(Context context, String token, SearchRequest searchRequest) {
        return apiInterface.getSearchProducts(getLang(context), token, searchRequest);
    }
}
