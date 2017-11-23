package uk.co.ribot.androidboilerplate.ui.presenter;


import javax.inject.Inject;

import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderListMvpView;

/**
 * Created by mike on 2017/11/22.
 */
public class OrderListPresenter extends BasePresenter<OrderListMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;


    @Inject
    public OrderListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(OrderListMvpView mvpView) {
        super.attachView(mvpView);
    }




    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
