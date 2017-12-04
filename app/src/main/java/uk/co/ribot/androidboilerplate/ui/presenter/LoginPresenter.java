package uk.co.ribot.androidboilerplate.ui.presenter;

import android.text.TextUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.HostResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.LoginMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/9/28.
 */

public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mGetHostSubscription;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mGetHostSubscription != null) mGetHostSubscription.unsubscribe();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public boolean isLogin() {
        return mDataManager.isLogin();
    }

    public void saveHost(String host) {
        mDataManager.saveHost(host);
    }

    public void saveDataBase(String databaseName) {
        mDataManager.saveDataBase(databaseName);
    }

    public void login(String account, String password) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.login(account,password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "网络错误");
                        getMvpView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        String success = loginResponse.getIsSuccess();
                        if (TextUtils.isEmpty(success) || "false".equals(success)) {
                            getMvpView().loginConflict();
                        } else {
                            mDataManager.saveUser(loginResponse.getUser());
                            getMvpView().onSuccess();
                        }
                    }
                });
    }

    public void getHost(String companyName) {
        checkViewAttached();
        RxUtil.unsubscribe(mGetHostSubscription);
        mGetHostSubscription = mDataManager.getHost(companyName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<HostResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().getHostError(e.toString());
                    }

                    @Override
                    public void onNext(HostResponse hostResponse) {
                        if(hostResponse != null){
                            getMvpView().getHostSuccess(hostResponse);
                        }else{
                            getMvpView().getHostError("");
                        }
                    }
                });
    }
}
