package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.request.LoginRequest;
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

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void login(String account, String password) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(account);
        loginRequest.setPassword(password);
        loginRequest.setRegistrationID("test");
        mSubscription = mDataManager.login(loginRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "网络错误");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        if ("false".equals(loginResponse.getIsSuccess())) {
//                            getMvpView().showRibotsEmpty();
                        } else {
                            getMvpView().onSuccess();
                        }
                    }
                });
    }

}
