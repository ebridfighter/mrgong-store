package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.runwise.commomlibrary.util.NumberUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderState;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2017/11/15.
 */

public class OrderCommitAdapter extends BaseAdapter<RecyclerView.ViewHolder> {
    List<OrderListResponse.ListBean> mListOrders = new ArrayList<>();

    private boolean mCanSeePrice;
    private boolean mShowUploadButton;

    public void setCanSeePrice(boolean canSeePrice) {
        mCanSeePrice = canSeePrice;
    }

    public void setShowUploadButton(boolean showUploadButton) {
        mShowUploadButton = showUploadButton;
    }

    @Inject
    public OrderCommitAdapter() {
    }

    public void setData(List<OrderListResponse.ListBean> listOrders) {
        mListOrders.clear();
        mListOrders.addAll(listOrders);
        notifyDataSetChanged();
    }

    private int mType;

    public void setType(int type) {
        mType = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 0) {
            viewHolder = new HeaderViewHolder(getLayout(parent.getContext(), R.layout.item_order_success_header));
        } else {
            viewHolder = new ViewHolder(getLayout(parent.getContext(), R.layout.item_order_success));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (mType == 0) {//修改
                if (mListOrders.size() > 1) {
                    headerViewHolder.mTvContent.setText("由于商品种类不同，您的订单已被拆分为以下几个订单");
                } else {
                    headerViewHolder.mTvContent.setText("修改成功，我们将尽快确认您的订单");
                }
                headerViewHolder.mTvTitle.setText("修改成功");
                headerViewHolder.mIvIcon.setImageResource(R.drawable.default_ico_eidt_successed);
            } else {//下单成功
                if (mListOrders.size() > 1) {
                    headerViewHolder.mTvContent.setText("由于商品种类不同，您的订单已被拆分为以下几个订单");
                } else {
                    headerViewHolder.mTvContent.setText("提交成功，我们将尽快确认您的订单");
                }
                headerViewHolder.mTvTitle.setText("下单成功");
                headerViewHolder.mIvIcon.setImageResource(R.drawable.default_ico_order_successed);
            }
            headerViewHolder.mBtnUpload.setVisibility(mShowUploadButton ? View.VISIBLE : View.GONE);
            setOnChildItemListener(headerViewHolder.mBtnUpload,position);

        } else {
            int realPosition = position-1;
            ViewHolder viewHolder = (ViewHolder) holder;
            OrderListResponse.ListBean order = mListOrders.get(realPosition);
            viewHolder.mTvOrderName.setText(order.getName());
            viewHolder.mTvOrderState.setText(order.getState());
            if (realPosition == mListOrders.size() - 1) {
                viewHolder.mRlRoot.setBackgroundResource(R.drawable.background_order_bottom);
            } else {
                viewHolder.mRlRoot.setBackgroundResource(R.drawable.background_order_mid);
            }
            //预计时间
            StringBuffer etSb = new StringBuffer();
            if (order.getState().equals(OrderState.DRAFT.getName()) || order.getState().equals(OrderState.SALE.getName())) {
                etSb.append("预计").append(formatTimeStr(order.getEstimatedDate())).append("送达");
            } else if (order.getState().equals(OrderState.PEISONG.getName())) {
                etSb.append("预计").append(formatTimeStr(order.getEstimatedDate())).append("送达");
            } else {
                etSb.append(order.getDoneDatetime()).append("已送达");
            }
            viewHolder.mTvOrderTitle.setText(etSb.toString());
            viewHolder.mTvOrderState.setText(OrderState.getValueByName(order.getState()));
            StringBuilder sb = new StringBuilder();
            if (mCanSeePrice){
                sb.append("￥").append(NumberUtil.getIOrD(order.getAmountTotal())).append("，");
            }
            sb.append((int) order.getAmount()).append("件商品");
            viewHolder.mTvOrderDesc.setText(sb.toString());
            setOnChildItemListener(viewHolder.mTvOrderAction,realPosition);
        }
    }
    SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat sdfTarget = new SimpleDateFormat("MM月dd日",Locale.getDefault());
    private String formatTimeStr(String str){
        try{
            return sdfTarget.format(sdfSource.parse(str));
        }catch (ParseException e){
            e.printStackTrace();
            return str;
        }
    }
    @Override
    public int getItemCount() {
        return mListOrders.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.btn_upload)
        Button mBtnUpload;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_title)
        TextView mTvOrderTitle;
        @BindView(R.id.tv_order_name)
        TextView mTvOrderName;
        @BindView(R.id.tv_order_desc)
        TextView mTvOrderDesc;
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState;
        @BindView(R.id.tv_order_action)
        TextView mTvOrderAction;
        @BindView(R.id.rl_root)
        RelativeLayout mRlRoot;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
