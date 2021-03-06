package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryDetailFragmentMvpView;

/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryDetailFragmentPresenter extends BasePresenter<MakeInventoryDetailFragmentMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;


    @Inject
    public MakeInventoryDetailFragmentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MakeInventoryDetailFragmentMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
