package uk.co.ribot.androidboilerplate.util;

import io.reactivex.disposables.Disposable;
import rx.Subscription;

public class RxUtil {
    //rxjava1取消订阅
    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    //rxjava2取消订阅
    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
