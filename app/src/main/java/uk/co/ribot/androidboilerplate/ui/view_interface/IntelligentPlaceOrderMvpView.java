package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.IntelligentProductDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/14.
 */

public interface IntelligentPlaceOrderMvpView extends MvpView {

    void getUserInfoSuccess(UserInfoResponse userInfoResponse);
    void showIntelligentProducts(IntelligentProductDataResponse intelligentProductDataResponse);
    void showIntelligentProductsError();
    void commitOrderSuccess(OrderCommitResponse orderCommitResponse);
    void commitOrderError();
}
