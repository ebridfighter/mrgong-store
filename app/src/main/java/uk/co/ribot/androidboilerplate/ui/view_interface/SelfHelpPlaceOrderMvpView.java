package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/16.
 */

public interface SelfHelpPlaceOrderMvpView extends MvpView{

    void getUserInfoSuccess(UserInfoResponse userInfoResponse);
    void commitOrderSuccess(OrderCommitResponse orderCommitResponse);
    void commitOrderError();
    void showProductSuccess(ProductBean product);
}
