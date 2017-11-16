package uk.co.ribot.androidboilerplate.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.request.LoginRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.data.remote.subscriber.BaseSubscriber;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.LoginMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/9/28.
 */

public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    public boolean isLogin(){
        return mDataManager.isLogin();
    }

    public void login(String account, String password) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(account);
        loginRequest.setPassword(password);
        loginRequest.setRegistrationID("test");
        getMvpView().showProgressDialog();
        mSubscription = mDataManager.login(loginRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<LoginResponse>((Context) getMvpView()) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Timber.e(e, "网络错误");
                        getMvpView().showError();
                        getMvpView().hideProgressDialog();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        super.onNext(loginResponse);
                        String success = loginResponse.getIsSuccess();
                        if (TextUtils.isEmpty(success)||"false".equals(success)) {
                            getMvpView().showError();
                        } else {
                            mDataManager.saveUser(loginResponse.getUser());
                            getMvpView().onSuccess();
                        }
                        getMvpView().hideProgressDialog();
                    }
                });
    }

}
