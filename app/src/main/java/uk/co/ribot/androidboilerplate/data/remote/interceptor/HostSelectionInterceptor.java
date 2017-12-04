package uk.co.ribot.androidboilerplate.data.remote.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;

import static uk.co.ribot.androidboilerplate.data.remote.RunwiseService.HOST_URL;

/**
 * Created by mike on 2017/11/30.
 * 动态改变host
 */

public class HostSelectionInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().uri().getPath();
        String host;
        if (path.contains(HOST_URL)) {
            host = PreferencesHelper.DEFAULT_HOST;
        } else {
            host = PreferencesHelper.getHost();
        }
        if (host != null) {
            HttpUrl newUrl = HttpUrl.parse(host).newBuilder().encodedPath(path).build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }
}
