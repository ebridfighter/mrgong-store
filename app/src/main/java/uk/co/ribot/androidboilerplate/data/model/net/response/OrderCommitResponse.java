package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.util.ArrayList;

/**
 * 提交订单的返回
 * 包含生成的订单信息
 * 订单数为复数因为可能拆单
 *
 * Created by Dong on 2017/10/25.
 */

public class OrderCommitResponse {
    private ArrayList<OrderListResponse.ListBean> orders;

    public ArrayList<OrderListResponse.ListBean> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderListResponse.ListBean> orders) {
        this.orders = orders;
    }
}
