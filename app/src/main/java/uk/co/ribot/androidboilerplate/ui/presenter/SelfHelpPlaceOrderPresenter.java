package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.SelfHelpPlaceOrderMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

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
        RxUtil.unsubscribe(mGetUserInfoSubscription);
        mGetUserInfoSubscription = mDataManager.getUserInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<UserInfoResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserInfoResponse userInfoResponse) {
                        getMvpView().getUserInfoSuccess(userInfoResponse);
                    }
                });
    }

    public void commitOrder(String estimated_time, String order_type_id, List<CommitOrderRequest.ProductsBean> products) {
        checkViewAttached();
        RxUtil.unsubscribe(mCommitOrderSubscription);
        mCommitOrderSubscription = mDataManager.commitOrder(estimated_time, order_type_id, products)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<OrderCommitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().commitOrderError();
                    }

                    @Override
                    public void onNext(OrderCommitResponse orderCommitResponse) {
                        getMvpView().commitOrderSuccess(orderCommitResponse);
                    }
                });
    }

    public void loadProduct(int productId){
        checkViewAttached();
        mDataManager.loadProduct(productId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<ProductListResponse.Product>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ProductListResponse.Product product) {
                getMvpView().showProductSuccess(product);
            }
        });
    }
}
