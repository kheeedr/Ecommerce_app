package com.khedr.ecommerce.network;

import com.khedr.ecommerce.pojo.Address.AddAddressResponse;
import com.khedr.ecommerce.pojo.Address.AddressData;
import com.khedr.ecommerce.pojo.Address.GetAddressData;
import com.khedr.ecommerce.pojo.Address.GetAddressesResponse;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.pojo.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.pojo.order.AddOrderRequest;
import com.khedr.ecommerce.pojo.order.AddOrderResponse;
import com.khedr.ecommerce.pojo.product.ProductDetailsResponse;
import com.khedr.ecommerce.pojo.product.ProductId;
import com.khedr.ecommerce.pojo.product.cart.delete.DeleteFromCartResponse;
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

import io.reactivex.Single;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    //user
    @Headers({"Content-Type:application/json"})
    @POST("register")
    Single<UserApiResponse> register(@Header("lang") String lang, @Body UserDataForRegisterRequest user);

    @Headers({"Content-Type:application/json"})
    @POST("login")
    Single<UserApiResponse> login(@Header("lang") String lang, @Body UserDataForLoginRequest user);

    @Headers({"Content-Type:application/json"})
    @POST("logout")
    Single<UserApiResponse> logOut(@Header("lang") String lang, @Header("Authorization") String token, @Body TokenModel tokenModel);

    @Headers({"Content-Type:application/json"})
    @PUT("update-profile")
    Single<UserApiResponse> updateProfile(@Header("lang") String lang, @Header("Authorization") String token, @Body UserDataForRegisterRequest user);

    //  Get Profile
    @Headers({"Content-Type:application/json"})
    @GET("profile")
    Single<UserApiResponse> getProfile(@Header("lang") String lang, @Header("Authorization") String token);

    //  Home
    @Headers({"Content-Type:application/json"})
    @GET("home")
    Single<HomePageApiResponse> getHomePage(@Header("lang") String lang, @Header("Authorization") String token);

    // favorite
    @Headers({"Content-Type:application/json"})
    @POST("favorites")
    Single<PostFavoriteResponse> addToFavorite(@Header("lang") String lang, @Header("Authorization") String token
            , @Body ProductId product_id);

    @Headers({"Content-Type:application/json"})
    @GET("favorites")
    Single<GetFavoritesResponse> getFavorites(@Header("lang") String lang, @Header("Authorization") String token);

    // cart
    @Headers({"Content-Type:application/json"})
    @POST("carts")
    Single<PostCartResponse> addToCart(@Header("lang") String lang, @Header("Authorization") String token
            , @Body ProductId product_id);

    @Headers({"Content-Type:application/json"})
    @GET("carts")
    Single<GetCartResponse> getCart(@Header("lang") String lang, @Header("Authorization") String token);

    @Headers({"Content-Type:application/json"})
    @PUT("carts/{product_id}")
    Single<UpdateQuantityResponse> updateQuantity(@Header("lang") String lang, @Header("Authorization") String token
            , @Path("product_id") int product_id, @Body Quantity quantity);

    @Headers({"Content-Type:application/json"})
    @DELETE("carts/{product_id}")
    Single<DeleteFromCartResponse> deleteFromCart(@Header("lang") String lang, @Header("Authorization") String token
            , @Path("product_id") int product_id);

    // Categories
    @Headers({"Content-Type:application/json"})
    @GET("categories")
    Single<GetCategoriesResponse> getCategories(@Header("lang") String lang);

    //items by category
    @Headers({"Content-Type:application/json"})
    @GET("categories/{category_id}")
    Single<GetCategoryItemsResponse> getCategoryItems(@Header("lang") String lang, @Header("Authorization") String token
            , @Path("category_id") int category_id);

    // search
    @Headers({"Content-Type:application/json"})
    @POST("products/search")
    Single<SearchResponse> getSearchProducts(@Header("lang") String lang, @Header("Authorization") String token
            , @Body SearchRequest searchRequest);

    // product details
    @Headers({"Content-Type:application/json"})
    @GET("products/{product_id}")
    Single<ProductDetailsResponse> getProductDetails(@Header("lang") String lang, @Header("Authorization") String token
            , @Path("product_id") int product_id);

    // Add Order
    @Headers({"Content-Type:application/json"})
    @POST("orders")
    Single<AddOrderResponse> addOrder(@Header("lang") String lang, @Header("Authorization") String token
            , @Body AddOrderRequest orderRequest);

    // get Addresses
    @Headers({"Content-Type:application/json"})
    @GET("addresses")
    Single<GetAddressesResponse> getAddresses(@Header("lang") String lang, @Header("Authorization") String token);

    // add Address
    @Headers({"Content-Type:application/json"})
    @POST("addresses")
    Single<AddAddressResponse> addAddress(@Header("lang") String lang, @Header("Authorization") String token
            , @Body AddressData addressData);

    // edit Address
    @Headers({"Content-Type:application/json"})
    @PUT("addresses/{id}")
    Single<AddAddressResponse> updateAddress(@Header("lang") String lang, @Header("Authorization") String token, @Path("id") int id
            , @Body AddressData addressData);

    // delete Address
    @Headers({"Content-Type:application/json"})
    @DELETE("addresses/{id}")
    Single<AddAddressResponse> deleteAddress(@Header("lang") String lang, @Header("Authorization") String token, @Path("id") int id );
}
