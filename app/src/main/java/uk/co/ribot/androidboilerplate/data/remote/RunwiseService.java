package uk.co.ribot.androidboilerplate.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.data.model.net.request.EmptyRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.HomePageBannerRequest;
import uk.co.ribot.androidboilerplate.data.model.net.request.LoginRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.DashBoardResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.EmptyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.HomePageBannerResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.data.remote.gsonconverterfactory.CustomGsonConverterFactory;
import uk.co.ribot.androidboilerplate.data.remote.interceptor.AddHeaderInterceptor;
import uk.co.ribot.androidboilerplate.data.remote.interceptor.GetCookiesInterceptor;
import uk.co.ribot.androidboilerplate.data.remote.interceptor.HttpLoggingInterceptor;
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;

public interface RunwiseService {

    boolean test = true;
    String ENDPOINT = test ? "http://erp2.runwise.cn" : "https://api.ribot.io";

    String HEAD_KEY_COOKIE = "Cookie";
    String HEAD_KEY_DATABASE = "X-Odoo-Db";


    class Creator {
        /******** Helper class that sets up a new services *******/
        public static RunwiseService newRibotsService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AddHeaderInterceptor())
                    .addInterceptor(new GetCookiesInterceptor())
                    .addInterceptor(new HttpLoggingInterceptor())
                    .retryOnConnectionFailure(true)
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

}
