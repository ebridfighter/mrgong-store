package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.LastBuyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.PlaceOrderMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/11/6.
 */

public class PlaceOrderPresenter extends BasePresenter<PlaceOrderMvpView> {
    private DataManager mDataManager;
    private Subscription mLastBuySubscription;

    @Inject
    public PlaceOrderPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(PlaceOrderMvpView mvpView) {
        super.attachView(mvpView);
    }

    public boolean canSeePrice(){
        UserInfoResponse userInfoResponse = mDataManager.loadUser();
        if (userInfoResponse != null){
            return userInfoResponse.isCanSeePrice();
        }
        return false;
    }

    public void getLastBuy() {
        checkViewAttached();
        RxUtil.unsubscribe(mLastBuySubscription);
        mLastBuySubscription = mDataManager.getLastOrderAmount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LastBuyResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showLastOrderAmountError();
                    }

                    @Override
                    public void onNext(LastBuyResponse lastBuyResponse) {
                            getMvpView().showLastOrderAmount(lastBuyResponse);
                    }
                });
    }



}
