package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/15.
 */

public class CommonPresenter extends BasePresenter<MvpView>{
    private final DataManager mDataManager;
    @Inject
    public CommonPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public boolean canSeePrice(){
        UserInfoResponse userInfoResponse = mDataManager.loadUser();
        if (userInfoResponse != null){
            return userInfoResponse.isCanSeePrice();
        }
        return false;
    }



}
