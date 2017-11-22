package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
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

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
