package uk.co.ribot.androidboilerplate.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.runwise.commomlibrary.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderDoAction;
import uk.co.ribot.androidboilerplate.data.model.business.OrderState;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderResponse;
import uk.co.ribot.androidboilerplate.injection.ActivityContext;
import uk.co.ribot.androidboilerplate.tools.SystemUpgradeHelper;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;
import uk.co.ribot.androidboilerplate.view.RunwiseDialog;

/**
 * Created by mike on 2017/11/23.
 */

public class OrderListAdapter extends BaseAdapter<OrderListAdapter.ViewHolder> {

    private List<OrderResponse.ListBean> mOrders = new ArrayList<>();
    RunwiseDialog mRunwiseDialog;
    Context mContext;
    boolean mCanSeePrice;
    String mUserName;

    @Inject
    public OrderListAdapter(@ActivityContext Context context) {
        mRunwiseDialog = new RunwiseDialog(context);
        mContext = context;
    }

    public void setData(List<OrderResponse.ListBean> list) {
        mOrders.clear();
        mOrders.addAll(list);
        notifyDataSetChanged();
    }

    public void appendData(List<OrderResponse.ListBean> list) {
        mOrders.addAll(list);
        notifyDataSetChanged();
    }

    public void setCanSeePrice(boolean canSeePrice) {
        mCanSeePrice = canSeePrice;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(getLayout(parent.getContext(), R.layout.item_order));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
        setOnItemListener(holder.itemView, position);
        final OrderResponse.ListBean bean = mOrders.get(position);
        //待确认
        if ("draft".equals(bean.getState())) {
            holder.mTvPayStatus.setText("待确认");
            holder.mTvPay.setVisibility(View.VISIBLE);
            holder.mTvPay.setText("取消订单");
            holder.mIvOrderStatus.setImageResource(R.drawable.state_restaurant_1_tocertain);
            holder.mTvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!SystemUpgradeHelper.getInstance(mContext).check(mContext))
                        return;
                    mRunwiseDialog.setMessage("您确定要取消订单吗?");
                    mRunwiseDialog.setModel(RunwiseDialog.BOTH);
                    mRunwiseDialog.setLeftBtnListener("不取消了", null);
                    mRunwiseDialog.setRightBtnListener("取消订单", new RunwiseDialog.DialogListener() {
                        @Override
                        public void doClickButton(Button btn, RunwiseDialog mRunwiseDialog) {
//                            cancleOrderRequest(bean);
                        }
                    });
                    mRunwiseDialog.show();
                }
            });
        }
        //已确认
        else if ("sale".equals(bean.getState())) {
            holder.mTvPay.setVisibility(View.GONE);
            holder.mTvPayStatus.setText("已确认");
            holder.mIvOrderStatus.setImageResource(R.drawable.state_restaurant_2_certain);
        }
        //已发货
        else if ("peisong".equals(bean.getState())) {
            holder.mTvPayStatus.setText("已发货");
            holder.mTvPay.setVisibility(View.VISIBLE);
            String btnText;
            if (bean.isIsDoubleReceive()) {
                if (bean.isIsFinishTallying()) {
                    //双人收货
                    btnText = "收货";
                } else {
                    //双人点货
                    btnText = "点货";
                }
            } else {
                //正常收货
                btnText = "收货";
            }
            holder.mTvPay.setText(btnText);
            holder.mIvOrderStatus.setImageResource(R.drawable.state_restaurant_3_delivering);
            holder.mTvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!SystemUpgradeHelper.getInstance(mContext).check(mContext))
                        return;
                    //在这里做判断，是正常收货，还是双人收货,同时判断点货人是谁，如果是自己，则不能再收货
                    OrderDoAction action;
                    if (bean.isIsDoubleReceive()) {
                        if (bean.getTallyingUserName().equals(mUserName)) {
                            action = OrderDoAction.SELFTALLY;
                        } else {
                            action = OrderDoAction.SETTLERECEIVE;
                        }
                    } else {
                        action = OrderDoAction.RECEIVE;
                    }
                    switch (action) {
                        case RECEIVE://正常收货
//                            Intent intent = new Intent(mContext, ReceiveActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable("order", bean);
//                            bundle.putInt("mode", 0);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
                            break;
                        case SETTLERECEIVE:
                            //点货，计入结算单位
//                            Intent sIntent = new Intent(mContext, ReceiveActivity.class);
//                            Bundle sBundle = new Bundle();
//                            sBundle.putParcelable("order", bean);
//                            sBundle.putInt("mode", 2);
//                            sIntent.putExtras(sBundle);
//                            startActivity(sIntent);
                            break;
                        case SELFTALLY:
                            mRunwiseDialog.setModel(RunwiseDialog.RIGHT);
                            mRunwiseDialog.setMessageGravity();
                            mRunwiseDialog.setMessage("您已经点过货了，应由其他人完成收货");
                            mRunwiseDialog.setRightBtnListener("确认", null);
                            mRunwiseDialog.show();
                            break;
                    }
                }
            });
        }
        //待评价
        else if ("done".equals(bean.getState())) {
            holder.mTvPayStatus.setText("待评价");
            holder.mTvPay.setVisibility(View.GONE);
            holder.mIvOrderStatus.setImageResource(R.drawable.state_restaurant_2_certain);
            holder.mTvPay.setVisibility(View.VISIBLE);
            holder.mTvPay.setText("评价");
            holder.mTvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!SystemUpgradeHelper.getInstance(mContext).check(mContext))
                        return;
//                    Intent intent2 = new Intent(mContext, EvaluateActivity.class);
//                    Bundle bundle2 = new Bundle();
//                    bundle2.putParcelable("order", bean);
//                    intent2.putExtras(bundle2);
//                    startActivity(intent2);
                }
            });
        }
        //已评价
        else if ("rated".equals(bean.getState())) {
            holder.mTvPayStatus.setText("已评价");
            holder.mTvPay.setVisibility(View.GONE);
            holder.mIvOrderStatus.setImageResource(R.drawable.state_restaurant_5_rated);
        }
        //已取消cancel
        else {
            holder.mTvPayStatus.setText("订单关闭");
            holder.mTvPay.setVisibility(View.VISIBLE);
            holder.mTvPay.setText("删除订单");
            holder.mIvOrderStatus.setImageResource(R.drawable.state_restaurant_6_closed);
            holder.mTvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!SystemUpgradeHelper.getInstance(mContext).check(mContext))
                        return;
                    mRunwiseDialog.setMessage("您确定要取消订单吗?");
                    mRunwiseDialog.setModel(RunwiseDialog.BOTH);
                    mRunwiseDialog.setLeftBtnListener("不删除了", null);
                    mRunwiseDialog.setRightBtnListener("删除订单", new RunwiseDialog.DialogListener() {
                        @Override
                        public void doClickButton(Button btn, RunwiseDialog mRunwiseDialog) {
//                            deleteOrderRequest(bean);
                        }
                    });
                    mRunwiseDialog.show();
                }
            });
        }
        holder.mTvPayTitle.setText(bean.getName());
        holder.mTvPayDate.setText(TimeUtils.getTimeStamps3(bean.getCreateDate()));
        if (bean.getState().equals(OrderState.DONE.getName()) || bean.getState().equals(OrderState.RATED.getName())) {
            holder.mTvPaySum.setText("共" + NumberUtil.getIOrD(bean.getDeliveredQty()) + "件商品");
        } else {
            holder.mTvPaySum.setText("共" + NumberUtil.getIOrD(bean.getAmount()) + "件商品");
        }
        if (mCanSeePrice) {
            holder.mTvPayMoney.setVisibility(View.VISIBLE);
            holder.mTvPayMoney.setText("共" + NumberUtil.getIOrD(bean.getAmountTotal()));
        } else {
            holder.mTvPayMoney.setVisibility(View.GONE);
        }
        if (bean.getHasReturn() > 0) {
            holder.mTvReturn.setVisibility(View.VISIBLE);
        } else {
            holder.mTvReturn.setVisibility(View.GONE);
        }
        if ((OrderState.DONE.getName().equals(bean.getState()) || OrderState.RATED.getName().equals(bean.getState())) && isShiShou(bean)) {
            holder.mTvReal.setVisibility(View.VISIBLE);
        } else {
            holder.mTvReal.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    /**
     * 是否实收
     *
     * @return
     */
    private boolean isShiShou(OrderResponse.ListBean bean) {
        for (OrderResponse.ListBean.LinesBean linesBean : bean.getLines()) {
            if (linesBean.getDeliveredQty() != linesBean.getProductUomQty()) {
                return true;
            }
        }
        return false;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_order_status)
        ImageView mIvOrderStatus;
        @BindView(R.id.tv_pay_title)
        TextView mTvPayTitle;
        @BindView(R.id.tv_real)
        TextView mTvReal;
        @BindView(R.id.tv_return)
        TextView mTvReturn;
        @BindView(R.id.tv_pay_status)
        TextView mTvPayStatus;
        @BindView(R.id.tv_pay_date)
        TextView mTvPayDate;
        @BindView(R.id.tv_pay_sum)
        TextView mTvPaySum;
        @BindView(R.id.tv_pay_money)
        TextView mTvPayMoney;
        @BindView(R.id.tv_pay)
        TextView mTvPay;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
