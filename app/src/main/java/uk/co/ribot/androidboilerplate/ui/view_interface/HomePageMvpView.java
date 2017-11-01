package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/1.
 */

public interface HomePageMvpView extends MvpView {
    void showOrders(List<OrderListResponse.ListBean> orders);
    void showOrdersEmpty();
    void showError();;
}
