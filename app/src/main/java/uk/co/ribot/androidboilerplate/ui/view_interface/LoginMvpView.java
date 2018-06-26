package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.database.UserBean;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/9/28.
 */

public interface LoginMvpView extends MvpView {

    void onSuccess();

    void showError(String error);

    void loginConflict();
    void getHostError(String error);

    void loadUserListSuccess(List<UserBean> userBeanList);

}
