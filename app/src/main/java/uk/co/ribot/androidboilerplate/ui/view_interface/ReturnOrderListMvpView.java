package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnDataResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/22.
 */
public interface ReturnOrderListMvpView extends MvpView {
    void showReturnOrders(ReturnDataResponse returnDataResponse);
    void showReturnOrdersError(String error);
}

