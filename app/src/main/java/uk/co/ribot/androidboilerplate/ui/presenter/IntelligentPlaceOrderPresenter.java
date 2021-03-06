package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.IntelligentProductDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.IntelligentPlaceOrderMvpView;

/**
 * Created by mike on 2017/11/14.
 */

public class IntelligentPlaceOrderPresenter extends BasePresenter<IntelligentPlaceOrderMvpView> {

    private final DataManager mDataManager;
    private Subscription mGetUserInfoSubscription;
    private Subscription mGetIntelligentProductsSubscription;
    private Subscription mCommitOrderSubscription;

    @Inject
    public IntelligentPlaceOrderPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(IntelligentPlaceOrderMvpView mvpView) {
        super.attachView(mvpView);
    }

    public boolean canSeePrice() {
        return mDataManager.canSeePrice();
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

    public void getIntelligentProducts(double estimatedTurnover, double safetyFactor) {
        checkViewAttached();
        mDataManager.getIntelligentProducts(estimatedTurnover, safetyFactor).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<IntelligentProductDataResponse>() {

            @Override
            public void onError(Throwable e) {
                getMvpView().showIntelligentProductsError();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(IntelligentProductDataResponse intelligentProductDataResponse) {
                getMvpView().showIntelligentProducts(intelligentProductDataResponse);
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

    @Override
    public void detachView() {
        super.detachView();
        if (mGetUserInfoSubscription != null) mGetUserInfoSubscription.unsubscribe();
        if (mGetIntelligentProductsSubscription != null)
            mGetIntelligentProductsSubscription.unsubscribe();
        if (mCommitOrderSubscription != null) mCommitOrderSubscription.unsubscribe();
    }
}
