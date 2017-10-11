package uk.co.ribot.androidboilerplate.data.remote.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;

/**
 * Created by mike on 2017/9/29.
 */

public class GetCookiesInterceptor implements Interceptor {
    public static final String KEY_COOKIE = "Set-Cookie";

    public GetCookiesInterceptor() {
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers(KEY_COOKIE).isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Observable.from(originalResponse.headers(KEY_COOKIE))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            cookieBuffer.append(cookie);
                        }
                    });
            if (cookieBuffer.toString().contains("session_id")){
                PreferencesHelper.setCookie(cookieBuffer.toString());
                Log.i("session_id",cookieBuffer.toString());
            }
        }
        return originalResponse;
    }
}
