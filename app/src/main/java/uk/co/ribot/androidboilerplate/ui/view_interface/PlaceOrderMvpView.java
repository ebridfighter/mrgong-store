package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.LastBuyResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/6.
 */

public interface PlaceOrderMvpView extends MvpView {
    void showLastOrderAmount(LastBuyResponse lastBuyResponse);
    void showLastOrderAmountError();
}
