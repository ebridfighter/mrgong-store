package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.net.response.DashBoardResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.FinishReturnResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/1.
 */

public interface HomePageMvpView extends MvpView {
    boolean isFragmentVisible();

    void showOrders(List<OrderListResponse.ListBean> orders);

    void showReturnOrders(List<ReturnOrderListResponse.ListBean> orders);

    void showHomePageBanner(List<String> imageUrls);

    void showDashBoard(DashBoardResponse dashBoardResponse);

    void showDashBoardWithoutPrice(DashBoardResponse dashBoardResponse);

    void cancelOrderSuccess();

    void finishReturnOrderSuccess(FinishReturnResponse finishReturnResponse);

    void showOrdersEmpty();

    void showReturnOrdersEmpty();

    void showOrdersError();

    void showReturnOrdersError();

    void showHomePageBannerError();

    void showDashBoardError();

    void cancelOrderError();

    void finishReturnOrderError();
}
