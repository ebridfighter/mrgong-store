package uk.co.ribot.androidboilerplate.data.model.business;

import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;

/**
 * Created by mike on 2017/11/2.
 */

public class OrderListWrap {
    OrderListResponse.ListBean mOrderListBean;
    ReturnOrderListResponse.ListBean mReturnOrderListBean;
    public OrderListResponse.ListBean getOrderListBean() {
        return mOrderListBean;
    }

    public void setOrderListBean(OrderListResponse.ListBean orderListBean) {
        mOrderListBean = orderListBean;
    }

    public ReturnOrderListResponse.ListBean getReturnOrderListBean() {
        return mReturnOrderListBean;
    }

    public void setReturnOrderListBean(ReturnOrderListResponse.ListBean returnOrderListBean) {
        mReturnOrderListBean = returnOrderListBean;
    }
}
