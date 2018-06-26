package uk.co.ribot.androidboilerplate.ui.presenter;

import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.database.UserBean;
import uk.co.ribot.androidboilerplate.data.model.net.response.HostResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.LoginResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.LoginMvpView;

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

    public void loadUserList(){
        checkViewAttached();
        mDataManager.loadUserList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<List<UserBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<UserBean> userBeanList) {
                getMvpView().loadUserListSuccess(userBeanList);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "加载账号列表,数据库操作出错");
            }
        });
    }

    public void login(String companyName, String account, String password) {
        checkViewAttached();
        mDataManager.getHost(companyName)
                .flatMap(new Function<HostResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(HostResponse hostResponse) throws Exception {
                        if (hostResponse == null) {
                            getMvpView().getHostError("");
                            return null;
                        }
                        if (TextUtils.isEmpty(hostResponse.getPort())) {
                            mDataManager.saveHost(hostResponse.getHost());
                        } else {
                            mDataManager.saveHost(hostResponse.getHost() + ":" + hostResponse.getPort());
                        }
                        mDataManager.saveDataBase(hostResponse.getDbName());
                        return mDataManager.login(account, password);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<LoginResponse>() {
                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "网络错误");
                            getMvpView().showError(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            String success = loginResponse.getIsSuccess();
                            mDataManager.saveUserToDB(companyName,account, password);
                            if (TextUtils.isEmpty(success) || "false".equals(success)) {
                                getMvpView().loginConflict();
                            } else {
                                mDataManager.saveUserToPre(loginResponse.getUser());
                                getMvpView().onSuccess();
                            }
                        }
        });

    }

    public void deleteUser(UserBean userBean){
        mDataManager.deleteUser(userBean);
    }
}
