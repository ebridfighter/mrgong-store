package uk.co.ribot.androidboilerplate.data;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.database.Ribot;
import uk.co.ribot.androidboilerplate.data.model.net.request.CategoryRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.ChangeOrderStateRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.EmptyRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.GetIntelligentProductsRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.HomePageBannerRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.LoginRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.StockListRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.DashBoardResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.EmptyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.FinishReturnResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.HomePageBannerResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.IntelligentProductDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LastBuyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderDetailResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderDetailResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ShopInfoResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.StockListResponse;
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

    public Observable<List<ProductListResponse.Product>> loadProducts() {
        return mDatabaseHelper.getProducts().distinct();
    }

    public Observable<LoginResponse> login(LoginRequest loginRequest) {
        return mRunwiseService.login(loginRequest);
    }

    public void saveUser(UserInfoResponse userInfoResponse) {
        mPreferencesHelper.setUserInfo(userInfoResponse);
    }

    public UserInfoResponse loadUser() {
        return mPreferencesHelper.getUserInfo();
    }

    public boolean isLogin() {
        return mPreferencesHelper.isLogin();
    }

    public boolean canSeePrice() {
        UserInfoResponse userInfoResponse = loadUser();
        if (userInfoResponse != null) {
            return userInfoResponse.isCanSeePrice();
        }
        return false;
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

    /**
     * 获取库存信息
     *
     * @param pageIndex     分页页号
     * @param pageLimit     每页多少条
     * @param stockType     类型category
     * @param searchKeyword 搜索的关键字
     */
    public Observable<StockListResponse> getStockList(int pageIndex, int pageLimit, String stockType, String searchKeyword) {
        return mRunwiseService.getStockList(new StockListRequest(pageLimit, pageIndex, searchKeyword, stockType));
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

    public Observable<MessageResponse> getMessages() {
        return mRunwiseService.getMessage(new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, MessageResponse>() {
                    @Override
                    public MessageResponse call(Throwable throwable) {
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

    public Observable<EmptyResponse> changeOrderState(int orderId, String state) {
        ChangeOrderStateRequest changeOrderStateRequest = new ChangeOrderStateRequest();
        changeOrderStateRequest.setState(state);
        return mRunwiseService.changeOrderState(orderId, changeOrderStateRequest)
                .onErrorReturn(new Func1<Throwable, EmptyResponse>() {
                    @Override
                    public EmptyResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<FinishReturnResponse> finishReturnOrder(int returnOrderId) {
        return mRunwiseService.finishReturnOrder(returnOrderId, new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, FinishReturnResponse>() {
                    @Override
                    public FinishReturnResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<OrderDetailResponse> getOrderDetail(int orderId) {
        return mRunwiseService.getOrderDetail(orderId, new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, OrderDetailResponse>() {
                    @Override
                    public OrderDetailResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<ReturnOrderDetailResponse> getReturnOrderDetail(int returnOrderId) {
        return mRunwiseService.getReturnOrderDetail(returnOrderId, new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, ReturnOrderDetailResponse>() {
                    @Override
                    public ReturnOrderDetailResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<CategoryResponse> getCategorys() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setUser_id(Integer.parseInt(loadUser().getUid()));
        return mRunwiseService.getCategorys(categoryRequest)
                .onErrorReturn(new Func1<Throwable, CategoryResponse>() {
                    @Override
                    public CategoryResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<UserInfoResponse> getUserInfo() {
        return mRunwiseService.getUserInfo(new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, UserInfoResponse>() {
                    @Override
                    public UserInfoResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                }).doOnNext(userInfoResponse -> {
                    saveUser(userInfoResponse);
                });
    }

    public Observable<ProductListResponse.Product> loadProduct(int productId) {
        return mDatabaseHelper.getProduct(productId);
    }

    public Observable<IntelligentProductDataResponse> getIntelligentProducts(double estimatedTurnover, double safetyFactor) {
        GetIntelligentProductsRequest getIntelligentProductsRequest = new GetIntelligentProductsRequest();
        getIntelligentProductsRequest.setPredict_sale_amount(estimatedTurnover);
        getIntelligentProductsRequest.setYongliang_factor(safetyFactor);

        return mRunwiseService.getIntelligentProducts(getIntelligentProductsRequest)
                .onErrorReturn(new Func1<Throwable, IntelligentProductDataResponse>() {
                    @Override
                    public IntelligentProductDataResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<OrderCommitResponse> commitOrder(String estimated_time, String order_type_id, List<CommitOrderRequest.ProductsBean> products) {
        CommitOrderRequest commitOrderRequest = new CommitOrderRequest();
        commitOrderRequest.setEstimated_time(estimated_time);
        commitOrderRequest.setOrder_type_id(order_type_id);
        commitOrderRequest.setProducts(products);

        return mRunwiseService.commitOrder(commitOrderRequest)
                .onErrorReturn(new Func1<Throwable, OrderCommitResponse>() {
                    @Override
                    public OrderCommitResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

    public Observable<ShopInfoResponse> getShopInfo() {
        return mRunwiseService.getShopInfo(new EmptyRequest())
                .onErrorReturn(new Func1<Throwable, ShopInfoResponse>() {
                    @Override
                    public ShopInfoResponse call(Throwable throwable) {
                        Log.i("onErrorReturn", throwable.toString());
                        return null;
                    }
                });
    }

}
