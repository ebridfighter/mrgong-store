package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.ShopInfoResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/3.
 */

public interface MoreMvpView extends MvpView {

    void logout();
    void logoutError();
    void showShopInfo(ShopInfoResponse shopInfoResponse);
    void showUserInfo(UserInfoResponse userInfoResponse);
    void showProcumentPermission(UserInfoResponse userInfoResponse);
    void showTransferPermission(UserInfoResponse userInfoResponse);

}
