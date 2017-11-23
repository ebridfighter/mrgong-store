package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderListFragmentMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

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

    public boolean canSeePrice(){
        UserInfoResponse userInfoResponse = mDataManager.loadUser();
        if (userInfoResponse != null){
            return userInfoResponse.isCanSeePrice();
        }
        return false;
    }

    public UserInfoResponse loadUser(){
        return mDataManager.loadUser();
    }


    public void getOrders(int page,int pageNum,String startTime,String endTime){
        checkViewAttached();
        RxUtil.unsubscribe(mGetOrdersSubscription);
        mGetOrdersSubscription = mDataManager.getOrderList(page,pageNum,startTime,endTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<OrderResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showOrdersError();
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
