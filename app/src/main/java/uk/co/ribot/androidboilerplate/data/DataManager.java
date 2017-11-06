package uk.co.ribot.androidboilerplate.data;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.data.model.net.request.EmptyRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.HomePageBannerRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.LoginRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.DashBoardResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.EmptyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.HomePageBannerResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LastBuyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.data.remote.RunwiseService;

@Singleton
public class DataManager {

    private final RunwiseService mRunwiseService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(RunwiseService runwiseService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mRunwiseService = runwiseService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Ribot> syncRibots() {
        return mRunwiseService.getRibots()
                .concatMap(new Func1<List<Ribot>, Observable<Ribot>>() {
                    @Override
                    public Observable<Ribot> call(List<Ribot> ribots) {
                        return mDatabaseHelper.setRibots(ribots);
                    }
                });
    }

    public Observable<List<Ribot>> getRibots() {
        return mDatabaseHelper.getRibots().distinct();
    }

    public Observable<List<ProductListResponse.Product>> getProducts() {
        return mDatabaseHelper.getProducts().distinct();
    }

    public Observable<LoginResponse> login(LoginRequest loginRequest) {
        return mRunwiseService.login(loginRequest);
    }

    public void saveUser(UserInfoResponse userInfoResponse) {
        mPreferencesHelper.setUserInfo(userInfoResponse);
    }

    public UserInfoResponse getUserInfo() {
        return mPreferencesHelper.getUserInfo();
    }

    public Observable<ProductListResponse> syncProducts() {
        return mRunwiseService.getProducts(new EmptyRequest())
                .concatMap(new Func1<ProductListResponse, Observable<ProductListResponse>>() {
                    @Override
                    public Observable<ProductListResponse> call(ProductListResponse productListResponse) {
                        return mDatabaseHelper.setProducts(productListResponse);
                    }
                }).onErrorReturn(new Func1<Throwable, ProductListResponse>() {
                    @Override
                    public ProductListResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<OrderListResponse> syncOrders() {
        return mRunwiseService.getOrders(new EmptyRequest())
               /* .concatMap(new Func1<OrderListResponse, Observable<OrderListResponse>>() {
                    @Override
                    public Observable<OrderListResponse> call(OrderListResponse orderListResponse) {
                        return mDatabaseHelper.setOrders(orderListResponse);
                    }
                })*/.onErrorReturn(new Func1<Throwable, OrderListResponse>() {
                    @Override
                    public OrderListResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<ReturnOrderListResponse> syncReturnOrders() {
        return mRunwiseService.getReturnOrders(new EmptyRequest())
               /* .concatMap(new Func1<OrderListResponse, Observable<OrderListResponse>>() {
                    @Override
                    public Observable<OrderListResponse> call(OrderListResponse orderListResponse) {
                        return mDatabaseHelper.setOrders(orderListResponse);
                    }
                })*/.onErrorReturn(new Func1<Throwable, ReturnOrderListResponse>() {
                    @Override
                    public ReturnOrderListResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<HomePageBannerResponse> getHomePageBanner(String tag) {
        HomePageBannerRequest homePageBannerRequest = new HomePageBannerRequest();
        homePageBannerRequest.setTag(tag);
        return mRunwiseService.getHomePageBanner(homePageBannerRequest)
                .onErrorReturn(new Func1<Throwable, HomePageBannerResponse>() {
                    @Override
                    public HomePageBannerResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<DashBoardResponse> getDashboard() {
        return mRunwiseService.getDashboard(new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, DashBoardResponse>() {
                    @Override
                    public DashBoardResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }
    public Observable<EmptyResponse> logout() {
        return mRunwiseService.logout(new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, EmptyResponse>() {
                    @Override
                    public EmptyResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<LastBuyResponse> getLastOrderAmount() {
        return mRunwiseService.getLastOrderAmount(new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, LastBuyResponse>() {
                    @Override
                    public LastBuyResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }


}
