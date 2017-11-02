package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.HomePageMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/11/1.
 */

public class HomePagePresenter extends BasePresenter<HomePageMvpView>{
    private final DataManager mDataManager;
    private Subscription mOrderSubscription;
    private Subscription mReturnOrderSubscription;

    @Inject
    public HomePagePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(HomePageMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void syncOrders() {
        checkViewAttached();
        RxUtil.unsubscribe(mOrderSubscription);
        mOrderSubscription = mDataManager.syncOrders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<OrderListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showOrdersError();
                    }

                    @Override
                    public void onNext(OrderListResponse orderListResponse) {
                        if (orderListResponse.getList().isEmpty()) {
                            getMvpView().showOrdersEmpty();
                        } else {
                            getMvpView().showOrders(orderListResponse.getList());
                        }
                    }
                });
    }

    public void syncReturnOrders() {
        checkViewAttached();
        RxUtil.unsubscribe(mReturnOrderSubscription);
        mReturnOrderSubscription = mDataManager.syncReturnOrders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReturnOrderListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showReturnOrdersError();
                    }

                    @Override
                    public void onNext(ReturnOrderListResponse returnOrderListResponse) {
                        if (returnOrderListResponse.getList().isEmpty()) {
                            getMvpView().showReturnOrdersEmpty();
                        } else {
                            getMvpView().showReturnOrders(returnOrderListResponse.getList());
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mOrderSubscription != null) mOrderSubscription.unsubscribe();
    }
}
