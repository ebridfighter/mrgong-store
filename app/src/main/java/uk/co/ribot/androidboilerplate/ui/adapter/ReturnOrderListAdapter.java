package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.runwise.commomlibrary.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2017/11/27.
 */

public class ReturnOrderListAdapter extends BaseAdapter<ReturnOrderListAdapter.ViewHolder> {

    List<ReturnOrderListResponse.ListBean> mReturnOrders = new ArrayList<>();
    private boolean mCanSeePrice;
    
    @Inject
    public ReturnOrderListAdapter(){}

    public void setData(List<ReturnOrderListResponse.ListBean> returnOrders){
        mReturnOrders.clear();
        mReturnOrders.addAll(returnOrders);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayout(parent.getContext(),R.layout.item_return_order);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReturnOrderListResponse.ListBean bean = mReturnOrders.get(position);

        if("process".equals(bean.getState())) {
            holder.mTvPayStatus.setText("退货中");
            holder.mIvOrderStatus.setImageResource(R.drawable.state_delivery_7_return);
        }
        //已发货
        else if("done".equals(bean.getState())) {
            holder.mTvPayStatus.setText("已退货");
            holder.mIvOrderStatus.setImageResource(R.drawable.state_delivery_7_return);
        }
        else  {
            holder.mTvPayStatus.setText("已退货");
            holder.mIvOrderStatus.setImageResource(R.drawable.state_delivery_7_return);
        }

        holder.mTvPayTitle.setText(bean.getName());
        holder.mTvPayDate.setText(TimeUtils.getTimeStamps3(bean.getCreateDate()));
        holder.mTvPatSum.setText("共"+ NumberUtil.getIOrD(bean.getAmount())+"件商品");
        if(mCanSeePrice) {
            holder.mTvPayMoney.setVisibility(View.VISIBLE);
            holder.mTvPayMoney.setText("共"+bean.getAmountTotal());
        }
        else {
            holder.mTvPayMoney.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mReturnOrders.size();
    }

    public void setCanSeePrice(boolean canSeePrice) {
        mCanSeePrice = canSeePrice;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_order_status)
        ImageView mIvOrderStatus;
        @BindView(R.id.tv_pay_title)
        TextView mTvPayTitle;
        @BindView(R.id.tv_pay_status)
        TextView mTvPayStatus;
        @BindView(R.id.tv_pay_date)
        TextView mTvPayDate;
        @BindView(R.id.tv_pat_sum)
        TextView mTvPatSum;
        @BindView(R.id.tv_pay_money)
        TextView mTvPayMoney;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
