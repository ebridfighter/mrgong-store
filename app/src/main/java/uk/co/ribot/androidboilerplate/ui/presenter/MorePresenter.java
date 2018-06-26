package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.ShopInfoResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MoreMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/11/3.
 */

public class MorePresenter extends BasePresenter<MoreMvpView> {
    private final DataManager mDataManager;
    private Disposable mDisposable;
    private Disposable mGetShopInfoDisposable;
    private Disposable mGetUserDisposable;
    private Disposable mGetProcumentPermissionDisposable;
    private Disposable mGetTransferPermissionDisposable;

    @Inject
    public MorePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MoreMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
        if (mGetShopInfoDisposable != null) mGetShopInfoDisposable.dispose();
        if (mGetUserDisposable != null) mGetUserDisposable.dispose();
        if (mGetProcumentPermissionDisposable != null)
            mGetProcumentPermissionDisposable.dispose();
        if (mGetTransferPermissionDisposable != null)
            mGetTransferPermissionDisposable.dispose();
    }

    public boolean isLogin() {
        return mDataManager.isLogin();
    }

    public UserInfoResponse loadUser() {
        return mDataManager.loadUser();
    }

    public boolean canSeePrice() {
        return mDataManager.canSeePrice();
    }

    public void getUser() {
        checkViewAttached();
        mDataManager.getUserInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<UserInfoResponse>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mGetUserDisposable = d;
            }

            @Override
            public void onNext(UserInfoResponse userInfoResponse) {
                getMvpView().showUserInfo(userInfoResponse);
            }
        });
    }

    public void getProcumentPermission() {
        checkViewAttached();
        mDataManager.getUserInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<UserInfoResponse>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mGetProcumentPermissionDisposable = d;
            }

            @Override
            public void onNext(UserInfoResponse userInfoResponse) {
                getMvpView().showProcumentPermission(userInfoResponse);
            }
        });
    }

    public void getTransferPermission() {
        checkViewAttached();
        mDataManager.getUserInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<UserInfoResponse>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mGetTransferPermissionDisposable = d;
            }

            @Override
            public void onNext(UserInfoResponse userInfoResponse) {
                getMvpView().showTransferPermission(userInfoResponse);
            }
        });
    }


    public void getShopInfo() {
        checkViewAttached();
        mDataManager.getShopInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<ShopInfoResponse>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mGetShopInfoDisposable = d;
            }

            @Override
            public void onNext(ShopInfoResponse shopInfoResponse) {
                getMvpView().showShopInfo(shopInfoResponse);
            }
        });
    }


    public void logoutLocal() {
        checkViewAttached();
        if (mDisposable != null){
            mDisposable.dispose();
        }
        getMvpView().logout();
    }
}
