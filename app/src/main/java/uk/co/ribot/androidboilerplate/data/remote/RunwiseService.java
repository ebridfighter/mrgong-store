package uk.co.ribot.androidboilerplate.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.database.Ribot;
import uk.co.ribot.androidboilerplate.data.model.net.request.CancelMakeInventoryRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.CategoryRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.ChangeOrderStateRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.EmptyRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.GetCategoryRequest;
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
import uk.co.ribot.androidboilerplate.data.remote.gsonconverterfactory.CustomGsonConverterFactory;
import uk.co.ribot.androidboilerplate.data.remote.gsonconverterfactory.StringConverter;
import uk.co.ribot.androidboilerplate.data.remote.interceptor.AddHeaderInterceptor;
import uk.co.ribot.androidboilerplate.data.remote.interceptor.GetCookiesInterceptor;
import uk.co.ribot.androidboilerplate.data.remote.interceptor.HostSelectionInterceptor;
import uk.co.ribot.androidboilerplate.data.remote.interceptor.HttpLoggingInterceptor;
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;

public interface RunwiseService {

    boolean test = true;
    String ENDPOINT = test ? PreferencesHelper.DEFAULT_HOST : "https://api.ribot.io";

    String HOST_URL = "/api/get/host";

    String HEAD_KEY_COOKIE = "Cookie";
    String HEAD_KEY_DATABASE = "X-Odoo-Db";
    String HEAD_KEY_TIMESTAMP = "timeStamp";
    int CONNECT_TIMEOUT = 60;


    class Creator {
        /******** Helper class that sets up a new services *******/
        public static RunwiseService newRunwiseService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
//                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                    .registerTypeAdapter(String.class, new StringConverter())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HostSelectionInterceptor())
                    .addInterceptor(new AddHeaderInterceptor())
                    .addInterceptor(new GetCookiesInterceptor())
                    .addInterceptor(new HttpLoggingInterceptor())
                    .retryOnConnectionFailure(true)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(RunwiseService.ENDPOINT)
                    .addConverterFactory(CustomGsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RunwiseService.class);
        }

    }

    @GET("/ribots")
    Observable<List<Ribot>> getRibots();

    @POST("/gongfu/v2/authenticate")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/gongfu/v2/product/list")
    Observable<ProductListResponse> getProducts(@Body EmptyRequest emptyRequest);

    @POST("/api/v2/stock/list")
    Observable<StockListResponse> getStockList(@Body StockListRequest request);

    @POST("/gongfu/v2/order/undone_orders")
    Observable<OrderListResponse> getOrders(@Body EmptyRequest emptyRequest);

    @POST("/gongfu/v2/return_order/undone")
    Observable<ReturnOrderListResponse> getReturnOrders(@Body EmptyRequest emptyRequest);

    @POST("/gongfu/blog/post/list/login")
    Observable<HomePageBannerResponse> getHomePageBanner(@Body HomePageBannerRequest homePageBannerRequest);

    @POST("/gongfu/v2/shop/stock/dashboard")
    Observable<DashBoardResponse> getDashboard(@Body EmptyRequest emptyRequest);

    @POST("/gongfu/logout")
    Observable<EmptyResponse> logout(@Body EmptyRequest emptyRequest);

    @POST("/gongfu/message/list")
    Observable<MessageResponse> getMessage(@Body EmptyRequest emptyRequest);

    @POST("/gongfu/v2/order/last_order_amout")
    Observable<LastBuyResponse> getLastOrderAmount(@Body EmptyRequest emptyRequest);

    @POST("/api/product/category")
    Observable<CategoryResponse> getCategory(@Body GetCategoryRequest request);

    @POST("/gongfu/order/{orderId}/state")
    Observable<EmptyResponse> changeOrderState(@Path("orderId") int orderId, @Body ChangeOrderStateRequest changeOrderStateRequest);

    @POST("/gongfu/v2/return_order/{returnOrderId}/done")
    Observable<FinishReturnResponse> finishReturnOrder(@Path("returnOrderId") int returnOrderId, @Body EmptyRequest emptyRequest);

    @POST("/gongfu/v2/order/{orderId}")
    Observable<OrderDetailResponse> getOrderDetail(@Path("orderId") int orderId, @Body EmptyRequest emptyRequest);

    @POST("/gongfu/v2/return_order/{returnOrderId}")
    Observable<ReturnOrderDetailResponse> getReturnOrderDetail(@Path("returnOrderId") int returnOrderId, @Body EmptyRequest emptyRequest);

    @POST("/gongfu/v2/user/information")
    Observable<UserInfoResponse> getUserInfo(@Body EmptyRequest emptyRequest);

    @POST("/api/product/category")
    Observable<CategoryResponse> getCategorys(@Body CategoryRequest categoryRequest);

    @POST("/gongfu/v2/shop/preset/product/list")
    Observable<IntelligentProductDataResponse> getIntelligentProducts(@Body GetIntelligentProductsRequest getIntelligentProductsRequest);

    @POST("/gongfu/v2/order/create")
    Observable<OrderCommitResponse> commitOrder(@Body CommitOrderRequest commitOrderRequest);

    @POST("/api/sale/shop/info")
    Observable<ShopInfoResponse> getShopInfo(@Body EmptyRequest emptyRequest);

    @POST("/gongfu/order/list")
    Observable<OrderResponse> getOrderList(@Body OrderListRequest orderListRequest);

    @POST("/API/v2/return_order/list")
    Observable<ReturnDataResponse> getReturnOrderList(@Body EmptyRequest emptyRequest);

    @POST("/gongfu/v2/account_invoice/list")
    Observable<StatementAccountListResponse> getStatementAccountList(@Body EmptyRequest emptyRequest);

    @POST("/api/inventory/list")
    Observable<InventoryResponse> getInventoryList(@Body GetInventoryListRequest getInventoryListRequest);

    @POST("/api/inventory/state")
    Observable<EmptyResponse> cancleMakeInventory(@Body CancelMakeInventoryRequest cancelMakeInventoryRequest);

    @POST("/api/get/host")
    Observable<HostResponse> getHost(@Body GetHostRequest getHostRequest);

    @POST("/gongfu/shop/zicai/list")
    Observable<ProcurementResponse> getZiCaiList(@Body ProcurementRequest procurementRequest);

}
