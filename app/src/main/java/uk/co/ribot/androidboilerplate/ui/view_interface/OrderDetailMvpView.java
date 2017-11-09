package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderDetailResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/9.
 */

public interface OrderDetailMvpView extends MvpView{
    void showCategorys(CategoryResponse categoryResponse);
    void showOrder(OrderDetailResponse orderDetailResponse);
    void showCategorysError(CategoryResponse categoryResponse);
}
