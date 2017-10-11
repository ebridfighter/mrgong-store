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
import uk.co.ribot.androidboilerplate.data.model.net.request.LoginRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.remote.RibotsService;

@Singleton
public class DataManager {

    private final RibotsService mRibotsService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Ribot> syncRibots() {
        return mRibotsService.getRibots()
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
        return mRibotsService.login(loginRequest);
    }

    public Observable<ProductListResponse> syncProducts() {
        return mRibotsService.getProducts(new EmptyRequest())
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


}
