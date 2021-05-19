package com.khedr.ecommerce.network;

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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    //user
    @Headers({ "Content-Type:application/json"})
    @POST("register")
    Call<UserApiResponse> register(@Header("lang") String lang,@Body UserDataForRegisterRequest user);

    @Headers({"Content-Type:application/json"})
    @POST("login")
    Call<UserApiResponse> login(@Header("lang") String lang,@Body UserDataForLoginRequest user);

    @Headers({ "Content-Type:application/json"})
    @POST("logout")
    Call<UserApiResponse> logOut(@Header("lang") String lang,@Header("Authorization") String token, @Body TokenModel tokenModel);

    @Headers({"lang:en", "Content-Type:application/json"})
    @PUT("update-profile")
    Call<UserApiResponse> updateProfile(@Header("lang") String lang,@Header("Authorization") String token, @Body UserDataForRegisterRequest user);

    //  Home
    @Headers({ "Content-Type:application/json"})
    @GET("home")
    Call<HomePageApiResponse> getHomePage(@Header("lang") String lang,@Header("Authorization") String token);

    // favorite
    @Headers({ "Content-Type:application/json"})
    @POST("favorites")
    Call<PostFavoriteResponse> addToFavorite(@Header("lang") String lang,@Header("Authorization") String token
            , @Body ProductId product_id);

    @Headers({ "Content-Type:application/json"})
    @GET("favorites")
    Call<GetFavoritesResponse> getFavorites(@Header("lang") String lang,@Header("Authorization") String token);

    // cart
    @Headers({ "Content-Type:application/json"})
    @POST("carts")
    Call<PostCartResponse> addToCart(@Header("lang") String lang,@Header("Authorization") String token
            , @Body ProductId product_id);

    @Headers({ "Content-Type:application/json"})
    @GET("carts")
    Call<GetCartResponse> getCart(@Header("lang") String lang,@Header("Authorization") String token);

    @Headers({ "Content-Type:application/json"})
    @PUT("carts/{product_id}")
    Call<UpdateQuantityResponse> updateQuantity(@Header("lang") String lang,@Header("Authorization") String token
            , @Path("product_id") int product_id, @Body Quantity quantity);

    // Categories
    @Headers({ "Content-Type:application/json"})
    @GET("categories")
    Call<GetCategoriesResponse> getCategories(@Header("lang") String lang);
    //items by category
    @Headers({ "Content-Type:application/json"})
    @GET("categories/{product_id}")
    Call<GetCategoryItemsResponse> getCategoryItems(@Header("lang") String lang,@Header("Authorization") String token
            , @Path("product_id") int product_id);

    // search
    @Headers({ "Content-Type:application/json"})
    @POST("products/search")
    Call<SearchResponse> getSearchProducts(@Header("lang") String lang,@Header("Authorization") String token
            , @Body SearchRequest searchRequest);
}
