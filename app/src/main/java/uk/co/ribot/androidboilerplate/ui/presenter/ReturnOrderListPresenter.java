package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ReturnOrderListMvpView;

/**
 * Created by mike on 2017/11/22.
 */
public class ReturnOrderListPresenter extends BasePresenter<ReturnOrderListMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;


    @Inject
    public ReturnOrderListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ReturnOrderListMvpView mvpView) {
        super.attachView(mvpView);
    }

    public UserInfoResponse loadUser(){
        return mDataManager.loadUser();
    }

    public void getReturnOrderList() {
        checkViewAttached();
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = mDataManager.getReturnOrderList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReturnDataResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "网络错误");
                        getMvpView().showReturnOrdersError(e.getMessage());
                    }

                    @Override
                    public void onNext(ReturnDataResponse returnDataResponse) {
                        getMvpView().showReturnOrders(returnDataResponse);
                    }
                });
    }


    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
