package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.SelfHelpPlaceOrderMvpView;

/**
 * Created by mike on 2017/11/16.
 */

public class SelfHelpPlaceOrderPresenter extends BasePresenter<SelfHelpPlaceOrderMvpView> {
    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mGetUserInfoSubscription;
    private Subscription mCommitOrderSubscription;
    private Subscription mLoadProductSubscription;

    @Inject
    public SelfHelpPlaceOrderPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SelfHelpPlaceOrderMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mGetUserInfoSubscription != null) mGetUserInfoSubscription.unsubscribe();
        if (mCommitOrderSubscription != null) mCommitOrderSubscription.unsubscribe();
    }

    public UserInfoResponse loadUser() {
        return mDataManager.loadUser();
    }

    public void getUserInfo() {
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
                getMvpView().getUserInfoSuccess(userInfoResponse);
            }
        });
    }

    public void commitOrder(String estimated_time, String order_type_id, List<CommitOrderRequest.ProductsBean> products) {
        checkViewAttached();
       mDataManager.commitOrder(estimated_time, order_type_id, products)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<OrderCommitResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().commitOrderError();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OrderCommitResponse orderCommitResponse) {
                        getMvpView().commitOrderSuccess(orderCommitResponse);
                    }
                });
    }

    public void loadProduct(int productId) {
        checkViewAttached();
        mDataManager.loadProduct(productId) .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new MaybeObserver<ProductBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ProductBean product) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
