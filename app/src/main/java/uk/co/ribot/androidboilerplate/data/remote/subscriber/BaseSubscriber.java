package uk.co.ribot.androidboilerplate.data.remote.subscriber;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import rx.Subscriber;
import uk.co.ribot.androidboilerplate.data.model.net.exception.ApiException;

/**
 * Created by mike on 2017/10/11.
 */

public class BaseSubscriber<T> extends Subscriber<T> {
    protected Context mContext;

    public BaseSubscriber(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(final Throwable e) {
        Log.w("Subscriber onError", e);
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            if (exception.isSessionExpried()) {
                //处理Session失效对应的逻辑

            } else {
                if (!TextUtils.isEmpty(e.getMessage())){
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onNext(T t) {

    }
}
