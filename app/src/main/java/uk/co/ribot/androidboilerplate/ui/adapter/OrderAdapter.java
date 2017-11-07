package uk.co.ribot.androidboilerplate.ui.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runwise.commomlibrary.util.NumberUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderDoAction;
import uk.co.ribot.androidboilerplate.data.model.business.OrderListWrap;
import uk.co.ribot.androidboilerplate.data.model.business.OrderState;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.util.OrderActionUtils;

import static uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse.ListBean.TYPE_THIRD_PART_DELIVERY;
import static uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse.ListBean.TYPE_VENDOR_DELIVERY;

/**
 * Created by mike on 2017/11/1.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderListWrap> mOrderListWraps;
    private List<OrderListResponse.ListBean> mOrders;
    private List<ReturnOrderListResponse.ListBean> mReturnOrders;
    //记录当前扩展打开的状态
    private HashMap<Integer, Boolean> mExpandMap = new HashMap<>();

    public static final int VIEW_TYPE_ORDER = 1 << 0;
    public static final int VIEW_TYPE_RETURN_ORDER = 1 << 1;

    public interface DoActionInterface {
        void doAction(OrderDoAction action, int postion);

        void call(String phone);
    }

    private DoActionInterface callback;


    @Inject
    public OrderAdapter() {
        mOrderListWraps = new ArrayList<>();
    }

    public void setOrders(List<OrderListResponse.ListBean> orders) {
        mOrders = orders;
        for (OrderListResponse.ListBean listBean : orders) {
            OrderListWrap orderListWrap = new OrderListWrap();
            orderListWrap.setOrderListBean(listBean);
            mOrderListWraps.add(orderListWrap);
        }
    }

    public void setReturnOrders(List<ReturnOrderListResponse.ListBean> returnOrders) {
        mReturnOrders = returnOrders;
        for (int i = 0; i < returnOrders.size(); i++) {
            ReturnOrderListResponse.ListBean listBean = returnOrders.get(i);
            OrderListWrap orderListWrap = new OrderListWrap();
            orderListWrap.setReturnOrderListBean(listBean);
            mOrderListWraps.add(i, orderListWrap);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mOrderListWraps.get(position).getOrderListBean() != null ? VIEW_TYPE_ORDER : VIEW_TYPE_RETURN_ORDER;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_ORDER) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order_homepage, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order_homepage, parent, false);
        }
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_ORDER) {
            OrderListResponse.ListBean order = mOrderListWraps.get(position).getOrderListBean();
            holder.mTvOrderNum.setText(order.getName());
        } else {
            ReturnOrderListResponse.ListBean returnOrder = mOrderListWraps.get(position).getReturnOrderListBean();
            holder.mTvOrderNum.setText("退：" + returnOrder.getName());
        }
        if (getItemViewType(position) == VIEW_TYPE_ORDER) {
            final OrderListResponse.ListBean bean = mOrderListWraps.get(position).getOrderListBean();
            holder.mIbArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isExpand;
                    //更改boolean状态
                    if (mExpandMap.get(Integer.valueOf(bean.getOrderID())) != null) {
                        isExpand = mExpandMap.get(Integer.valueOf(bean.getOrderID())).booleanValue();
                        isExpand = !isExpand;
                    } else {
                        isExpand = true;
                    }
                    mExpandMap.put(Integer.valueOf(bean.getOrderID()), isExpand);
                    if (isExpand) {
                        //只有点击时，才去放timeline的内容
                        setTimeLineContent(v.getContext(), bean.getStateTracker(), holder.mRecyclerView);
                        holder.mTimelineLL.setVisibility(View.VISIBLE);
                        holder.mIbArrow.setImageResource(R.drawable.login_btn_dropup);
                    } else {
                        holder.mTimelineLL.setVisibility(View.GONE);
                        holder.mIbArrow.setImageResource(R.drawable.login_btn_dropdown);
                    }
                }
            });
            holder.mBtnDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(!SystemUpgradeHelper.getInstance(context).check(context))return;
                    //根据状态进行不同的逻辑处理
                    String doAction = ((TextView) v).getText().toString();
                    OrderDoAction action = OrderActionUtils.getDoActionByText(doAction, bean);
                    if (callback != null) {
                        callback.doAction(action, position);
                    }
                }
            });
            if (mExpandMap.get(Integer.valueOf(bean.getOrderID())) != null && mExpandMap.get(Integer.valueOf(bean.getOrderID())).booleanValue()) {
                holder.mTimelineLL.setVisibility(View.VISIBLE);
                //重刷一次，免得重复
                holder.mIbArrow.setImageResource(R.drawable.login_btn_dropup);
                setTimeLineContent(holder.mIbArrow.getContext(), bean.getStateTracker(), holder.mRecyclerView);
            } else {
                holder.mTimelineLL.setVisibility(View.GONE);
                holder.mIbArrow.setImageResource(R.drawable.login_btn_dropdown);
            }
            //派单前，派单后，用户收货后
            StringBuffer etSb = new StringBuffer();
            if (bean.getState().equals(OrderState.DRAFT.getName()) || bean.getState().equals(OrderState.SALE.getName())) {
                etSb.append("预计").append(formatTimeStr(bean.getEstimatedDate())).append("送达");
            } else if (bean.getState().equals(OrderState.PEISONG.getName())) {
                etSb.append("预计").append(formatTimeStr(bean.getEstimatedDate())).append("送达");
            } else {
                etSb.append(bean.getDoneDatetime()).append("已送达");
            }
            holder.mTvOrderTime.setText(bean.getName());
            holder.mTvOrderNum.setText(etSb.toString());
            holder.mTvState.setText(OrderState.getValueByName(bean.getState()));
            holder.mTvState.setTextColor(Color.parseColor("#333333"));
            if (bean.getWaybill() != null && bean.getWaybill() != null && bean.getWaybill().getDeliverVehicle() != null) {
                holder.mTvCarNum.setText(bean.getWaybill().getDeliverVehicle().getLicensePlate());
                holder.mDriverLL.setVisibility(View.VISIBLE);
            } else {
                holder.mTvCarNum.setText("未指派");
                holder.mDriverLL.setVisibility(View.GONE);
            }
            if (bean.getWaybill() != null && bean.getWaybill().getDeliverUser() != null) {
                holder.mSenderTv.setText(bean.getWaybill().getDeliverUser().getName());
                holder.mSenderTv.setVisibility(View.VISIBLE);
            } else {
                holder.mSenderTv.setText("未指派");
                holder.mSenderTv.setVisibility(View.GONE);
            }
            holder.mCallIb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean != null && bean.getWaybill() != null && bean.getWaybill().getDeliverUser() != null
                            && bean.getWaybill().getDeliverUser().getMobile() != null) {
                        callback.call(bean.getWaybill().getDeliverUser().getMobile());
                    } else {
                        callback.call(null);
                    }

                }
            });
            StringBuffer sb = new StringBuffer("共");
            if ("done".equals(bean.getState()) && bean.getDeliveredQty() != bean.getAmount()) {
                sb.append((int) bean.getDeliveredQty()).append("件商品");
            } else {
                sb.append((int) bean.getAmount()).append("件商品");
            }
            holder.mTvCount.setText(sb.toString());
            holder.mTvMoney.setText(NumberUtil.getIOrD(bean.getAmountTotal()));
            StringBuffer drawableSb = new StringBuffer("state_restaurant_");
            drawableSb.append(bean.getState());
            if (getResIdByDrawableName(holder.mImg.getContext(),drawableSb.toString()) == 0) {
                holder.mImg.setImageResource(R.drawable.state_restaurant_draft);
            } else {
                holder.mImg.setImageResource(getResIdByDrawableName(holder.mImg.getContext(),drawableSb.toString()));
            }
            String doString = OrderActionUtils.getDoBtnTextByState(bean);
            if (!TextUtils.isEmpty(doString)) {
                if (doString.equals("已评价")) {
                    holder.mBtnDo.setVisibility(View.INVISIBLE);
                } else {
                    holder.mBtnDo.setVisibility(View.VISIBLE);
                    holder.mBtnDo.setText(doString);
                }
            } else {
                holder.mBtnDo.setVisibility(View.INVISIBLE);
            }
            if (bean.getHasReturn() != 0) {
                holder.mTvReturn.setVisibility(View.VISIBLE);
            } else {
                holder.mTvReturn.setVisibility(View.GONE);
            }
            if ("done".equals(bean.getState()) && bean.getDeliveredQty() != bean.getAmount()) {
                holder.mTvReal.setVisibility(View.VISIBLE);
            } else {
                holder.mTvReal.setVisibility(View.GONE);
            }
            if (bean.getHasAttachment() == 0 && bean.getOrderSettleName().contains("单次结算") && bean.getOrderSettleName().contains("先付款后收货") && bean.getState().equals(OrderState.DRAFT.getName())) {
                holder.mTvToPay.setVisibility(View.VISIBLE);
            } else {
                holder.mTvToPay.setVisibility(View.GONE);
            }
        } else {
            final ReturnOrderListResponse.ListBean bean = mOrderListWraps.get(position).getReturnOrderListBean();
            //发货单
            holder.mTvReturn.setVisibility(View.GONE);
            holder.mTvReal.setVisibility(View.GONE);
            holder.mTvToPay.setVisibility(View.GONE);
            holder.mImg.setImageResource(R.drawable.more_restaurant_returnrecord);
            holder.mTvOrderTime.setText(bean.getName());
            holder.mTvState.setText("退货中");
            holder.mTvState.setTextColor(Color.parseColor("#FA694D"));
            holder.mTvOrderNum.setText(TimeUtils.getMMddHHmm(bean.getCreateDate()));
            StringBuffer sb = new StringBuffer("共");
            sb.append((int) bean.getAmount()).append("件商品");
            holder.mTvCount.setText(sb.toString());
            holder.mTvMoney.setText(NumberUtil.getIOrD(bean.getAmountTotal()));
            holder.mBtnDo.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(bean.getDriveMobile())) {
                holder.mTvCarNum.setText(bean.getVehicle());
                holder.mTvCarNum.setVisibility(View.VISIBLE);
                holder.mCallIb.setVisibility(View.VISIBLE);
                holder.mDriverLL.setVisibility(View.VISIBLE);
            } else {
                holder.mTvCarNum.setText("未指派");
                holder.mTvCarNum.setVisibility(View.GONE);
                holder.mCallIb.setVisibility(View.GONE);
                holder.mDriverLL.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(bean.getDriver())) {
                holder.mSenderTv.setText(bean.getDriver());
            } else {
                holder.mSenderTv.setText("未指派");
            }
            holder.mCallIb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(bean.getDriveMobile())) {
                        callback.call(bean.getDriveMobile());
                    } else {
                        callback.call(null);
                    }
                }
            });
            holder.mIbArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更改boolean状态
                    Boolean isExpand = mExpandMap.get(Integer.valueOf(bean.getOrderID()));
                    if (isExpand != null) {
                        isExpand = !isExpand;
                    } else {
                        isExpand = true;
                    }
                    mExpandMap.put(Integer.valueOf(bean.getOrderID()), isExpand);
                    if (isExpand) {
                        //只有点击时，才去放timeline的内容
                        setTimeLineContent(v.getContext(),bean.getStateTracker(), holder.mRecyclerView);
                        holder.mTimelineLL.setVisibility(View.VISIBLE);
                        holder.mIbArrow.setImageResource(R.drawable.login_btn_dropup);
                    } else {
                        holder.mTimelineLL.setVisibility(View.GONE);
                        holder.mIbArrow.setImageResource(R.drawable.login_btn_dropdown);
                    }

                }
            });
            if (mExpandMap.get(Integer.valueOf(bean.getOrderID())) != null && mExpandMap.get(Integer.valueOf(bean.getOrderID())).booleanValue()) {
                holder.mTimelineLL.setVisibility(View.VISIBLE);
                //重刷一次，免得重复
                holder.mIbArrow.setImageResource(R.drawable.login_btn_dropup);
                setTimeLineContent(holder.mIbArrow.getContext(),bean.getStateTracker(), holder.mRecyclerView);
            } else {
                holder.mTimelineLL.setVisibility(View.GONE);
                holder.mIbArrow.setImageResource(R.drawable.login_btn_dropdown);
            }
            String deliveryType = bean.getDeliveryType();
            if (deliveryType.equals(OrderListResponse.ListBean.TYPE_FRESH_VENDOR_DELIVERY) ||
                    deliveryType.equals(TYPE_VENDOR_DELIVERY)
                    || ((deliveryType.equals(TYPE_THIRD_PART_DELIVERY) || deliveryType.equals(TYPE_THIRD_PART_DELIVERY))
                    && bean.isReturnThirdPartLog())
                    ) {
                holder.mBtnDo.setVisibility(View.VISIBLE);
                holder.mBtnDo.setText("完成退货");
                holder.mBtnDo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //根据状态进行不同的逻辑处理
                        if (callback != null) {
                            callback.doAction(OrderDoAction.FINISH_RETURN, position);
                        }

                    }
                });
            } else {
                holder.mBtnDo.setVisibility(View.INVISIBLE);
            }

        }

    }

    private void setTimeLineContent(Context context, List<String> stList, RecyclerView recyclerView) {
        TimeLineAdapter adapter = new TimeLineAdapter(context, stList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mOrderListWraps.size();
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
    private int getResIdByDrawableName(Context context,String name) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
        return resID;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img)
        ImageView mImg;
        @BindView(R.id.tv_order_num)
        TextView mTvOrderNum;
        @BindView(R.id.tv_real)
        TextView mTvReal;
        @BindView(R.id.tv_return)
        TextView mTvReturn;
        @BindView(R.id.tv_to_pay)
        TextView mTvToPay;
        @BindView(R.id.ll_order_num)
        LinearLayout mLlOrderNum;
        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.tv_order_time)
        TextView mTvOrderTime;
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.iv_icon2)
        ImageView mIvIcon2;
        @BindView(R.id.tv_money)
        TextView mTvMoney;
        @BindView(R.id.ll_price)
        LinearLayout mLlPrice;
        @BindView(R.id.ll1)
        LinearLayout mLl1;
        @BindView(R.id.v_line)
        View mVLine;
        @BindView(R.id.btn_do)
        TextView mBtnDo;
        @BindView(R.id.carNumTv)
        TextView mTvCarNum;
        @BindView(R.id.senderTv)
        TextView mSenderTv;
        @BindView(R.id.callIb)
        ImageButton mCallIb;
        @BindView(R.id.driverLL)
        LinearLayout mDriverLL;
        @BindView(R.id.recyclerView)
        RecyclerView mRecyclerView;
        @BindView(R.id.timelineLL)
        LinearLayout mTimelineLL;
        @BindView(R.id.ib_arrow)
        ImageButton mIbArrow;
        @BindView(R.id.ll_arrow_area)
        LinearLayout mLlArrowArea;

        OrderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
