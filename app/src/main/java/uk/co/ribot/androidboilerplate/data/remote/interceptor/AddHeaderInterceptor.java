package uk.co.ribot.androidboilerplate.data.remote.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;

import static uk.co.ribot.androidboilerplate.data.remote.RunwiseService.HEAD_KEY_COOKIE;
import static uk.co.ribot.androidboilerplate.data.remote.RunwiseService.HEAD_KEY_DATABASE;
import static uk.co.ribot.androidboilerplate.data.remote.RunwiseService.HEAD_KEY_TIMESTAMP;
import static uk.co.ribot.androidboilerplate.data.remote.RunwiseService.HOST_URL;

/**
 * Created by mike on 2017/9/29.
 */

public class AddHeaderInterceptor implements Interceptor {


    public AddHeaderInterceptor() {
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //添加请求头
        Request request = chain.request();
        String path = request.url().uri().getPath();
        String dataBaseName;
        if (path.contains(HOST_URL)) {
            dataBaseName = PreferencesHelper.DEFAULT_DATABASE_NAME;
        }else{
            dataBaseName = PreferencesHelper.getCurrentDataBaseName();
        }
        Request.Builder builder1 = request.newBuilder();
        Request build = builder1.addHeader(HEAD_KEY_COOKIE, PreferencesHelper.getCookie())
                .addHeader(HEAD_KEY_DATABASE, dataBaseName)
                .addHeader(HEAD_KEY_TIMESTAMP, String.valueOf(System.currentTimeMillis()))
                .build();
        return chain.proceed(build);
    }
}
