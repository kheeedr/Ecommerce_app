/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.repository;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.data.local.AppDao;
import com.khedr.ecommerce.data.local.entities.RecentlyViewedEntity;
import com.khedr.ecommerce.data.romote.ApiInterface;
import com.khedr.ecommerce.data.model.Address.AddAddressResponse;
import com.khedr.ecommerce.data.model.Address.AddressData;
import com.khedr.ecommerce.data.model.Address.GetAddressesResponse;
import com.khedr.ecommerce.data.model.categories.GetCategoriesResponse;
import com.khedr.ecommerce.data.model.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.data.model.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.data.model.order.AddOrderRequest;
import com.khedr.ecommerce.data.model.order.AddOrderResponse;
import com.khedr.ecommerce.data.model.order.CancelOrderResponse;
import com.khedr.ecommerce.data.model.order.EstimateOrderRequest;
import com.khedr.ecommerce.data.model.order.EstimateOrderResponse;
import com.khedr.ecommerce.data.model.order.GetAllOrdersResponse;
import com.khedr.ecommerce.data.model.order.GetOrderDetailsResponse;
import com.khedr.ecommerce.data.model.product.ProductDetailsResponse;
import com.khedr.ecommerce.data.model.product.ProductId;
import com.khedr.ecommerce.data.model.product.cart.delete.DeleteFromCartResponse;
import com.khedr.ecommerce.data.model.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.data.model.product.cart.post.PostCartResponse;
import com.khedr.ecommerce.data.model.product.cart.update.Quantity;
import com.khedr.ecommerce.data.model.product.cart.update.UpdateQuantityResponse;
import com.khedr.ecommerce.data.model.product.favorites.get.GetFavoritesResponse;
import com.khedr.ecommerce.data.model.product.favorites.post.PostFavoriteResponse;
import com.khedr.ecommerce.data.model.product.search.SearchRequest;
import com.khedr.ecommerce.data.model.product.search.SearchResponse;
import com.khedr.ecommerce.data.model.promocode.PromoCodeRequest;
import com.khedr.ecommerce.data.model.promocode.PromoCodeResponse;
import com.khedr.ecommerce.data.model.user.TokenModel;
import com.khedr.ecommerce.data.model.user.UserApiResponse;
import com.khedr.ecommerce.data.model.user.UserDataForLoginRequest;
import com.khedr.ecommerce.data.model.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AppRepositoryImpl implements AppRepository {

    ApiInterface apiInterface;
    BaseApplication context;
    AppDao appDao;


    @Inject
    public AppRepositoryImpl(BaseApplication context, ApiInterface apiInterface, AppDao appDao) {
        this.context = context;
        this.apiInterface = apiInterface;
        this.appDao = appDao;
    }

    public Completable insert(RecentlyViewedEntity product) {
        return appDao.insert(product);
    }

    public Single<List<RecentlyViewedEntity>> getRecentProducts() {
        return appDao.getRecentProducts();
    }

    public void deleteDuplicates() {
        appDao.deleteDuplicates();
    }

    public void updateProduct(int id, boolean inCart) {
        appDao.updateProduct(id, inCart);
    }

    public void removeAllRecentProductFromCart() {
        appDao.removeAllRecentProductFromCart();
    }

    public void removeAllRecentProduct() {
        appDao.removeAllRecentProduct();
    }

    public Single<RecentlyViewedEntity> getProductById(int id) {
        return appDao.getProductById(id);
    }

    public String getLang() {
        return UiUtils.getAppLang(context);
    }

    public String getToken() {
        return UserUtils.getUserToken(context);
    }

    public Single<UserApiResponse> register(UserDataForRegisterRequest user) {

        return apiInterface.register(getLang(), user);
    }

    public Single<UserApiResponse> login(UserDataForLoginRequest user) {
        return apiInterface.login(getLang(), user);
    }

    public Single<UserApiResponse> logOut() {
        return apiInterface.logOut(getLang(), getToken(), new TokenModel(getToken()));
    }

    public Single<UserApiResponse> updateProfile(UserDataForRegisterRequest user) {
        return apiInterface.updateProfile(getLang(), getToken(), user);
    }

    public Single<UserApiResponse> getProfile() {
        return apiInterface.getProfile(getLang(), getToken());
    }

    public Single<HomePageApiResponse> getHomePage() {
        return apiInterface.getHomePage(getLang(), getToken());
    }

    public Single<PostFavoriteResponse> addToFavorite(ProductId id) {
        return apiInterface.addToFavorite(getLang(), getToken(), id);
    }

    public Single<GetFavoritesResponse> getFavorites() {
        return apiInterface.getFavorites(getLang(), getToken());
    }

    public Single<PostCartResponse> addToCart(ProductId product_id) {
        return apiInterface.addToCart(getLang(), getToken(), product_id);
    }

    public Single<GetCartResponse> getCart() {
        return apiInterface.getCart(getLang(), getToken());
    }

    public Single<UpdateQuantityResponse> updateQuantity(int product_id, Quantity quantity) {
        return apiInterface.updateQuantity(getLang(), getToken(), product_id, quantity);
    }

    public Single<DeleteFromCartResponse> deleteFromCart(int product_id) {
        return apiInterface.deleteFromCart(getLang(), getToken(), product_id);
    }

    public Single<GetCategoriesResponse> getCategories() {
        return apiInterface.getCategories(getLang());
    }

    public Single<GetCategoryItemsResponse> getCategoryItems(int id) {
        return apiInterface.getCategoryItems(getLang(), getToken(), id);
    }

    public Single<SearchResponse> getSearchProducts(SearchRequest searchRequest) {
        return apiInterface.getSearchProducts(getLang(), getToken(), searchRequest);
    }

    public Single<ProductDetailsResponse> getProductDetails(int id) {
        return apiInterface.getProductDetails(getLang(), getToken(), id);
    }

    public Single<AddOrderResponse> addOrder(AddOrderRequest orderRequest) {
        return apiInterface.addOrder(getLang(), getToken(), orderRequest);
    }

    public Single<EstimateOrderResponse> estimateOrder(EstimateOrderRequest estimateOrderRequest) {
        return apiInterface.estimateOrder(getLang(), getToken(), estimateOrderRequest);
    }

    public Single<GetAddressesResponse> getAddresses() {
        return apiInterface.getAddresses(getLang(), getToken());
    }

    public Single<AddAddressResponse> addAddress(AddressData addressData) {
        return apiInterface.addAddress(getLang(), getToken(), addressData);
    }

    public Single<AddAddressResponse> updateAddress(int id, AddressData addressData) {
        return apiInterface.updateAddress(getLang(), getToken(), id, addressData);
    }

    public Single<AddAddressResponse> deleteAddress(int id) {
        return apiInterface.deleteAddress(getLang(), getToken(), id);
    }

    // validate promo code
    public Single<PromoCodeResponse> validatePromoCode(PromoCodeRequest promoCodeRequest) {
        return apiInterface.validatePromoCode(getLang(), getToken(), promoCodeRequest);
    }

    public Single<GetAllOrdersResponse> getAllOrders() {
        return apiInterface.getAllOrders(getLang(), getToken());
    }

    public Single<GetOrderDetailsResponse> getOrderDetails(int id) {
        return apiInterface.getOrderDetails(getLang(), getToken(), id);
    }

    public Single<CancelOrderResponse> cancelOrder(int id) {
        return apiInterface.cancelOrder(getLang(), getToken(), id);
    }
}
