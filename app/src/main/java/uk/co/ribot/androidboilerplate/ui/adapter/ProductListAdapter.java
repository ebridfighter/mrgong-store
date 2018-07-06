package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.eventbus.EventBus;
import com.runwise.commomlibrary.util.NumberUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.event.ProductCountUpdateEvent;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;
import uk.co.ribot.androidboilerplate.ui.activity.PlaceOrderProductListImproveActivity;
import uk.co.ribot.androidboilerplate.ui.base.ProductCountSetter;

public class ProductListAdapter extends BaseAdapter{
    //    ProductBean
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;


    ProductCountSetter productCountSetter;
    boolean canSeePrice = false;
    DecimalFormat df = new DecimalFormat("#.##");
    private List<ProductBean> mData;

    public ProductListAdapter(@Nullable List<ProductBean> data,boolean canSeePrice) {
//        super(R.layout.item_product_with_subcategory, data);
        this.canSeePrice = canSeePrice;
        mData = data;
    }

    public void setProductCountSetter(ProductCountSetter productCountSetter) {
        this.productCountSetter = productCountSetter;
    }

    public void setList(List<ProductBean> productBeanList){
        if (mData == null){
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(productBeanList);
        notifyDataSetChanged();
    }

    public List<ProductBean> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_with_subcategory, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(R.id.view_tag,viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag(R.id.view_tag);
        }
        ProductBean listBean = mData.get(position);
        if (position == 0) {
            String headText = listBean.getCategoryChild();
            viewHolder.mStickHeader.setVisibility(!TextUtils.isEmpty(headText)?View.VISIBLE:View.GONE);
            viewHolder.mTvHeader.setText(headText);
            viewHolder.mFoodMain.setTag(FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(listBean.getCategoryChild(), mData.get(position - 1).getCategoryChild())) {
                viewHolder.mStickHeader.setVisibility(View.VISIBLE);
                viewHolder.mTvHeader.setText(listBean.getCategoryChild());
                viewHolder.mFoodMain.setTag(HAS_STICKY_VIEW);
            } else {
                viewHolder.mStickHeader.setVisibility(View.GONE);
                viewHolder.mFoodMain.setTag(NONE_STICKY_VIEW);
            }
        }
        convertView.setContentDescription(listBean.getCategoryChild());

        //标签
        if (TextUtils.isEmpty(listBean.getProductTag())) {
            viewHolder.mIvProductSale.setVisibility(View.GONE);
        } else {
            viewHolder.mIvProductSale.setVisibility(View.VISIBLE);
        }
        double count = productCountSetter.getCount(listBean);
        viewHolder.mTvProductCount.setText(NumberUtil.getIOrD(count) + listBean.getSaleUom());
        //先根据集合里面对应个数初始化一次
        if (count > 0) {
            viewHolder.mTvProductCount.setVisibility(View.VISIBLE);
            viewHolder.mIvProductReduce.setVisibility(View.VISIBLE);
            viewHolder.mIvProductAdd.setBackgroundResource(R.drawable.ic_order_btn_add_green_part);
        } else {
            viewHolder.mTvProductCount.setVisibility(View.INVISIBLE);
            viewHolder.mIvProductReduce.setVisibility(View.INVISIBLE);
            viewHolder.mIvProductAdd.setBackgroundResource(R.drawable.order_btn_add_gray);
        }
        /**
         * 减
         */
        viewHolder.mIvProductReduce.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int currentNum = mCountMap.get(listBean)==null?0:mCountMap.get(listBean);
                double currentNum = productCountSetter.getCount(listBean);
                if (currentNum > 0) {
                    //https://stackoverflow.com/questions/179427/how-to-resolve-a-java-rounding-double-issue
                    //防止double的问题
                    currentNum = BigDecimal.valueOf(currentNum).subtract(BigDecimal.ONE).doubleValue();
                    if (currentNum < 0) currentNum = 0;
                    viewHolder.mTvProductCount.setText(NumberUtil.getIOrD(currentNum) + listBean.getSaleUom());
                    productCountSetter.setCount(listBean, currentNum);
                    if (currentNum == 0) {
                        v.setVisibility(View.INVISIBLE);
                        viewHolder.mTvProductCount.setVisibility(View.INVISIBLE);
                        viewHolder.mIvProductAdd.setBackgroundResource(R.drawable.order_btn_add_gray);
                    }
                    ProductCountUpdateEvent productCountUpdateEvent = new ProductCountUpdateEvent(listBean, currentNum);
                    productCountUpdateEvent.setException(ProductListAdapter.this);
                    BoilerplateApplication.get(v.getContext()).getComponent().eventBus().post(productCountUpdateEvent);
                }

            }
        });

        /**
         * 加
         */
        viewHolder.mIvProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double currentNum = productCountSetter.getCount(listBean);
                currentNum = BigDecimal.valueOf(currentNum).add(BigDecimal.ONE).doubleValue();
                viewHolder.mTvProductCount.setText(NumberUtil.getIOrD(currentNum) + listBean.getSaleUom());
                productCountSetter.setCount(listBean, currentNum);
                if (currentNum == 1) {//0变到1
                    viewHolder.mIvProductReduce.setVisibility(View.VISIBLE);
                    viewHolder.mTvProductCount.setVisibility(View.VISIBLE);
                    viewHolder.mIvProductAdd.setBackgroundResource(R.drawable.ic_order_btn_add_green_part);
                }
                ProductCountUpdateEvent productCountUpdateEvent = new ProductCountUpdateEvent(listBean, currentNum);
                productCountUpdateEvent.setException(ProductListAdapter.this);
                BoilerplateApplication.get(v.getContext()).getComponent().eventBus().post(productCountUpdateEvent);
            }
        });
        /**
         * 点击数量展示输入对话框
         */
        viewHolder.mTvProductCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double currentCount = productCountSetter.getCount(listBean);
//                new ProductValueDialog(viewHolder.itemView.getContext(), listBean.getName(), currentCount, productCountSetter.getRemark(listBean), new ProductValueDialog.IProductDialogCallback() {
//                    @Override
//                    public void onInputValue(double value, String remark) {
//
//                        productCountSetter.setCount(listBean, value);
//                        listBean.setRemark(remark);
//                        productCountSetter.setRemark(listBean);
//                        if (value == 0) {
//                            viewHolder.mIvProductReduce.setVisibility(View.INVISIBLE);
//                            viewHolder.mTvProductCount.setVisibility(View.INVISIBLE);
//                            viewHolder.mIvProductAdd.setBackgroundResource(R.drawable.order_btn_add_gray);
//                        } else {
//                            viewHolder.mIvProductReduce.setVisibility(View.VISIBLE);
//                            viewHolder.mTvProductCount.setVisibility(View.VISIBLE);
//                            viewHolder.mIvProductAdd.setBackgroundResource(R.drawable.ic_order_btn_add_green_part);
//                        }
//                        viewHolder.mTvProductCount.setText(NumberUtil.getIOrD(value) + listBean.getSaleUom());
//                        ProductCountUpdateEvent productCountUpdateEvent = new ProductCountUpdateEvent(listBean, (int) value);
//                        productCountUpdateEvent.setException(ProductListAdapter.this);
//                        EventBus.getDefault().post(productCountUpdateEvent);
//                    }
//                }).show();
            }
        });

        viewHolder.mTvProductName.setText(listBean.getName());
        viewHolder.mTvProductCode.setText(listBean.getDefaultCode());
        viewHolder.mTvProductContent.setText(listBean.getUnit());

        if (canSeePrice) {
            StringBuffer sb1 = new StringBuffer();
            sb1.append("¥").append(df.format(Double.valueOf(listBean.getPrice())));

            viewHolder.mTvProductPrice.setText(sb1.toString());
            viewHolder.mTvProductPriceUnit.setText("/" + listBean.getSaleUom());
        } else {
            viewHolder.mTvProductPrice.setVisibility(View.GONE);
            viewHolder.mTvProductPriceUnit.setVisibility(View.GONE);
        }
        if (listBean.getImage() != null) {
            FrecoFactory.getInstance(convertView.getContext()).disPlay(viewHolder.mSdvProductImage, listBean.getImage().getImageSmall());
        }

        viewHolder.mSdvProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProductImageDialog productImageDialog = new ProductImageDialog(viewHolder.itemView.getContext());
//                productImageDialog.setListBean(listBean);
//                productImageDialog.setProductCountSetter(productCountSetter);
//                productImageDialog.show();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_header)
        TextView mTvHeader;
        @BindView(R.id.stick_header)
        FrameLayout mStickHeader;
        @BindView(R.id.sdv_product_image)
        SimpleDraweeView mSdvProductImage;
        @BindView(R.id.tv_product_name)
        TextView mTvProductName;
        @BindView(R.id.tv_product_code)
        TextView mTvProductCode;
        @BindView(R.id.v_line)
        View mVLine;
        @BindView(R.id.tv_product_content)
        TextView mTvProductContent;
        @BindView(R.id.iv_product_sale)
        TextView mIvProductSale;
        @BindView(R.id.tv_product_price)
        TextView mTvProductPrice;
        @BindView(R.id.tv_product_price_unit)
        TextView mTvProductPriceUnit;
        @BindView(R.id.iv_product_reduce)
        ImageButton mIvProductReduce;
        @BindView(R.id.tv_product_count)
        TextView mTvProductCount;
        @BindView(R.id.iv_product_add)
        ImageButton mIvProductAdd;
        @BindView(R.id.food_main)
        LinearLayout mFoodMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
