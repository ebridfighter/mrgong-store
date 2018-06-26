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
    private Subscription mSubscription;
    private Subscription mGetShopInfoSubscription;
    private Subscription mGetUserSubscription;
    private Subscription mGetProcumentPermissionSubscription;
    private Subscription mGetTransferPermissionSubscription;

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
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mGetShopInfoSubscription != null) mGetShopInfoSubscription.unsubscribe();
        if (mGetUserSubscription != null) mGetUserSubscription.unsubscribe();
        if (mGetProcumentPermissionSubscription != null)
            mGetProcumentPermissionSubscription.unsubscribe();
        if (mGetTransferPermissionSubscription != null)
            mGetTransferPermissionSubscription.unsubscribe();
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

            }

            @Override
            public void onNext(ShopInfoResponse shopInfoResponse) {
                getMvpView().showShopInfo(shopInfoResponse);
            }
        });
    }


    public void logoutLocal() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        getMvpView().logout();
    }
}
