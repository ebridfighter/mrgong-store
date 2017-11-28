package uk.co.ribot.androidboilerplate.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.runwise.commomlibrary.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.PandianResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.remote.RunwiseService;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2017/11/28.
 */

public class MakeInventoryDetailAdapter extends BaseAdapter<MakeInventoryDetailAdapter.ViewHolder> {

    List<PandianResponse.InventoryBean.LinesBean> mLinesBeans = new ArrayList<>();
    private boolean mReading;
    @Inject
    public MakeInventoryDetailAdapter() {
    }

    public void setData(List<PandianResponse.InventoryBean.LinesBean> linesBeans){
        mLinesBeans.clear();
        mLinesBeans.addAll(linesBeans);
        notifyDataSetChanged();
    }

    @Override
    public MakeInventoryDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayout(parent.getContext(),R.layout.item_make_inventory_detail);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MakeInventoryDetailAdapter.ViewHolder holder, int position) {
        final PandianResponse.InventoryBean.LinesBean bean =  mLinesBeans.get(position);
        ProductListResponse.Product productBean = bean.getProduct();
        if (productBean != null){
            holder.mTvName.setText(productBean.getName());
            holder.mTvNumber.setText(productBean.getDefaultCode() + " | ");
            holder.mContent.setText(productBean.getUnit());
            if(productBean.getImage() != null)
                FrecoFactory.getInstance(holder.itemView.getContext()).disPlay(holder.mSdvProduct, RunwiseService.ENDPOINT + productBean.getImage().getImageSmall());
        }
        holder.mTvDateNumber.setText(bean.getLotNum());

        if( bean.getDiff() == 0 || mReading) {
            holder.mTvValue.setText("--");
            holder.mTvValue.setTextColor(Color.parseColor("#9b9b9b"));
        }
        else if(bean.getDiff() > 0) {
            holder.mTvValue.setText(NumberUtil.getIOrD(bean.getDiff()));
            holder.mTvValue.setTextColor(Color.parseColor("#9cb62e"));
        }
        else{
            holder.mTvValue.setText(NumberUtil.getIOrD(bean.getDiff()));
            holder.mTvValue.setTextColor(Color.parseColor("#e75967"));
        }
        holder.mTvDateLate.setText(NumberUtil.getIOrD(bean.getActualQty()));
        holder.mTvDateLate11.setText("/"+ NumberUtil.getIOrD(bean.getTheoreticalQty()));
    }

    @Override
    public int getItemCount() {
        return mLinesBeans.size();
    }

    public void setReading(boolean reading) {
        mReading = reading;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.sdv_product)
        SimpleDraweeView mSdvProduct;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_number)
        TextView mTvNumber;
        @BindView(R.id.tv_content)
        TextView mContent;
        @BindView(R.id.tv_value)
        TextView mTvValue;
        @BindView(R.id.tv_uom)
        TextView mTvUom;
        @BindView(R.id.tv_date_number)
        TextView mTvDateNumber;
        @BindView(R.id.tv_date_late)
        TextView mTvDateLate;
        @BindView(R.id.tv_date_late11)
        TextView mTvDateLate11;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
