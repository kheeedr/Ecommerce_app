package com.khedr.ecommerce.network;

import android.content.Context;

import com.khedr.ecommerce.pojo.Address.AddAddressResponse;
import com.khedr.ecommerce.pojo.Address.AddressData;
import com.khedr.ecommerce.pojo.Address.GetAddressesResponse;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.pojo.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.pojo.order.AddOrderRequest;
import com.khedr.ecommerce.pojo.order.AddOrderResponse;
import com.khedr.ecommerce.pojo.order.EstimateOrderRequest;
import com.khedr.ecommerce.pojo.order.EstimateOrderResponse;
import com.khedr.ecommerce.pojo.order.GetAllOrdersResponse;
import com.khedr.ecommerce.pojo.order.GetOrderDetailsResponse;
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
import com.khedr.ecommerce.pojo.promocode.PromoCodeRequest;
import com.khedr.ecommerce.pojo.promocode.PromoCodeResponse;
import com.khedr.ecommerce.pojo.user.TokenModel;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.pojo.user.UserDataForLoginRequest;
import com.khedr.ecommerce.pojo.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.pojo.order.CancelOrderResponse;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import io.reactivex.Single;
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

    public String getToken(Context context) {
        return UserUtils.getUserToken(context);
    }


    public Single<UserApiResponse> register(Context context, UserDataForRegisterRequest user) {

        return apiInterface.register(getLang(context), user);
    }

    public Single<UserApiResponse> login(Context context, UserDataForLoginRequest user) {
        return apiInterface.login(getLang(context), user);
    }

    public Single<UserApiResponse> logOut(Context context) {
        return apiInterface.logOut(getLang(context), getToken(context), new TokenModel(getToken(context)));
    }

    public Single<UserApiResponse> updateProfile(Context context, UserDataForRegisterRequest user) {
        return apiInterface.updateProfile(getLang(context), getToken(context), user);
    }

    public Single<UserApiResponse> getProfile(Context context) {
        return apiInterface.getProfile(getLang(context), getToken(context));
    }

    public Single<HomePageApiResponse> getHomePage(Context context) {
        return apiInterface.getHomePage(getLang(context), getToken(context));
    }

    public Single<PostFavoriteResponse> addToFavorite(Context context, ProductId id) {
        return apiInterface.addToFavorite(getLang(context), getToken(context), id);
    }

    public Single<GetFavoritesResponse> getFavorites(Context context) {
        return apiInterface.getFavorites(getLang(context), getToken(context));
    }

    public Single<PostCartResponse> addToCart(Context context, ProductId product_id) {
        return apiInterface.addToCart(getLang(context), getToken(context), product_id);
    }

    public Single<GetCartResponse> getCart(Context context) {
        return apiInterface.getCart(getLang(context), getToken(context));
    }

    public Single<UpdateQuantityResponse> updateQuantity(Context context, int product_id, Quantity quantity) {
        return apiInterface.updateQuantity(getLang(context), getToken(context), product_id, quantity);
    }

    public Single<DeleteFromCartResponse> deleteFromCart(Context context, int product_id) {
        return apiInterface.deleteFromCart(getLang(context), getToken(context), product_id);
    }

    public Single<GetCategoriesResponse> getCategories(Context context) {
        return apiInterface.getCategories(getLang(context));
    }

    public Single<GetCategoryItemsResponse> getCategoryItems(Context context, int id) {
        return apiInterface.getCategoryItems(getLang(context), getToken(context), id);
    }

    public Single<SearchResponse> getSearchProducts(Context context, SearchRequest searchRequest) {
        return apiInterface.getSearchProducts(getLang(context), getToken(context), searchRequest);
    }

    public Single<ProductDetailsResponse> getProductDetails(Context context, int id) {
        return apiInterface.getProductDetails(getLang(context), getToken(context), id);
    }

    public Single<AddOrderResponse> addOrder(Context context, AddOrderRequest orderRequest) {
        return apiInterface.addOrder(getLang(context), getToken(context), orderRequest);
    }

    public Single<EstimateOrderResponse> estimateOrder(Context context, EstimateOrderRequest estimateOrderRequest) {
        return apiInterface.estimateOrder(getLang(context), getToken(context), estimateOrderRequest);
    }

    public Single<GetAddressesResponse> getAddresses(Context context) {
        return apiInterface.getAddresses(getLang(context), getToken(context));
    }

    public Single<AddAddressResponse> addAddress(Context context, AddressData addressData) {
        return apiInterface.addAddress(getLang(context), getToken(context), addressData);
    }

    public Single<AddAddressResponse> updateAddress(Context context, int id, AddressData addressData) {
        return apiInterface.updateAddress(getLang(context), getToken(context), id, addressData);
    }

    public Single<AddAddressResponse> deleteAddress(Context context, int id) {
        return apiInterface.deleteAddress(getLang(context), getToken(context), id);
    }

    // validate promo code
    public Single<PromoCodeResponse> validatePromoCode(Context context, PromoCodeRequest promoCodeRequest) {
        return apiInterface.validatePromoCode(getLang(context), getToken(context), promoCodeRequest);
    }

    public Single<GetAllOrdersResponse> getAllOrders(Context context) {
        return apiInterface.getAllOrders(getLang(context), getToken(context));
    }
    public Single<GetOrderDetailsResponse> getOrderDetails(Context context, int  id){
        return apiInterface.getOrderDetails(getLang(context), getToken(context),id);
    }
    public Single<CancelOrderResponse> cancelOrder(Context context, int  id){
        return apiInterface.cancelOrder(getLang(context), getToken(context),id);
    }
}
