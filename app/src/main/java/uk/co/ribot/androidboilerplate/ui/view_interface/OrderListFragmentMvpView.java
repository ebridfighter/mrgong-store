package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.OrderResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/22.
 */
public interface OrderListFragmentMvpView extends MvpView {
   void showOrders(OrderResponse orderResponse);
   void showOrdersError();
}
