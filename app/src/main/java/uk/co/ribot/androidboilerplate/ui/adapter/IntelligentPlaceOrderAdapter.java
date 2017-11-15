package uk.co.ribot.androidboilerplate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.DefaultProductResponse;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;
import uk.co.ribot.androidboilerplate.util.ToastUtil;

/**
 * Created by mike on 2017/11/14.
 */

public class IntelligentPlaceOrderAdapter extends BaseAdapter<IntelligentPlaceOrderAdapter.ViewHolder> {

    public enum SELECTTYPE {
        NO_SELECT, ALL_SELECT, PART_SELECT
    }

    @Inject
    public IntelligentPlaceOrderAdapter() {

    }

    private Context mContext;
    private boolean editMode;

    private boolean ischange;
    private HashMap<Integer, Integer> countMap = new HashMap<>();
    private List<DefaultProductResponse> selectArr = new ArrayList<>();

    public HashMap<Integer, Integer> getCountMap() {
        return countMap;
    }

    public List<DefaultProductResponse> getSelectArr() {
        return selectArr;
    }

    public void setSelectArr(List<DefaultProductResponse> selectArr) {
        this.selectArr = selectArr;
    }

    public interface OneKeyInterface {
        void countChanged();

        //选择的类型,0没选，1全选,2部分选
        void selectClicked(SELECTTYPE selectType);
    }

    private OneKeyInterface callback;

    public void setCallback(OneKeyInterface callback) {
        this.callback = callback;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        ArrayList<DefaultProductResponse> defaultPBeen = new ArrayList<>();
        if (!editMode) {
            for (Object o : mDefaultPBeans) {
                DefaultProductResponse defaultPBean = (DefaultProductResponse) o;
                Integer integer = countMap.get(defaultPBean.getProductID());
                if (integer != null && integer == 0) {
                    defaultPBeen.add(defaultPBean);
                }
            }
            for (DefaultProductResponse defaultPBean : defaultPBeen) {
                mDefaultPBeans.remove(defaultPBean);
            }
        }
    }

    List<DefaultProductResponse> mDefaultPBeans = new ArrayList<>();

    public List<DefaultProductResponse> getList(){
        return mDefaultPBeans;
    }

    public void setData(List list) {
        mDefaultPBeans.clear();
        mDefaultPBeans.addAll(list);
        for (Object bean : list) {
            if (!countMap.containsKey(((DefaultProductResponse) bean).getProductID())) {
                countMap.put(((DefaultProductResponse) bean).getProductID(), ((DefaultProductResponse) bean).getPresetQty());
            }
        }
        callback.countChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayout(parent.getContext(), R.layout.item_place_order_product);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setOnItemListener(holder.itemView, position);
        final DefaultProductResponse bean = (DefaultProductResponse) mDefaultPBeans.get(position);
        final EditText edittext = holder.mEt;
        holder.mEt.addTextChangedListener(new TextWatcher() {
            String mmPrevious;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mmPrevious = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!ischange) {
                    Integer num;
                    if (!TextUtils.isDigitsOnly(s)) {
                        edittext.setText(mmPrevious);
                        return;
                    }
                    if (TextUtils.isEmpty(s.toString())) {
                        return;
                    }
                    num = Integer.valueOf(s.toString());
                    countMap.put(bean.getProductID(), num);
//                notifyDataSetChanged();
                    callback.countChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.mEt.setTag(position);
        if (editMode) {
            holder.mCb.setVisibility(View.VISIBLE);
            holder.mLlEdit.setVisibility(View.VISIBLE);
            holder.mTvCount.setVisibility(View.INVISIBLE);
            holder.mTvUnit1.setVisibility(View.INVISIBLE);
        } else {
            holder.mLlEdit.setVisibility(View.INVISIBLE);
            holder.mCb.setVisibility(View.GONE);
            holder.mTvCount.setVisibility(View.VISIBLE);
            holder.mTvUnit1.setVisibility(View.VISIBLE);
        }
        final EditText editText = holder.mEt;
        final TextView mTvCount = holder.mTvCount;
//        ProductBasicList.ListBean basicBean = ProductBasicUtils.getBasicMap(mContext).get(String.valueOf(bean.getProductID()));
//        if (basicBean != null) {
//            viewHolder.nameTv.setText(basicBean.getName());
//            if (basicBean.getImage() != null)
//                FrecoFactory.getInstance(mContext).disPlay(holder.sdv, Constant.BASE_URL + basicBean.getImage().getImageSmall());
//            StringBuffer sb = new StringBuffer(basicBean.getDefaultCode());
//            sb.append(" | ").append(basicBean.getUnit());
//            boolean canSeePrice = GlobalApplication.getInstance().getCanSeePrice();
//            DecimalFormat df = new DecimalFormat("#.##");
//            if (canSeePrice) {
//                sb.append("  ").append(df.format(basicBean.getPrice())).append("元/").append(basicBean.getUom());
//            }
//            holder.mTvCount.setText(sb.toString());
//            holder.mTvUnit1.setText(basicBean.getUom());
//        }
        Integer proId = bean.getProductID();
        String count = String.valueOf(countMap.get(proId));
        ischange = true;
        editText.setText(count);
        mTvCount.setText("x" + count);
        ischange = false;
        holder.mIbInputMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer pId = bean.getProductID();
                int count = countMap.get(pId);
                if (count > 0) {
                    ischange = true;
                    editText.setText(String.valueOf(--count));
                    mTvCount.setText("x" + String.valueOf(count));
                    ischange = false;
                    countMap.put(pId, count);
                } else {
//                    countMap.remove(pId);
//                    mList.remove(bean);
//                    notifyDataSetChanged();
//                    //不管这些包含在selcetArr中，都要刷新全选
//                    if (selectArr.contains(bean)){
//                        selectArr.remove(bean);
//                    }
//                    checkSelectState();
                }
                //发送事件
                callback.countChanged();
            }
        });
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Integer pId = bean.getProductID();
                int count = countMap.get(pId);
                if (count >= 9999) {
                    ToastUtil.show(mContext, "最大只支持到9999");
                } else {
                    ischange = true;
                    editText.setText(String.valueOf(++count));
                    mTvCount.setText("x" + String.valueOf(count));
                    ischange = false;
                    countMap.put(pId, Integer.valueOf(count));
                }
                callback.countChanged();
            }
        });
        holder.mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selectArr.contains(bean)) {
                        selectArr.add(bean);
                    }
                } else {
                    selectArr.remove(bean);
                }
                checkSelectState();
            }
        });
        if (selectArr.contains(bean)) {
            holder.mCb.setChecked(true);
        } else {
            holder.mCb.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDefaultPBeans.size();
    }

    private void checkSelectState() {
        //返回是否全选标记,true全选, false一个没选
        if (selectArr.size() == mDefaultPBeans.size() && selectArr.size() != 0) {
            callback.selectClicked(SELECTTYPE.ALL_SELECT);
        } else if (selectArr.size() == 0) {
            callback.selectClicked(SELECTTYPE.NO_SELECT);
        } else {
            callback.selectClicked(SELECTTYPE.PART_SELECT);
        }
    }

    public void setAllSelect(boolean isAll) {
        if (isAll) {
            selectArr.clear();
            selectArr.addAll(mDefaultPBeans);
        } else {
            selectArr.clear();
        }
        notifyDataSetChanged();
    }

    public void clearSelect() {
        selectArr.clear();
        notifyDataSetChanged();
    }

    public void deleteSelectItems() {
        //TODO:同时如果计数里面有值，也得一并清掉
        mDefaultPBeans.removeAll(selectArr);
        selectArr.clear();
        if (mDefaultPBeans.isEmpty()) {
            callback.selectClicked(SELECTTYPE.NO_SELECT);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb)
        android.widget.CheckBox mCb;
        @BindView(R.id.sdv_product)
        SimpleDraweeView mSdvProduct;
        @BindView(R.id.btn_add)
        ImageButton mBtnAdd;
        @BindView(R.id.ib_input_minus)
        ImageButton mIbInputMinus;
        @BindView(R.id.et)
        android.widget.EditText mEt;
        @BindView(R.id.ib_input_add)
        ImageButton mIbInputAdd;
        @BindView(R.id.ll_edit)
        LinearLayout mLlEdit;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_unit1)
        TextView mTvUnit1;
        @BindView(R.id.tv_count)
        TextView mTvCount;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
