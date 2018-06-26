package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderListFragmentMvpView;

/**
 * Created by mike on 2017/11/22.
 */
public class OrderListFragmentPresenter extends BasePresenter<OrderListFragmentMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mGetOrdersSubscription;


    @Inject
    public OrderListFragmentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(OrderListFragmentMvpView mvpView) {
        super.attachView(mvpView);
    }

    public boolean canSeePrice() {
        UserInfoResponse userInfoResponse = mDataManager.loadUser();
        if (userInfoResponse != null) {
            return userInfoResponse.isCanSeePrice();
        }
        return false;
    }

    public UserInfoResponse loadUser() {
        return mDataManager.loadUser();
    }


    public void getOrders(int page, int pageNum, String startTime, String endTime) {
        checkViewAttached();
        mDataManager.getOrderList(page, pageNum, startTime, endTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<OrderResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showOrdersError();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OrderResponse orderResponse) {
                        getMvpView().showOrders(orderResponse);
                    }
                });
    }


    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mGetOrdersSubscription != null) mGetOrdersSubscription.unsubscribe();
    }
}
