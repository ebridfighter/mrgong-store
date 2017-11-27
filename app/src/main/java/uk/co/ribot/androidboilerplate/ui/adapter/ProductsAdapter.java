package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.remote.RunwiseService;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;

/**
 * Created by mike on 2017/10/10.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<ProductListResponse.Product> mRibots;

    @Inject
    public ProductsAdapter() {
        mRibots = new ArrayList<>();
    }
    public void setRibots(List<ProductListResponse.Product> ribots) {
        mRibots = ribots;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pirce, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ProductListResponse.Product product = mRibots.get(position);
        holder.mTvName.setText(product.getName());
        holder.mTvNumber.setText(product.getDefaultCode());
        holder.mTvContent.setText(product.getUnit());
        if (product.getImage() != null){
            FrecoFactory.getInstance(holder.itemView.getContext()).disPlay(holder.mSdvProduct, RunwiseService.ENDPOINT + product.getImage().getImageSmall());
        }
        if (product.isTwoUnit()){
            holder.mTvValue.setText("￥"+ NumberUtil.getIOrD(product.getSettlePrice()+"") + "/" +product.getSettleUomId());
        }else{
            holder.mTvValue.setText("￥"+NumberUtil.getIOrD(product.getPrice()+"") + "/" +product.getUom());
        }
    }

    @Override
    public int getItemCount() {
        return mRibots.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sdv_product)
        SimpleDraweeView mSdvProduct;
        @BindView(R.id.tv_number)
        TextView mTvNumber;
        @BindView(R.id.tv_value)
        TextView mTvValue;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_content)
        TextView mTvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
