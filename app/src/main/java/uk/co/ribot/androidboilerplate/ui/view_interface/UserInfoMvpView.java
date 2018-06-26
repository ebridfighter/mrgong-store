package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/27.
 */
public interface UserInfoMvpView extends MvpView {
    void logoutError();

    void logout();
}
