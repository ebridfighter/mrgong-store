package uk.co.ribot.androidboilerplate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProcurementResponse;
import uk.co.ribot.androidboilerplate.injection.ActivityContext;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2018/1/4.
 */

public class ProcurementListAdapter extends BaseAdapter<ProcurementListAdapter.ViewHolder> {

    private List<ProcurementResponse.ListBean.ProductsBean> mListBeans = new ArrayList<>();
    HashMap<Integer, ProcurementResponse.ListBean> mHeadMap = new HashMap<>();
    Context mContext;

    public void setHeadMap(HashMap<Integer, ProcurementResponse.ListBean> headMap) {
        mHeadMap = headMap;
    }

    public void setSelection(ArrayList<Integer> selection) {
        mSelection = selection;
    }

    ArrayList<Integer> mSelection = new ArrayList<>();

    @Inject
    public ProcurementListAdapter(@ActivityContext Context context) {
        mContext = context;
    }

    public void setData(List<ProcurementResponse.ListBean.ProductsBean> list) {
        mListBeans.clear();
        mListBeans.addAll(list);
        notifyDataSetChanged();
    }

    public void appendData(List<ProcurementResponse.ListBean.ProductsBean> list) {
        mListBeans.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProcurementListAdapter.ViewHolder viewHolder = new ProcurementListAdapter.ViewHolder(getLayout(parent.getContext(), R.layout.procurement_layout_item));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProcurementResponse.ListBean.ProductsBean bean = mListBeans.get(position);
//        holder.mName.setText(bean.getName());
//        StringBuffer sb = new StringBuffer(bean.getDefaultCode());
//        sb.append("  ").append(bean.getUnit());
//        DecimalFormat df = new DecimalFormat("#.##");
//        holder.mContent.setText(sb.toString());
//        holder.mTvCount.setText(NumberUtil.getIOrD(String.valueOf(bean.getQty())) + bean.getProductUom());
//        if (bean.getImage() != null) {
//            FrecoFactory.getInstance(holder.itemView.getContext()).disPlay(holder.mProductImage, RunwiseService.ENDPOINT + bean.getImage().getImageSmall());
//        }
        if (isHead(position)) {
            holder.mRlHead.setVisibility(View.VISIBLE);
            ProcurementResponse.ListBean listBean = mHeadMap.get(position);
            holder.mTvDate.setText(listBean.getDate());
            holder.mTvCaiGouRen.setText("采购人:" + listBean.getUser());
        } else {
            holder.mRlHead.setVisibility(View.GONE);
        }
    }

    private boolean isHead(int position) {
        for (Integer integer : mSelection) {
            if (position == integer) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_cai_gou_ren)
        TextView mTvCaiGouRen;
        @BindView(R.id.rl_head)
        RelativeLayout mRlHead;
        @BindView(R.id.productImage)
        SimpleDraweeView mProductImage;
        @BindView(R.id.tv_tag)
        TextView mTvTag;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.editLL)
        LinearLayout mEditLL;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.content)
        TextView mContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
