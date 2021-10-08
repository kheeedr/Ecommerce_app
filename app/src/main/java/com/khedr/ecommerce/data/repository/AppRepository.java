/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.repository;


import com.khedr.ecommerce.data.local.entities.RecentlyViewedEntity;
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
import com.khedr.ecommerce.data.model.user.UserApiResponse;
import com.khedr.ecommerce.data.model.user.UserDataForLoginRequest;
import com.khedr.ecommerce.data.model.user.UserDataForRegisterRequest;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AppRepository {

    Completable insert(RecentlyViewedEntity product);

    Single<List<RecentlyViewedEntity>> getRecentProducts();

    void deleteDuplicates();

    void updateProduct(int id, boolean inCart) ;

    void removeAllRecentProductFromCart();

    void removeAllRecentProduct() ;

    Single<RecentlyViewedEntity> getProductById(int id) ;

    Single<UserApiResponse> register(UserDataForRegisterRequest user);

    Single<UserApiResponse> login(UserDataForLoginRequest user);

    Single<UserApiResponse> logOut();

    Single<UserApiResponse> updateProfile(UserDataForRegisterRequest user);

    Single<UserApiResponse> getProfile();

    Single<HomePageApiResponse> getHomePage();

    Single<PostFavoriteResponse> addToFavorite(ProductId id);

    Single<GetFavoritesResponse> getFavorites();

    Single<PostCartResponse> addToCart(ProductId product_id);

    Single<GetCartResponse> getCart();

    Single<UpdateQuantityResponse> updateQuantity(int product_id, Quantity quantity);

    Single<DeleteFromCartResponse> deleteFromCart(int product_id);

    Single<GetCategoriesResponse> getCategories();

    Single<GetCategoryItemsResponse> getCategoryItems(int id);

    Single<SearchResponse> getSearchProducts(SearchRequest searchRequest);

    Single<ProductDetailsResponse> getProductDetails(int id);

    Single<AddOrderResponse> addOrder(AddOrderRequest orderRequest);

    Single<EstimateOrderResponse> estimateOrder(EstimateOrderRequest estimateOrderRequest);

    Single<GetAddressesResponse> getAddresses();

    Single<AddAddressResponse> addAddress(AddressData addressData);

    Single<AddAddressResponse> updateAddress(int id, AddressData addressData);

    Single<AddAddressResponse> deleteAddress(int id);

    Single<PromoCodeResponse> validatePromoCode(PromoCodeRequest promoCodeRequest);

    Single<GetAllOrdersResponse> getAllOrders();

    Single<GetOrderDetailsResponse> getOrderDetails(int id);

    Single<CancelOrderResponse> cancelOrder(int id);


}
