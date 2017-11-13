package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.runwise.commomlibrary.util.DensityUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.chenupt.dragtoplayout.DragTopLayout;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderState;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderDetailResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderDetailResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.OrderProductFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.OrderDetailActivityPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderDetailMvpView;
import uk.co.ribot.androidboilerplate.util.CommonUtils;
import uk.co.ribot.androidboilerplate.util.OrderActionUtils;
import uk.co.ribot.androidboilerplate.view.ProductTypePopup;

public class OrderDetailActivity extends BaseActivity implements OrderDetailMvpView {

    public static final String INTENT_KEY_ORDER_ID = "intent_key_order_id";
    private static final int TAB_EXPAND_COUNT = 4;
    @Inject
    OrderDetailActivityPresenter mOrderDetailActivityPresenter;

    List<Fragment> mFragmentList;
    List<String> mTitleList;
    int mOrderId;

    int mFinishFlag = 0;
    final int FINISH_COUNT = 2;
    @BindView(R.id.tv_order_state)
    TextView mTvOrderState;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.btn_state)
    TextView mBtnState;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_receive)
    TextView mTvReceive;
    @BindView(R.id.tv_return)
    TextView mTvReturn;
    @BindView(R.id.ll_order)
    LinearLayout mLlOrder;
    @BindView(R.id.tv_buyer)
    TextView mTvBuyer;
    @BindView(R.id.tv_buy_time)
    TextView mTvBuyTime;
    @BindView(R.id.tv_pay_state)
    TextView mTvPayState;
    @BindView(R.id.tv_buyer_value)
    TextView mTvBuyerValue;
    @BindView(R.id.tv_order_time_value)
    TextView mTvOrderTimeValue;
    @BindView(R.id.tv_pay_state_value)
    TextView mTvPayStateValue;
    @BindView(R.id.btn_upload)
    TextView mBtnUpload;
    @BindView(R.id.ll_return)
    LinearLayout mLlReturn;
    @BindView(R.id.ll_return_parent)
    LinearLayout mLLReturnParent;
    @BindView(R.id.tl_category)
    TabLayout mTlCategory;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.btn_right)
    Button mBtnRight;
    @BindView(R.id.btn_right2)
    Button mBtnRight2;
    @BindView(R.id.rl_bottom)
    RelativeLayout mRlBottom;
    @BindView(R.id.v_space)
    View mVSpace;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_count_value)
    TextView mTvCountValue;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_money_value)
    TextView mTvMoneyValue;
    @BindView(R.id.ll_price)
    LinearLayout mLlPrice;
    @BindView(R.id.top_view)
    LinearLayout mTopView;
    @BindView(R.id.iv_open)
    ImageView mIvOpen;
    @BindView(R.id.drag_content_view)
    LinearLayout mDragContentView;
    @BindView(R.id.drag_layout)
    DragTopLayout mDragLayout;
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;

    private boolean mHasAttachment;        //默认无凭证
    private boolean mModifyOrder;
    private List<OrderListResponse.ListBean.LinesBean> mLineBeans;


    public static Intent getStartIntent(Context context, int orderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(INTENT_KEY_ORDER_ID, orderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        configPersistentComponent.orderDetailActivityComponent(new ActivityModule(this)).inject(this);
        mOrderDetailActivityPresenter.attachView(this);

        mOrderId = getIntent().getIntExtra(INTENT_KEY_ORDER_ID, 0);
        if (mOrderId == 0) {
            finish();
            toast(R.string.no_exist_order);
        }
        mOrderDetailActivityPresenter.getCategorys();
        mOrderDetailActivityPresenter.getOrderDetail(mOrderId);
        mDragLayout.setOverDrag(false);
        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击空白不收起draglayout
            }
        });
    }

    CategoryResponse mCategoryResponse;

    @Override
    public void showCategorys(CategoryResponse categoryResponse) {
        mCategoryResponse = categoryResponse;
        mFinishFlag++;
        if (mFinishFlag == FINISH_COUNT) {
            updateUI();
        }
    }

    OrderDetailResponse mOrderDetailResponse;

    @Override
    public void showOrder(OrderDetailResponse orderDetailResponse) {
        mOrderDetailResponse = orderDetailResponse;
        mFinishFlag++;
        if (mFinishFlag == FINISH_COUNT) {
            updateUI();
        }
    }

    @Override
    public void showReturnOrder(ReturnOrderDetailResponse returnOrderDetailResponse) {
        setUpReturnOrderView(returnOrderDetailResponse);
    }

    private void setUpReturnOrderView(ReturnOrderDetailResponse returnOrderDetailResponse) {
        TextView tv = new TextView(getActivityContext());
        tv.setTextSize(14);
        tv.setTextColor(Color.parseColor("#999999"));
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTag(returnOrderDetailResponse.getReturnOrder().getReturnOrderID());
        if (!TextUtils.isEmpty(returnOrderDetailResponse.getReturnOrder().getName())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    CommonUtils.dip2px(getActivityContext(), 40));
            SpannableString ss = new SpannableString("退货单号：" + returnOrderDetailResponse.getReturnOrder().getName());
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#2F96D8")), 5, 5 + returnOrderDetailResponse.getReturnOrder().getName().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(ss);
            mLlReturn.addView(tv, params);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到退货单详情
//                    Intent intent = new Intent(mContext, ReturnDetailActivity.class);
//                    intent.putExtra("rid", returnId);
//                    startActivity(intent);
                }
            });
        }
    }

    private void updateUI() {
        if (mOrderDetailResponse != null) {
            if (mOrderDetailResponse.getOrder().getHasReturn() != 0) {
                mLLReturnParent.setVisibility(View.VISIBLE);
                //可能有多个退货单。
                if (mOrderDetailResponse.getOrder().getReturnOrders().size() > 0) {
                    String returnId = mOrderDetailResponse.getOrder().getReturnOrders().get(0);
                    mOrderDetailActivityPresenter.getReturnOrder(Integer.parseInt(returnId));
                }
            }
            String state = "";
            String tip = "";
            if (mOrderDetailResponse.getOrder().getState().equals(OrderState.DRAFT.name())) {
                state = getString(R.string.order_submited);
                tip = getString(R.string.tag_order_num) + mOrderDetailResponse.getOrder().getName();
            } else if (mOrderDetailResponse.getOrder().getState().equals(OrderState.SALE.name())) {
                state = getString(R.string.order_confirmed);
                tip = getString(R.string.select_product_for_you);
                ViewGroup.LayoutParams lp = mRlBottom.getLayoutParams();
                lp.height = 0;
                mRlBottom.setLayoutParams(lp);
            } else if (mOrderDetailResponse.getOrder().getState().equals(OrderState.PEISONG.name())) {
                state = "订单已发货";
                tip = "预计发达时间：" + mOrderDetailResponse.getOrder().getEstimatedTime();
            } else if (mOrderDetailResponse.getOrder().getState().equals(OrderState.DONE.name())) {
                state = "订单已收货";
                String recdiveName = mOrderDetailResponse.getOrder().getReceiveUserName();
//                tip = "收货人："+ recdiveName;
                tip = "配送已完成，如有问题请联系客服";
                //TODO:退货单没有收货人姓名，暂时处理
                if (TextUtils.isEmpty(recdiveName)) {
                    tip = "已退货";
                    state = "订单已退货";
                }
                if (!TextUtils.isEmpty(mOrderDetailResponse.getOrder().getAppraisalUserName())) {
                    //已评价
                    ViewGroup.LayoutParams lp = mRlBottom.getLayoutParams();
                    lp.height = 0;
                    mRlBottom.setLayoutParams(lp);
                }
                //预计价钱改为，商品金额
                mTvMoney.setText("商品金额");
            } else if (mOrderDetailResponse.getOrder().getState().equals(OrderState.RATED.name())) {
                state = "订单已评价";
                ViewGroup.LayoutParams lp = mRlBottom.getLayoutParams();
                lp.height = 0;
                mRlBottom.setLayoutParams(lp);
                tip = "感谢您的评价，供鲜生祝您生活愉快！";
            } else if ("cancel".equals(mOrderDetailResponse.getOrder().getState())) {
                state = "订单已取消";
                tip = "您的订单已取消成功";
            }
            mTvOrderState.setText(state);
            mTvTip.setText(tip);
            mBtnRight.setText(OrderActionUtils.getDoBtnTextByState(mOrderDetailResponse.getOrder()));
            mTvDate.setText(TimeUtils.getMMdd(mOrderDetailResponse.getOrder().getCreateDate()));

            if (mOrderDetailResponse.getOrder().getOrderSettleName().contains("先付款后收货") && mOrderDetailResponse.getOrder().getOrderSettleName().contains("单次结算")) {
                setUpPaymentInstrument();
            }

            //支付凭证在收货流程后，才显示
            if ((mOrderDetailResponse.getOrder().getState().equals("rated") || mOrderDetailResponse.getOrder().getState().equals("done"))
                    && mOrderDetailResponse.getOrder().getOrderSettleName().contains("单次结算")) {
                setUpPaymentInstrument();
            }
            if (mOrderDetailResponse.getOrder().getState().equals(OrderState.DRAFT.getName())) {
//                setTitleRightText(true, "修改");
                mModifyOrder = true;
            } else if (!mOrderDetailResponse.getOrder().isUnApplyService() && (mOrderDetailResponse.getOrder().getState().equals("rated") || mOrderDetailResponse.getOrder().getState().equals("done"))) {
                //同时，显示右上角，申请售后
//                setTitleRightText(true, "申请售后");
            } else {
//                setTitleRightText(false, "");
            }
            //订单信息
            mTvOrderNum.setText(mOrderDetailResponse.getOrder().getName());
            mTvBuyerValue.setText(mOrderDetailResponse.getOrder().getCreateUserName());
            mTvOrderTimeValue.setText(mOrderDetailResponse.getOrder().getCreateDate());
            if (mOrderDetailResponse.getOrder().getHasReturn() == 0) {
                mTvReturn.setVisibility(View.INVISIBLE);
            } else {
                mTvReturn.setVisibility(View.VISIBLE);
            }
            //实收判断
            if ((OrderState.DONE.getName().equals(mOrderDetailResponse.getOrder().getState()) || OrderState.RATED.getName().equals(mOrderDetailResponse.getOrder().getState())) && isActualReceive()) {
                mTvReceive.setVisibility(View.VISIBLE);
                mTvCountValue.setText((int) mOrderDetailResponse.getOrder().getDeliveredQty() + "件");
            } else {
                mTvReceive.setVisibility(View.GONE);
                mTvCountValue.setText((int) mOrderDetailResponse.getOrder().getAmount() + "件");
            }
            if (!mOrderDetailActivityPresenter.isCanSeePrice()){
                mLlPrice.setVisibility(View.GONE);
            }else{
                mLlPrice.setVisibility(View.VISIBLE);
            }
            //商品数量/预估金额
            DecimalFormat df = new DecimalFormat("#.##");
            mTvMoneyValue.setText("¥" + df.format(mOrderDetailResponse.getOrder().getAmountTotal()));
            mLineBeans = mOrderDetailResponse.getOrder().getLines();
            setUpDataForViewPage();
        }
    }

    private void setUpDataForViewPage() {
        List<Fragment> orderProductFragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        HashMap<String, ArrayList<OrderListResponse.ListBean.LinesBean>> map = new HashMap<>();
        titles.add("全部");
        for (String category : mCategoryResponse.getCategoryList()) {
            titles.add(category);
            map.put(category, new ArrayList<OrderListResponse.ListBean.LinesBean>());
        }
        for (OrderListResponse.ListBean.LinesBean linesBean : mLineBeans) {
            String category = linesBean.getCategory();
            if (!TextUtils.isEmpty(category)) {
                ArrayList<OrderListResponse.ListBean.LinesBean> linesBeen = map.get(category);
                linesBeen.add(linesBean);
            }
        }
        for (String category : mCategoryResponse.getCategoryList()) {
            ArrayList<OrderListResponse.ListBean.LinesBean> value = map.get(category);
            orderProductFragmentList.add(newOrderProductFragment(value));
        }
        orderProductFragmentList.add(0, newOrderProductFragment((ArrayList<OrderListResponse.ListBean.LinesBean>) mLineBeans));

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), titles, orderProductFragmentList);
        mVp.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        mTlCategory.setupWithViewPager(mVp);//将TabLayout和ViewPager关联起来
        mVp.setOffscreenPageLimit(titles.size());
        mVp.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
//                IntEvent intEvent = new IntEvent();
//                intEvent.setHeight(mVp.getHeight());
//                EventBus.getDefault().post(intEvent);
                mVp.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mTlCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mVp.setCurrentItem(position);
                mTypeWindow.dismiss();
                if(mDragLayout.getState()== DragTopLayout.PanelState.EXPANDED)mDragLayout.toggleTopView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (titles.size() <= TAB_EXPAND_COUNT) {
            mIvOpen.setVisibility(View.GONE);
            mTlCategory.setTabMode(TabLayout.MODE_FIXED);
        } else {
            mIvOpen.setVisibility(View.VISIBLE);
            mTlCategory.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        initPopWindow((ArrayList<String>) titles);
    }

    public OrderProductFragment newOrderProductFragment(ArrayList<OrderListResponse.ListBean.LinesBean> value) {
        OrderProductFragment orderProductFragment = new OrderProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderProductFragment.BUNDLE_KEY_LIST, value);
        bundle.putBoolean(OrderProductFragment.BUNDLE_KEY_CAN_SEE_PRICE, mOrderDetailActivityPresenter.isCanSeePrice());
        bundle.putSerializable(OrderProductFragment.BUNDLE_KEY_ORDER_DATA, mOrderDetailResponse.getOrder());
        orderProductFragment.setArguments(bundle);
        return orderProductFragment;
    }

    private void setUpPaymentInstrument() {
        mTvPayState.setVisibility(View.VISIBLE);
        mTvPayStateValue.setVisibility(View.VISIBLE);
        mBtnUpload.setVisibility(View.VISIBLE);
        if (mOrderDetailResponse.getOrder().getHasAttachment() == 0) {
            mTvPayStateValue.setText("未有支付凭证");
            if (!mOrderDetailResponse.getOrder().getState().equals(OrderState.DRAFT.getName()) && mOrderDetailResponse.getOrder().getOrderSettleName().contains("先付款后收货") && mOrderDetailResponse.getOrder().getOrderSettleName().contains("单次结算")) {
                mBtnUpload.setVisibility(View.INVISIBLE);
            } else {
                mBtnUpload.setText("上传凭证");
            }
            mHasAttachment = false;
        } else {
            mTvPayStateValue.setText("已上传支付凭证");
            mBtnUpload.setText("查看凭证");
            mBtnUpload.setVisibility(View.VISIBLE);
            mHasAttachment = true;
        }
        mModifyOrder = false;
    }

    /**
     * 是否实收
     *
     * @return
     */
    private boolean isActualReceive() {
        for (OrderListResponse.ListBean.LinesBean linesBean : mOrderDetailResponse.getOrder().getLines()) {
            if (linesBean.getDeliveredQty() != linesBean.getProductUomQty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showCategorysError(CategoryResponse categoryResponse) {

    }
    boolean mCanShow = false;
    @OnClick({R.id.btn_right, R.id.btn_right2,R.id.iv_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                break;
            case R.id.btn_right2:
                break;
            case R.id.iv_open:
                if (mDragLayout.getState() == DragTopLayout.PanelState.EXPANDED) {
                    mDragLayout.toggleTopView();
                    mCanShow = true;
                } else {
//                    if (mProductTypeWindow.isShowing()) {
//                        mProductTypeWindow.dismiss();
                    if (mTypeWindow.isShowing()) {
                        mTypeWindow.dismiss();
                    } else {
                        showPopWindow();
                    }
                }
                mDragLayout.listener(new DragTopLayout.PanelListener() {
                    @Override
                    public void onPanelStateChanged(DragTopLayout.PanelState panelState) {
                        if (panelState == DragTopLayout.PanelState.COLLAPSED) {
                            if (mCanShow) {
                                showPopWindow();
                                mCanShow = false;
                            }
                        } else {
                            //mProductTypeWindow.dismiss();//
                            mTypeWindow.dismiss();
                        }
                    }

                    @Override
                    public void onSliding(float ratio) {

                    }

                    @Override
                    public void onRefresh() {

                    }
                });

                break;
        }
    }
    private void initPopWindow(ArrayList<String> typeList) {
        mTypeWindow = new ProductTypePopup(this,
                DensityUtil.getScreenWidth(getActivityContext()),
                DensityUtil.getScreenHeight(getActivityContext()) - mTlCategory.getHeight(),
                typeList,0);
        mTypeWindow.setViewPager(mVp);
        mTypeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mIvOpen.setImageResource(R.drawable.arrow);
            }
        });
    }
    private ProductTypePopup mTypeWindow;
    private void showPopWindow() {
        int y = /*findViewById(R.id.title_bar).getHeight() +*/ mTlCategory.getHeight() + getStatusBarHeight();
//        mProductTypeWindow.showAtLocation(getRootView(OrderDetailActivity.this), Gravity.NO_GRAVITY, 0, y);
//        mProductTypeAdapter.setSelectIndex(viewpager.getCurrentItem());
        mTypeWindow.setSelect(mVp.getCurrentItem());
        mTypeWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, 0, y);
        mIvOpen.setImageResource(R.drawable.arrow_up);
    }
}
