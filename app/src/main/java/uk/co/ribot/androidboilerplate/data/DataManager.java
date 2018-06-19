package uk.co.ribot.androidboilerplate.data;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.database.Ribot;
import uk.co.ribot.androidboilerplate.data.model.net.request.CancelMakeInventoryRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.CategoryRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.ChangeOrderStateRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.EmptyRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.GetHostRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.GetIntelligentProductsRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.GetInventoryListRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.HomePageBannerRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.LoginRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.OrderListRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.ProcurementRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.StockListRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.DashBoardResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.EmptyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.FinishReturnResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.HomePageBannerResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.HostResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.IntelligentProductDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LastBuyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderDetailResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProcurementResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderDetailResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ShopInfoResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.StatementAccountListResponse;
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
                .concatMap(ribots -> mDatabaseHelper.setRibots(ribots));
    }

    public void setCookies(String cookies){
        mPreferencesHelper.setCookie("");
    }

    public rx.Observable<List<Ribot>> getRibots() {
        return mDatabaseHelper.getRibots().distinct();
    }

    public rx.Observable<List<ProductListResponse.Product>> loadProducts() {
        return mDatabaseHelper.getProducts().distinct();
    }

    public Observable<LoginResponse> login(String account, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(account);
        loginRequest.setPassword(password);
        loginRequest.setRegistrationID("test");
        return mRunwiseService.login(loginRequest);
    }

    public void saveUser(UserInfoResponse userInfoResponse) {
        mPreferencesHelper.setUserInfo(userInfoResponse);
    }

    public void saveHost(String host) {
        mPreferencesHelper.setHost(host);
    }

    public void saveDataBase(String databaseName) {
        mPreferencesHelper.setCurrentDataBaseName(databaseName);
    }

    public UserInfoResponse loadUser() {
        return mPreferencesHelper.getUserInfo();
    }

    public boolean isLogin() {
        return mPreferencesHelper.isLogin() && loadUser() !=null;
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
                .concatMap(productListResponse -> mDatabaseHelper.setProducts(productListResponse)).onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
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
                })*/.onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<ReturnOrderListResponse> syncReturnOrders() {
        return mRunwiseService.getReturnOrders(new EmptyRequest())
               /* .concatMap(new Func1<OrderListResponse, Observable<OrderListResponse>>() {
                    @Override
                    public Observable<OrderListResponse> call(OrderListResponse orderListResponse) {
                        return mDatabaseHelper.setOrders(orderListResponse);
                    }
                })*/.onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<HomePageBannerResponse> getHomePageBanner(String tag) {
        HomePageBannerRequest homePageBannerRequest = new HomePageBannerRequest();
        homePageBannerRequest.setTag(tag);
        return mRunwiseService.getHomePageBanner(homePageBannerRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }


    public Observable<DashBoardResponse> getDashboard() {
        return mRunwiseService.getDashboard(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<EmptyResponse> logout() {
        return mRunwiseService.logout(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<MessageResponse> getMessages() {
        return mRunwiseService.getMessage(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }


    public Observable<LastBuyResponse> getLastOrderAmount() {
        return mRunwiseService.getLastOrderAmount(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<EmptyResponse> changeOrderState(int orderId, String state) {
        ChangeOrderStateRequest changeOrderStateRequest = new ChangeOrderStateRequest();
        changeOrderStateRequest.setState(state);
        return mRunwiseService.changeOrderState(orderId, changeOrderStateRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<FinishReturnResponse> finishReturnOrder(int returnOrderId) {
        return mRunwiseService.finishReturnOrder(returnOrderId, new EmptyRequest())
                .onErrorReturn((Throwable throwable) -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<OrderDetailResponse> getOrderDetail(int orderId) {
        return mRunwiseService.getOrderDetail(orderId, new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<ReturnOrderDetailResponse> getReturnOrderDetail(int returnOrderId) {
        return mRunwiseService.getReturnOrderDetail(returnOrderId, new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<CategoryResponse> getCategorys() {
        CategoryRequest categoryRequest = new CategoryRequest();
        UserInfoResponse userInfoResponse = loadUser();
        if (userInfoResponse == null){
            return null;
        }
        categoryRequest.setUser_id(Integer.parseInt(loadUser().getUid()));
        return mRunwiseService.getCategorys(categoryRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<UserInfoResponse> getUserInfo() {
        return mRunwiseService.getUserInfo(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                }).doOnNext(userInfoResponse -> {
                    saveUser(userInfoResponse);
                });
    }

    public rx.Observable<ProductListResponse.Product> loadProduct(int productId) {
        return mDatabaseHelper.getProduct(productId);
    }

    public Observable<IntelligentProductDataResponse> getIntelligentProducts(double estimatedTurnover, double safetyFactor) {
        GetIntelligentProductsRequest getIntelligentProductsRequest = new GetIntelligentProductsRequest();
        getIntelligentProductsRequest.setPredict_sale_amount(estimatedTurnover);
        getIntelligentProductsRequest.setYongliang_factor(safetyFactor);

        return mRunwiseService.getIntelligentProducts(getIntelligentProductsRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<OrderCommitResponse> commitOrder(String estimated_time, String order_type_id, List<CommitOrderRequest.ProductsBean> products) {
        CommitOrderRequest commitOrderRequest = new CommitOrderRequest();
        commitOrderRequest.setEstimated_time(estimated_time);
        commitOrderRequest.setOrder_type_id(order_type_id);
        commitOrderRequest.setProducts(products);

        return mRunwiseService.commitOrder(commitOrderRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<ShopInfoResponse> getShopInfo() {
        return mRunwiseService.getShopInfo(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<OrderResponse> getOrderList(int page, int pageNum, String startTime, String endTime) {
        OrderListRequest orderListRequest = new OrderListRequest();
        orderListRequest.setLimit(pageNum);
        orderListRequest.setPz(page);
        if (startTime != null && !startTime.trim().equals("")) {
            orderListRequest.setStart(startTime);
        }
        if (endTime != null && !endTime.trim().equals("")) {
            orderListRequest.setEnd(endTime);
        }
        return mRunwiseService.getOrderList(orderListRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<ReturnDataResponse> getReturnOrderList() {
        return mRunwiseService.getReturnOrderList(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<StatementAccountListResponse> getStatementAccountList() {
        return mRunwiseService.getStatementAccountList(new EmptyRequest())
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<InventoryResponse> getInventoryList(int page, int pageNum, int type) {
        GetInventoryListRequest getInventoryListRequest = new GetInventoryListRequest();
        getInventoryListRequest.setLimit(pageNum);
        getInventoryListRequest.setPz(page);
        getInventoryListRequest.setDate_type(type);

        return mRunwiseService.getInventoryList(getInventoryListRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<EmptyResponse> cancelMakeInventory(int id, String state) {
        CancelMakeInventoryRequest cancelMakeInventoryRequest = new CancelMakeInventoryRequest();
        cancelMakeInventoryRequest.setId(id);
        cancelMakeInventoryRequest.setState(state);

        return mRunwiseService.cancleMakeInventory(cancelMakeInventoryRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<HostResponse> getHost(String companyName) {
        GetHostRequest getHostRequest = new GetHostRequest();
        getHostRequest.setCompanyName(companyName);
        return mRunwiseService.getHost(getHostRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }

    public Observable<ProcurementResponse> getZiCaiList(int type) {
        ProcurementRequest procurementRequest = new ProcurementRequest();
        procurementRequest.setType(type);
        return mRunwiseService.getZiCaiList(procurementRequest)
                .onErrorReturn(throwable -> {
                    Log.i("onErrorReturn", throwable.toString());
                    return null;
                });
    }


}
