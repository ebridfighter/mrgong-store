package uk.co.ribot.androidboilerplate.ui.presenter;

import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.LauncherMvpView;
import uk.co.ribot.androidboilerplate.util.RxCountDownUtil;

/**
 * Created by mike on 2017/11/29.
 */
public class LauncherPresenter extends BasePresenter<LauncherMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;


    @Inject
    public LauncherPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LauncherMvpView mvpView) {
        super.attachView(mvpView);
    }

    public boolean isLogin() {
        return mDataManager.isLogin();
    }

    public void timingBegins(int seconds) {
        RxCountDownUtil.countDown(seconds).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                getMvpView().countDownFinish();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                //显示倒计时剩有秒数
            }
        });
    }

    public void getServerDate() {
        checkViewAttached();
        new Thread(() -> {
            URL url = null;//取得资源对象
            try {
                url = new URL("http://www.baidu.com");
                URLConnection uc = url.openConnection();//生成连接对象
                uc.connect(); //发出连接
                getMvpView().onReceiveServerTime(uc.getDate()); //取得网站日期时间
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
