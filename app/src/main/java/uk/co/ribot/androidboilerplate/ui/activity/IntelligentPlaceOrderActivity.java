package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.runwise.commomlibrary.view.LoadingLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BottomDialog;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.DefaultProductResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.IntelligentProductDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.IntelligentPlaceOrderAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.IntelligentPlaceOrderPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.IntelligentPlaceOrderMvpView;
import uk.co.ribot.androidboilerplate.util.CommonUtils;
import uk.co.ribot.androidboilerplate.view.RunwiseDialog;

public class IntelligentPlaceOrderActivity extends BaseActivity implements IntelligentPlaceOrderMvpView, IntelligentPlaceOrderAdapter.OneKeyInterface {

    public static final String INTENT_KEY_ESTIMATED_TURNOVER = "intent_key_estimated_turnover";
    public static final String INTENT_KEY_SAFETY_FACTOR = "intent_key_safety_factor";
    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    @BindView(R.id.tv_loading)
    TextView mTvLoading;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.btn_place_order)
    Button mBtnPlaceOrder;
    @BindView(R.id.rl_bottom)
    LinearLayout mRlBottom;
    @BindView(R.id.cb_all)
    CheckBox mCbAll;
    @BindView(R.id.ll_all)
    LinearLayout mLlAll;
    @BindView(R.id.btn_delete)
    Button mBtnDelete;
    @BindView(R.id.rl_select_bar)
    RelativeLayout mRlSelectBar;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.tv_tip2)
    TextView mTvTip2;
    @BindView(R.id.btn_self_add)
    Button mBtnSelfAdd;
    @BindView(R.id.rl_self_help)
    RelativeLayout mRlSelfHelp;
    @BindView(R.id.v_bg)
    View mVBg;
    @BindView(R.id.iv_order_success)
    ImageView mIvOrderSuccess;
    @BindView(R.id.iv_no_purchase)
    ImageView mIvNoPurchase;
    @BindView(R.id.tv_purchase)
    TextView mTvPurchase;
    @BindView(R.id.rl_no_purchase)
    RelativeLayout mRlNoPurchase;
    @BindView(R.id.loadingLayout)
    LoadingLayout mLoadingLayout;
    int[] loadingImgs = new int[31];

    @Inject
    IntelligentPlaceOrderAdapter mIntelligentPlaceOrderAdapter;
    @Inject
    IntelligentPlaceOrderPresenter mIntelligentPlaceOrderPresenter;

    //标记是否主动点击全部,默认是主动true
    private boolean isInitiative = true;
    //弹窗星期的View集合
    private TextView[] wArr = new TextView[3];
    private TextView[] dArr = new TextView[3];
    //记录当前是选中的哪个送货时期，默认明天, 0今天，1明天，2后天
    private int selectedDate;
    private int selectedDateIndex;
    //缓存外部显示用的日期周几
    private String cachedDWStr;
    //标记当前是否在编辑模式
    private boolean editMode;
    private int currentIndex;
    private BottomDialog bDialog = BottomDialog.create(getSupportFragmentManager())
            .setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    initDefaultDate(v);
                }
            }).setLayoutRes(R.layout.date_layout)
            .setCancelOutside(true)
            .setDimAmount(0.5f);
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mIvLoading.setImageResource(loadingImgs[currentIndex++]);
            if (currentIndex >= 31) {
                currentIndex = 0;
            }
            handler.postDelayed(runnable, 30);
        }
    };
    private double mEstimatedTurnover;
    private double mSafetyFactor;
    int mReserveGoodsAdvanceDate;

    public static Intent getStartIntent(Context context, double estimatedTurnover, double safetyFactor) {
        Intent intent = new Intent(context, IntelligentPlaceOrderActivity.class);
        intent.putExtra(INTENT_KEY_ESTIMATED_TURNOVER, estimatedTurnover);
        intent.putExtra(INTENT_KEY_SAFETY_FACTOR, safetyFactor);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent_place_order);
        ButterKnife.bind(this);
        configPersistentComponent.intelligentPlaceOrderActivityComponent(new ActivityModule(getActivityContext()))
                .inject(this);
        mIntelligentPlaceOrderPresenter.attachView(this);
        setTitle(getString(R.string.titile_intelligent_place_order));
        showBackBtn();
        setTitleRightText("编辑");
        mRvProduct.setVisibility(View.INVISIBLE);
        mIntelligentPlaceOrderAdapter.setCallback(this);
        mRvProduct.setLayoutManager(new LinearLayoutManager(getActivityContext()));
        mRvProduct.setAdapter(mIntelligentPlaceOrderAdapter);
        initLoadingImgs();
        handler.postDelayed(runnable, 0);
        mTvDate.setText(cachedDWStr);
        mEstimatedTurnover = getIntent().getDoubleExtra(INTENT_KEY_ESTIMATED_TURNOVER, 0);
        mSafetyFactor = getIntent().getDoubleExtra(INTENT_KEY_SAFETY_FACTOR, 0) / 100;
//        showDialog.setTitle("选择送达日期");
//        showDialog.setCancelable(true)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIntelligentPlaceOrderPresenter.getIntelligentProducts(mEstimatedTurnover, mSafetyFactor);
            }
        }, 2000);
        mLlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInitiative = false;
                if (mCbAll.isChecked()) {
                    mCbAll.setChecked(false);
                    setDeleteBtnOk(false);
                    mIntelligentPlaceOrderAdapter.setAllSelect(false);
                } else {
                    mCbAll.setChecked(true);
                    setDeleteBtnOk(true);
                    mIntelligentPlaceOrderAdapter.setAllSelect(true);
                }
                isInitiative = true;
            }
        });
        mCbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isInitiative) {
                    if (isChecked) {
                        //adapter里面所有的选中
                        setDeleteBtnOk(true);
                        mIntelligentPlaceOrderAdapter.setAllSelect(true);
                    } else {
                        //清掉adapter里面所有选中的状态
                        setDeleteBtnOk(false);
                        mIntelligentPlaceOrderAdapter.setAllSelect(false);
                    }
                }
            }
        });
        if (!mIntelligentPlaceOrderPresenter.canSeePrice()) {
            mTvMoney.setVisibility(View.GONE);
        }
        mIntelligentPlaceOrderPresenter.getUserInfo();

    }

    private void switchEditMode() {
        //强制隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(getActivityContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLlAll.getWindowToken(), 0); //强制隐藏键盘
        if (!editMode) {
            this.setTitleRightText("完成");
//            setTitleLeftIcon(true, R.drawable.nav_add);
            mRlSelectBar.setVisibility(View.VISIBLE);
            ViewPropertyAnimator.animate(mRlBottom).setDuration(500).translationY(CommonUtils.dip2px(getActivityContext(), 55));
            ViewPropertyAnimator.animate(mRlSelectBar).setDuration(500).translationY(-CommonUtils.dip2px(getActivityContext(), 55));
            editMode = true;
        } else {
            mIntelligentPlaceOrderAdapter.clearSelect();
            setTitleRightText("编辑");
            ViewPropertyAnimator.animate(mRlBottom).setDuration(500).translationY(-CommonUtils.dip2px(getActivityContext(), 55));
            ViewPropertyAnimator.animate(mRlSelectBar).setDuration(500).translationY(CommonUtils.dip2px(getActivityContext(), 55));
            showBackBtn();
            editMode = false;
        }
        mIntelligentPlaceOrderAdapter.setEditMode(editMode);
        mIntelligentPlaceOrderAdapter.notifyDataSetChanged();
        if (mIntelligentPlaceOrderAdapter != null && mIntelligentPlaceOrderAdapter.getItemCount() == 0) {
            mRlBottom.setVisibility(View.INVISIBLE);
            setTitleRightText("");
        }
    }

    private void setDeleteBtnOk(boolean isOk) {
        if (isOk) {
            mBtnDelete.setEnabled(true);
            mBtnDelete.setBackgroundResource(R.drawable.product_delete_ok);
            mBtnDelete.setTextColor(Color.parseColor("#FF3B30"));
        } else {
            mBtnDelete.setEnabled(false);
            mBtnDelete.setBackgroundResource(R.drawable.product_delete_circle);
            mBtnDelete.setTextColor(Color.parseColor("#E3E3E3"));
        }

    }

    private void initDefaultDate(View v) {
        RelativeLayout rll1 = (RelativeLayout) v.findViewById(R.id.rll1);
        RelativeLayout rll2 = (RelativeLayout) v.findViewById(R.id.rll2);
        RelativeLayout rll3 = (RelativeLayout) v.findViewById(R.id.rll3);
        TextView wTv1 = (TextView) v.findViewById(R.id.wTv1);
        TextView dTv1 = (TextView) v.findViewById(R.id.dTv1);
        TextView wTv2 = (TextView) v.findViewById(R.id.wTv2);
        TextView dTv2 = (TextView) v.findViewById(R.id.dTv2);
        TextView wTv3 = (TextView) v.findViewById(R.id.wTv3);
        TextView dTv3 = (TextView) v.findViewById(R.id.dTv3);
        wArr[0] = wTv1;
        wArr[1] = wTv2;
        wArr[2] = wTv3;
        dArr[0] = dTv1;
        dArr[1] = dTv2;
        dArr[2] = dTv3;
        //选中哪个，通过selectedDate来判断
        wArr[selectedDateIndex].setTextColor(Color.parseColor("#6BB400"));
        dArr[selectedDateIndex].setTextColor(Color.parseColor("#6BB400"));
        //计算当前日期起，明后天的星期几+号数
        wTv1.setText(TimeUtils.getWeekStr(mReserveGoodsAdvanceDate - 1));
        String[] t = TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate - 1).split("-");
        if (t.length > 2) {
            dTv1.setText(t[1] + "-" + t[2]);
        }
        wTv2.setText(TimeUtils.getWeekStr(mReserveGoodsAdvanceDate));
        t = TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate).split("-");
        if (t.length > 2) {
            dTv2.setText(t[1] + "-" + t[2]);
        }
        wTv3.setText(TimeUtils.getWeekStr(mReserveGoodsAdvanceDate + 1));
        t = TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate + 1).split("-");
        if (t.length > 2) {
            dTv3.setText(t[1] + "-" + t[2]);
        }
        //初始化点击事件
        rll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空颜色
                setSelectedColor(0);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectedDate = mReserveGoodsAdvanceDate - 1;
                        selectedDateIndex = 0;
                        bDialog.dismiss();
                        mTvDate.setText(TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate - 1).substring(5) + " " + TimeUtils.getWeekStr(mReserveGoodsAdvanceDate - 1));
                    }
                }, 500);
            }
        });
        rll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空颜色
                setSelectedColor(1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectedDate = mReserveGoodsAdvanceDate;
                        selectedDateIndex = 1;
                        bDialog.dismiss();
                        mTvDate.setText(TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate).substring(5) + " " + TimeUtils.getWeekStr(mReserveGoodsAdvanceDate));
                    }
                }, 500);
            }
        });
        rll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空颜色
                setSelectedColor(2);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectedDate = mReserveGoodsAdvanceDate + 1;
                        selectedDateIndex = 2;
                        bDialog.dismiss();
                        mTvDate.setText(TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate + 1).substring(5) + " " + TimeUtils.getWeekStr(mReserveGoodsAdvanceDate + 1));
                    }
                }, 500);
            }
        });
    }

    //参数从0开始
    private void setSelectedColor(int i) {
        for (TextView tv : wArr) {
            if (tv != null) {
                tv.setTextColor(Color.parseColor("#2E2E2E"));
            }
        }
        for (TextView tv : dArr) {
            if (tv != null) {
                tv.setTextColor(Color.parseColor("#2E2E2E"));
            }
        }
        if (wArr[i] != null) {
            wArr[i].setTextColor(Color.parseColor("#6BB400"));
        }
        if (dArr[i] != null) {
            dArr[i].setTextColor(Color.parseColor("#6BB400"));
        }
    }

    private void initLoadingImgs() {
        StringBuffer sb;
        for (int i = 0; i < 31; i++) {
            sb = new StringBuffer("order_loading_");
            sb.append(i);
            loadingImgs[i] = getResIdByDrawableName(sb.toString());
        }
    }

    private void onSuccessCallBack() {
        //停止动画
        handler.removeCallbacks(runnable);
        mIvLoading.setVisibility(View.INVISIBLE);
        mTvLoading.setVisibility(View.INVISIBLE);
        mRlBottom.setVisibility(View.VISIBLE);
        ViewPropertyAnimator.animate(mRlBottom).translationY(-CommonUtils.dip2px(getActivityContext(), 55));
        mRvProduct.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_date, R.id.btn_place_order, R.id.btn_delete, R.id.btn_self_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_place_order:
                //下单按钮
                List<DefaultProductResponse> list = mIntelligentPlaceOrderAdapter.getList();
                List<CommitOrderRequest.ProductsBean> cList = new ArrayList<>();
                for (DefaultProductResponse bean : list) {
                    CommitOrderRequest.ProductsBean pBean = new CommitOrderRequest.ProductsBean();
                    pBean.setProduct_id(bean.getProductID());
                    int qty = mIntelligentPlaceOrderAdapter.getCountMap().get(Integer.valueOf(bean.getProductID()));
                    if (qty == 0) {
                        continue;
                    }
                    pBean.setQty(qty);
                    cList.add(pBean);
                }
                if (cList.size() == 0) {
                    toast("你还没选择任何商品!");
                    return;
                }
                mIntelligentPlaceOrderPresenter.commitOrder(TimeUtils.getAB2FormatData(selectedDate), "121", cList);
                mBtnPlaceOrder.setBackgroundColor(Color.parseColor("#7F9ACC35"));
                mBtnPlaceOrder.setEnabled(false);
                mTvDate.setEnabled(false);
                break;
            case R.id.btn_delete:
                showDialog("提示", "确认删除选中商品?", new RunwiseDialog.DialogListener() {
                    @Override
                    public void doClickButton(Button btn, RunwiseDialog dialog) {
                        mIntelligentPlaceOrderAdapter.deleteSelectItems();
                        //更新个数
                        countChanged();
                        if (mIntelligentPlaceOrderAdapter.getList().size() == 0) {
                            switchEditMode();
                            mRlBottom.setVisibility(View.INVISIBLE);
                            setTitleRightText("");
                            showBackBtn();
                            setSelectedColor(1);
                            selectedDate = mReserveGoodsAdvanceDate;
                            selectedDateIndex = 1;
                            if (bDialog != null && bDialog.isVisible()) {
                                bDialog.dismiss();
                            }
                            mTvDate.setText(TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate).substring(5) + " " + TimeUtils.getWeekStr(mReserveGoodsAdvanceDate));
                        }
                    }
                });
                break;
            case R.id.btn_self_add:
                break;
            case R.id.tv_date:
                //弹出日期选择控件
                if (bDialog.isVisible()) {
                    bDialog.dismiss();
                } else {
                    bDialog.show();
                }
                break;
        }
    }

    @Override
    public void countChanged() {
        int totalNum = 0;
        double totalMoney = 0;
        DecimalFormat df = new DecimalFormat("##.#");
        List<DefaultProductResponse> list = mIntelligentPlaceOrderAdapter.getList();
        for (DefaultProductResponse bean : list) {
            int count = mIntelligentPlaceOrderAdapter.getCountMap().get(Integer.valueOf(bean.getProductID()));
            totalNum += count;
//            double price = ProductBasicUtils.getBasicMap(mContext).get(String.valueOf(bean.getProductID())).getPrice();
//            totalMoney += count * price;
        }
        mLoadingLayout.onSuccess(list.size(), "哎呀！这里是空哒~~", R.drawable.default_icon_ordernone);
        mTvMoney.setText(df.format(totalMoney) + "元");
        mTvCount.setText(totalNum + "件");
    }

    @Override
    public void selectClicked(IntelligentPlaceOrderAdapter.SELECTTYPE type) {
        isInitiative = false;
        switch (type) {
            case ALL_SELECT:
                mCbAll.setChecked(true);
                setDeleteBtnOk(true);
                break;
            case PART_SELECT:
                isInitiative = false;
                mCbAll.setChecked(false);
                setDeleteBtnOk(true);
                break;
            case NO_SELECT:
                setDeleteBtnOk(false);
                mCbAll.setChecked(false);
                break;
        }
        isInitiative = true;
    }

    @Override
    public void getUserInfoSuccess(UserInfoResponse userInfoResponse) {
        mReserveGoodsAdvanceDate = userInfoResponse.getReserveGoodsAdvanceDate();
        cachedDWStr = TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate).substring(5) + " " + TimeUtils.getWeekStr(mReserveGoodsAdvanceDate);
        if (TextUtils.isEmpty(mTvDate.getText().toString())) mTvDate.setText(cachedDWStr);
        selectedDate = mReserveGoodsAdvanceDate;
        selectedDateIndex = 1;
    }

    @Override
    public void showIntelligentProducts(IntelligentProductDataResponse intelligentProductDataResponse) {
        onSuccessCallBack();
        mIntelligentPlaceOrderAdapter.setData(intelligentProductDataResponse.getList());
        if (intelligentProductDataResponse != null && intelligentProductDataResponse.getList() != null && intelligentProductDataResponse.getList().size() == 0) {
            //显示UI
            mRlNoPurchase.setVisibility(View.VISIBLE);
            setTitleRightText("");
        }
    }

    @Override
    public void commitOrderSuccess(OrderCommitResponse orderCommitResponse) {
        onSuccessCallBack();
        finish();
        mBtnPlaceOrder.setBackgroundColor(Color.parseColor("#9ACC35"));
//        dateTv.setEnabled(true);
//        EventBus.getDefault().post(new OrderSuccessEvent());
//
//        final OrderCommitResponse orderCommitResponse = (OrderCommitResponse) result.getResult().getData();
//        Intent intent = new Intent(OneKeyOrderActivity.this,OrderCommitSuccessActivity.class);
//        intent.putParcelableArrayListExtra(OrderCommitSuccessActivity.INTENT_KEY_ORDERS,orderCommitResponse.getOrders());
//        startActivity(intent);
    }
}
