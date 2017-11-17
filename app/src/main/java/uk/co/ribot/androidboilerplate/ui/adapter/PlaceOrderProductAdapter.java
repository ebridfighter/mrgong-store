package uk.co.ribot.androidboilerplate.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.runwise.commomlibrary.view.NoWatchEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.AddedProduct;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.remote.RunwiseService;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2017/11/16.
 */

public class PlaceOrderProductAdapter extends BaseAdapter<PlaceOrderProductAdapter.ViewHolder> {
    private boolean mCanSeePrice;
    private Activity mActivity;
    List<ProductListResponse.Product> mProducts = new ArrayList<>();
    private HashMap<String, AddedProduct> mCountMap;

    public void setProducts(List<ProductListResponse.Product> products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    public void setCanSeePrice(boolean canSeePrice) {
        this.mCanSeePrice = canSeePrice;
    }

    public void setCountMap(HashMap<String, AddedProduct> countMap) {
        mCountMap = countMap;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Inject
    public PlaceOrderProductAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(getLayout(parent.getContext(), R.layout.item_product_add));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductListResponse.Product bean = (ProductListResponse.Product) mProducts.get(position);
        holder.mNwet.setTag(position);
        holder.mNwet.removeTextChangedListener();
        holder.mNwet.addTextChangedListener(new TextWatcher() {
            String mmStrPrevious;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mmStrPrevious = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //检查特殊字符
                if (!TextUtils.isDigitsOnly(s)) {
                    holder.mNwet.setText(mmStrPrevious);
                    return;
                }
                int position = (int) holder.mNwet.getTag();
                ProductListResponse.Product listBean = mProducts.get(position);
                int changedNum = 0;
                if (!TextUtils.isEmpty(s)) {
                    changedNum = Integer.valueOf(s.toString());
                }
                AddedProduct addedProduct = new AddedProduct();
                addedProduct.setCount(changedNum);
                addedProduct.setProduct(listBean);
                mCountMap.put(String.valueOf(listBean.getProductID()), addedProduct);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //先根据集合里面对应个数初始化一次
        if (mCountMap.get(String.valueOf(bean.getProductID())).getCount() > 0) {
            holder.mLlEdit.setVisibility(View.VISIBLE);
            holder.mIbAdd.setVisibility(View.INVISIBLE);
            holder.mTvUnit1.setVisibility(View.INVISIBLE);
        } else {
            holder.mLlEdit.setVisibility(View.INVISIBLE);
            holder.mIbAdd.setVisibility(View.VISIBLE);
            holder.mTvUnit1.setVisibility(View.VISIBLE);
        }
        final NoWatchEditText mNwet = holder.mNwet;
        mNwet.setText(mCountMap.get(String.valueOf(bean.getProductID())).getCount() + "");
        final LinearLayout ll = holder.mLlEdit;
        final ImageButton mIbAdd = holder.mIbAdd;
        final TextView unit1 = holder.mTvUnit1;
        mIbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus(mActivity);//点击加减的时候，去掉所有edittext的focus，关闭软键盘
                view.setVisibility(View.INVISIBLE);
                unit1.setVisibility(View.INVISIBLE);
                ll.setVisibility(View.VISIBLE);
               AddedProduct addedProduct = mCountMap.get(String.valueOf(bean.getProductID()));
                int currentNum = addedProduct.getCount();
                mNwet.setText(++currentNum + "");
                addedProduct.setCount(currentNum);
                mCountMap.put(String.valueOf(bean.getProductID()), addedProduct);
            }
        });
        holder.mIbMinus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearFocus(mActivity);//点击加减的时候，去掉所有edittext的focus，关闭软键盘
                AddedProduct addedProduct = mCountMap.get(String.valueOf(bean.getProductID()));
                int currentNum = addedProduct.getCount();
                if (currentNum > 0) {
                    mNwet.setText(--currentNum + "");
                    addedProduct.setCount(currentNum);
                    mCountMap.put(String.valueOf(bean.getProductID()), addedProduct);
                    if (currentNum == 0) {
                        mIbAdd.setVisibility(View.VISIBLE);
                        unit1.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
        holder.mIbAdd1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearFocus(mActivity);//点击加减的时候，去掉所有edittext的focus，关闭软键盘
                AddedProduct addedProduct = mCountMap.get(String.valueOf(bean.getProductID()));
                int currentNum = addedProduct.getCount();
                mNwet.setText(++currentNum + "");
                addedProduct.setCount(currentNum);
                mCountMap.put(String.valueOf(bean.getProductID()), addedProduct);
            }
        });

        holder.mNwet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String str = holder.mNwet.getText().toString();
                    if (TextUtils.isEmpty(str) || Integer.valueOf(str) == 0) {//输入0或者空的时候，点完成变回初始样式
                        mIbAdd.setVisibility(View.VISIBLE);
                        unit1.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.INVISIBLE);
                    }
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        holder.mTvName.setText(bean.getName());

        StringBuffer sb = new StringBuffer(bean.getDefaultCode());
        sb.append(" | ").append(bean.getUnit());
        holder.mTvContent.setText(sb.toString());
        DecimalFormat df = new DecimalFormat("#.##");
        if (mCanSeePrice) {
            StringBuffer sb1 = new StringBuffer();
            if (bean.isTwoUnit()) {
                sb1.append("¥")
                        .append(df.format(Double.valueOf(bean.getSettlePrice())))
                        .append("元/")
                        .append(bean.getSettleUomId());
            } else {
                sb1.append("¥")
                        .append(df.format(Double.valueOf(bean.getPrice())))
                        .append("元/")
                        .append(bean.getUom());
            }
            holder.mTvPrice.setText(sb1.toString());
        } else {
            holder.mTvPrice.setText("");
        }
        if (bean.getImage() != null){
            FrecoFactory.getInstance(holder.itemView.getContext()).disPlay(holder.mSdvProduct, RunwiseService.ENDPOINT + bean.getImage().getImageSmall());
        }
        holder.mTvUnit1.setText(bean.getUom());
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sdv_product)
        SimpleDraweeView mSdvProduct;
        @BindView(R.id.tv_unit1)
        TextView mTvUnit1;
        @BindView(R.id.ib_add)
        ImageButton mIbAdd;
        @BindView(R.id.ib_minus)
        ImageButton mIbMinus;
        @BindView(R.id.nwet)
        NoWatchEditText mNwet;
        @BindView(R.id.ib_add1)
        ImageButton mIbAdd1;
        @BindView(R.id.ll_edit)
        LinearLayout mLlEdit;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_price)
        TextView mTvPrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    private void clearFocus(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            v.clearFocus();
        }
    }
}
