package uk.co.ribot.androidboilerplate.ui.presenter;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscriber;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderDetailResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderDetailResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderDetailMvpView;
import uk.co.ribot.androidboilerplate.util.ObjectTransformUtil;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/11/9.
 */

public class OrderDetailActivityPresenter extends BasePresenter<OrderDetailMvpView> {

    private DataManager mDataManager;
    private Subscription mCategorySubscription;
    private Subscription mOrderDetailSubscription;
    private Subscription mLoadProductSubscription;
    private Subscription mReturnOrderDetailSubscription;

    @Inject
    public OrderDetailActivityPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(OrderDetailMvpView mvpView) {
        super.attachView(mvpView);
    }


    public boolean isCanSeePrice() {
        return mDataManager.canSeePrice();
    }

    public void getCategorys() {
        checkViewAttached();
        mDataManager.getCategorys()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<CategoryResponse>() {
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
            public void onNext(CategoryResponse categoryResponse) {
                getMvpView().showCategorys(categoryResponse);
            }
        });
    }

    public void getOrderDetail(int orderId) {
        checkViewAttached();
        mDataManager.getOrderDetail(orderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<OrderDetailResponse>() {
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
            public void onNext(OrderDetailResponse orderDetailResponse) {
                getMvpView().showOrder(orderDetailResponse);
            }
        });
    }

    public void loadProduct(int productId) {
        checkViewAttached();
        RxUtil.unsubscribe(mCategorySubscription);
        mCategorySubscription = mDataManager.loadProduct(productId).subscribe(new Subscriber<ProductListResponse.Product>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ProductListResponse.Product product) {

            }
        });
    }

    public void getReturnOrder(int returnOrderId) {
        checkViewAttached();
        mDataManager.getReturnOrderDetail(returnOrderId).subscribe(new Observer<ReturnOrderDetailResponse>() {

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
            public void onNext(ReturnOrderDetailResponse returnOrderDetailResponse) {
                getMvpView().showReturnOrder(returnOrderDetailResponse);
            }
        });
    }

    public void getCategoryAndOrderDetail(int orderId) {
        Observable.merge(mDataManager.getCategorys(), mDataManager.getOrderDetail(orderId)).subscribe(new Observer<Object>() {
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
            public void onNext(Object o) {
                Log.i("", ObjectTransformUtil.toString(o));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mCategorySubscription != null) mCategorySubscription.unsubscribe();
        if (mOrderDetailSubscription != null) mOrderDetailSubscription.unsubscribe();
        if (mLoadProductSubscription != null) mLoadProductSubscription.unsubscribe();
        if (mReturnOrderDetailSubscription != null) mReturnOrderDetailSubscription.unsubscribe();
    }
}
