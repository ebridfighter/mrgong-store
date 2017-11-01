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
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;

/**
 * Created by mike on 2017/11/1.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderListResponse.ListBean> mOrders;

    @Inject
    public OrderAdapter() {
        mOrders = new ArrayList<>();
    }

    public void setRibots(List<OrderListResponse.ListBean> orders) {
        mOrders = orders;
    }

    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new OrderAdapter.OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.OrderViewHolder holder, int position) {
        OrderListResponse.ListBean order = mOrders.get(position);
        holder.nameTextView.setText(order.getName());
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_hex_color)
        View hexColorView;
        @BindView(R.id.text_name)
        TextView nameTextView;
        @BindView(R.id.text_email) TextView emailTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
