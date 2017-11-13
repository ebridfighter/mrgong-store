package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderState;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.remote.RunwiseService;
import uk.co.ribot.androidboilerplate.tools.UserUtils;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2017/11/9.
 */

public class OrderProductsAdapter extends BaseAdapter<OrderProductsAdapter.ViewHolder> {

    List<OrderListResponse.ListBean.LinesBean> mProducts;
    private boolean mHasReturn;          //是否有退货，默认没有
    private boolean isTwoUnit;           //双单位,有值就显示
    //当前状态
    private String mStatus;
    private boolean mCanSeePrice;

    public void setHasReturn(boolean hasReturn) {
        this.mHasReturn = hasReturn;
    }

    public void setTwoUnit(boolean twoUnit) {
        isTwoUnit = twoUnit;
    }

    public void setProducts(List<OrderListResponse.ListBean.LinesBean> products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    @Inject
    public OrderProductsAdapter() {

    }

    @Override
    public OrderProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(getLayout(parent.getContext(), R.layout.item_order_product));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderProductsAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final OrderListResponse.ListBean.LinesBean bean = mProducts.get(position);
        int pId = bean.getProductID();
        if (!TextUtils.isEmpty(bean.getImageMedium())) {
            FrecoFactory.getInstance(holder.itemView.getContext()).disPlay(holder.mSdvProduct, RunwiseService.ENDPOINT + bean.getImageMedium());
        }

        int puq = (int) bean.getProductUomQty();
        int dq = (int) bean.getDeliveredQty();
        if ((OrderState.DONE.getName().equals(mStatus) || OrderState.RATED.getName().equals(mStatus)) && bean.getDeliveredQty() != bean.getProductUomQty()) {
            holder.mTvOldPrice.setText("x" + puq);
            holder.mTvNowPrice.setText("x" + dq);
            holder.mTvOldPrice.setVisibility(View.VISIBLE);
        } else {
            holder.mTvOldPrice.setVisibility(View.GONE);
            holder.mTvNowPrice.setText("x" + puq);
        }

        StringBuffer sb = new StringBuffer(bean.getDefaultCode());
        sb.append(" | ").append(bean.getUnit());
        if (mCanSeePrice) {
            if (isTwoUnit) {
                sb.append("\n").append(UserUtils.formatPrice(String.valueOf(bean.getProductSettlePrice()))).append("元/").append(bean.getSettleUomId());
            } else {
                sb.append("\n").append(UserUtils.formatPrice(String.valueOf(bean.getProductPrice()))).append("元/").append(bean.getProductUom());
            }
        }
        holder.mTvUnit.setText(bean.getProductUom());
        holder.mTvContent.setText(sb.toString());
        if (isTwoUnit) {
            holder.mTvWeight.setVisibility(View.VISIBLE);
        } else {
            holder.mTvWeight.setVisibility(View.INVISIBLE);
        }
        holder.mTvName.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public void setCanSeePrice(boolean canSeePrice) {
        mCanSeePrice = canSeePrice;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sdv_product)
        SimpleDraweeView mSdvProduct;
        @BindView(R.id.tv_unit)
        TextView mTvUnit;
        @BindView(R.id.tv_old_price)
        TextView mTvOldPrice;
        @BindView(R.id.tv_now_price)
        TextView mTvNowPrice;
        @BindView(R.id.ll_count)
        LinearLayout mLlCount;
        @BindView(R.id.tv_weight)
        TextView mTvWeight;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.itemRootView)
        RelativeLayout mItemRootView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
