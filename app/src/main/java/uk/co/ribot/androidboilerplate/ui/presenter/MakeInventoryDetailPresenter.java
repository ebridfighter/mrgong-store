package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryDetailMvpView;

/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryDetailPresenter extends BasePresenter<MakeInventoryDetailMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;


    @Inject
    public MakeInventoryDetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MakeInventoryDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    public UserInfoResponse loadUserInfoResponse(){
        return mDataManager.loadUser();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
