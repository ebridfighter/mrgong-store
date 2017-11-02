package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.OrderListWrap;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;

/**
 * Created by mike on 2017/11/1.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderListWrap> mOrderListWraps;
    private List<OrderListResponse.ListBean> mOrders;
    private List<ReturnOrderListResponse.ListBean> mReturnOrders;

    public static final int VIEW_TYPE_ORDER = 1 << 0;
    public static final int VIEW_TYPE_RETURN_ORDER = 1 << 1;


    @Inject
    public OrderAdapter() {
        mOrderListWraps = new ArrayList<>();
    }

    public void setOrders(List<OrderListResponse.ListBean> orders) {
        mOrders = orders;
        for (OrderListResponse.ListBean listBean : orders) {
            OrderListWrap orderListWrap = new OrderListWrap();
            orderListWrap.setOrderListBean(listBean);
            mOrderListWraps.add(orderListWrap);
        }
    }

    public void setReturnOrders(List<ReturnOrderListResponse.ListBean> returnOrders) {
        mReturnOrders = returnOrders;
        for (int i = 0; i < returnOrders.size(); i++) {
            ReturnOrderListResponse.ListBean listBean = returnOrders.get(i);
            OrderListWrap orderListWrap = new OrderListWrap();
            orderListWrap.setReturnOrderListBean(listBean);
            mOrderListWraps.add(i, orderListWrap);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mOrderListWraps.get(position).getOrderListBean() != null ? VIEW_TYPE_ORDER : VIEW_TYPE_RETURN_ORDER;
    }

    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_ORDER){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ribot, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ribot, parent, false);
        }
        return new OrderAdapter.OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.OrderViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ORDER){
            OrderListResponse.ListBean order = mOrderListWraps.get(position).getOrderListBean();
            holder.nameTextView.setText(order.getName());
        }else{
            ReturnOrderListResponse.ListBean returnOrder = mOrderListWraps.get(position).getReturnOrderListBean();
            holder.nameTextView.setText("退："+returnOrder.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mOrderListWraps.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_hex_color)
        View hexColorView;
        @BindView(R.id.text_name)
        TextView nameTextView;
        @BindView(R.id.text_email)
        TextView emailTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
