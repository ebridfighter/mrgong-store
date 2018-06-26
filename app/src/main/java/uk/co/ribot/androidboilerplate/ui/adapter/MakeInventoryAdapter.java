package uk.co.ribot.androidboilerplate.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.tools.UserUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2017/11/28.
 */

public class MakeInventoryAdapter extends BaseAdapter<MakeInventoryAdapter.ViewHolder> {

    List<InventoryResponse.ListBean> mListBeans = new ArrayList<>();

    String mUserName;
    boolean mCanSeePrice;

    @Inject
    public MakeInventoryAdapter() {
    }

    public void setData(List<InventoryResponse.ListBean> listBeans) {
        mListBeans.clear();
        mListBeans.addAll(listBeans);
        notifyDataSetChanged();
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
    public void setCanSeePrice(boolean canSeePrice) {
        mCanSeePrice = canSeePrice;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayout(parent.getContext(), R.layout.item_make_inventory);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setOnItemListener(holder.itemView,position);
        final InventoryResponse.ListBean listBean = mListBeans.get(position);
        holder.mTvPayDate.setText(TimeUtils.getTimeStamps3(listBean.getCreateDate()));
        holder.mTvName.setText(listBean.getCreateUser());
        if ("confirm".equals(listBean.getState()) && listBean.getCreateUser().equals(mUserName)) {
            holder.mTvMoney.setVisibility(View.GONE);
            holder.mBtnHandler.setVisibility(View.VISIBLE);
            holder.mBtnHandler.setOnClickListener(v -> {
                if (mOnChildItemClickListener!= null){
                    mOnChildItemClickListener.onItemClick(v,position);
                }
            });
        }
        else if ("confirm".equals(listBean.getState()) && !listBean.getCreateUser().equals(mUserName)) {
            holder.mTvMoney.setVisibility(View.VISIBLE);
            holder.mBtnHandler.setVisibility(View.GONE);
            holder.mTvMoney.setTextColor(Color.parseColor("#3d3d3d"));
            holder.mTvMoney.setText("盘点中");
        }
        else {
            if(mCanSeePrice) {
                holder.mTvMoney.setText("¥"+ UserUtils.formatPrice(listBean.getValue()+"")+"");
                if(listBean.getValue() >= 0) {
                    holder.mTvMoney.setTextColor(Color.parseColor("#9cb62e"));
                }
                else{
                    holder.mTvMoney.setTextColor(Color.parseColor("#e75967"));
                }
            }
            else{
                holder.mTvMoney.setText(listBean.getNum()+"");
                if(Double.parseDouble(listBean.getNum()) >= 0) {
                    holder.mTvMoney.setTextColor(Color.parseColor("#9cb62e"));
                }
                else{
                    holder.mTvMoney.setTextColor(Color.parseColor("#e75967"));
                }
            }

            holder.mTvMoney.setVisibility(View.VISIBLE);
            holder.mBtnHandler.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mListBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_pay_date)
        TextView mTvPayDate;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_money)
        TextView mTvMoney;
        @BindView(R.id.btn_handler)
        TextView mBtnHandler;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
