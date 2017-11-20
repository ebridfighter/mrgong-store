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
import uk.co.ribot.androidboilerplate.data.model.business.AddedProduct;
import uk.co.ribot.androidboilerplate.data.model.net.request.CommitOrderRequest;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderCommitResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.SelfHelpPlaceOrderAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.SelfHelpPlaceOrderPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.SelfHelpPlaceOrderMvpView;
import uk.co.ribot.androidboilerplate.util.ActivityUtil;
import uk.co.ribot.androidboilerplate.util.CommonUtils;
import uk.co.ribot.androidboilerplate.util.ToastUtil;
import uk.co.ribot.androidboilerplate.view.RunwiseDialog;

public class SelfHelpPlaceOrderActivity extends BaseActivity implements SelfHelpPlaceOrderMvpView, SelfHelpPlaceOrderAdapter.OneKeyInterface {

    @Inject
    SelfHelpPlaceOrderPresenter mSelfHelpPlaceOrderPresenter;
    @Inject
    SelfHelpPlaceOrderAdapter mSelfHelpPlaceOrderAdapter;
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

    public static Intent getStartIntent(Context context){
        Intent intent = new Intent(context,SelfHelpPlaceOrderActivity.class);
        return intent;
    }

    private static final int DEFAULT_TYPE = 1 << 0;
    private static final int COMMIT_TYPE = 1 << 1;
    private static final int ADD_PRODUCT = 1 << 2;
    public static final int REQUEST_USER_INFO = 1 << 3;
    int[] loadingImgs = new int[31];
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
    //    private BottomSheetDialog showDialog = new BottomSheetDialog(getActivityContext());
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_help_place_order);
        ButterKnife.bind(this);
        configPersistentComponent.selfHelpPlaceOrderActivityComponent(new ActivityModule(this)).inject(this);
        mSelfHelpPlaceOrderPresenter.attachView(this);
        mReserveGoodsAdvanceDate = mSelfHelpPlaceOrderPresenter.loadUser().getReserveGoodsAdvanceDate();
        cachedDWStr = TimeUtils.getABFormatDate(mReserveGoodsAdvanceDate).substring(5) + " " + TimeUtils.getWeekStr(mReserveGoodsAdvanceDate);
        selectedDate = mReserveGoodsAdvanceDate;
        selectedDateIndex = 1;
        mRlSelfHelp.setVisibility(View.VISIBLE);
        mIvLoading.setVisibility(View.GONE);
        mTvLoading.setVisibility(View.GONE);
        setTitle("自助下单");
        showBackBtn();
        mRvProduct.setLayoutManager(new LinearLayoutManager(getActivityContext()));
        mRvProduct.setVisibility(View.INVISIBLE);
        mSelfHelpPlaceOrderAdapter.setCallback(this);
        mSelfHelpPlaceOrderAdapter.setCanSeePrice(mSelfHelpPlaceOrderPresenter.loadUser().isCanSeePrice());
        mRvProduct.setAdapter(mSelfHelpPlaceOrderAdapter);
        handler.postDelayed(runnable, 0);
        mTvDate.setText(cachedDWStr);
        setTitleEditShow();
        //xml中设置allCb不可点击，点整个layout设置状态
        mLlAll.setOnClickListener(v -> {
            isInitiative = false;//使onCheckChangedListener不会被调用
            if (mCbAll.isChecked()) {
                mCbAll.setChecked(false);
                setDeleteBtnOk(false);
                mSelfHelpPlaceOrderAdapter.setAllSelect(false);
            } else {
                mCbAll.setChecked(true);
                setDeleteBtnOk(true);
                mSelfHelpPlaceOrderAdapter.setAllSelect(true);
            }
            isInitiative = true;
        });
        mCbAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isInitiative) {
                if (isChecked) {
                    //adapter里面所有的选中
                    setDeleteBtnOk(true);
                    mSelfHelpPlaceOrderAdapter.setAllSelect(true);
                } else {
                    //清掉adapter里面所有选中的状态
                    setDeleteBtnOk(false);
                    mSelfHelpPlaceOrderAdapter.setAllSelect(false);
                }
            }
        });
        boolean canSeePrice = mSelfHelpPlaceOrderPresenter.loadUser().isCanSeePrice();
        if (!canSeePrice) {
            mTvMoney.setVisibility(View.GONE);
        }
        mSelfHelpPlaceOrderPresenter.getUserInfo();

    }

    @OnClick({R.id.tv_date, R.id.btn_place_order, R.id.btn_delete, R.id.btn_self_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                //弹出日期选择控件
                if (bDialog.isVisible()) {
                    bDialog.dismiss();
                } else {
                    bDialog.show();
                }
                break;
            case R.id.btn_place_order:
                //下单按钮
                List<AddedProduct> list = mSelfHelpPlaceOrderAdapter.getList();
                List<CommitOrderRequest.ProductsBean> cList = new ArrayList<>();
                for (AddedProduct bean : list) {
                    CommitOrderRequest.ProductsBean pBean = new CommitOrderRequest.ProductsBean();
                    pBean.setProduct_id(bean.getProduct().getProductID());
                    int qty = mSelfHelpPlaceOrderAdapter.getCountMap().get(Integer.valueOf(bean.getProduct().getProductID()));
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
                mSelfHelpPlaceOrderPresenter.commitOrder(TimeUtils.getAB2FormatData(selectedDate), "121", cList);
                showLoadingDialog("下单中...");
                mBtnPlaceOrder.setBackgroundColor(Color.parseColor("#7F9ACC35"));
                mBtnPlaceOrder.setEnabled(false);
                mTvDate.setEnabled(false);
                break;
            case R.id.btn_delete:
                showDialog("提示", "确认删除选中商品?", new RunwiseDialog.DialogListener() {
                    @Override
                    public void doClickButton(Button btn, RunwiseDialog dialog) {
                        mSelfHelpPlaceOrderAdapter.deleteSelectItems();
                        //更新个数
                        countChanged();
                        if (mSelfHelpPlaceOrderAdapter.getItemCount() == 0) {
                            switchEditMode();
                            mRlBottom.setVisibility(View.GONE);
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
                //到添加页面
                Intent intent = new Intent(getActivityContext(), PlaceOrderProductListActivity.class);
                Bundle bundle = new Bundle();
                int size = mSelfHelpPlaceOrderAdapter.getList().size();
                ArrayList<AddedProduct> addedList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    AddedProduct bean = mSelfHelpPlaceOrderAdapter.getList().get(i);
                    addedList.add(bean);
                }
                bundle.putSerializable("ap", addedList);
                intent.putExtra("apbundle", bundle);
                startActivityForResult(intent, ADD_PRODUCT);
                break;
        }
    }

    //切换编辑模式
    private void switchEditMode() {
        if (!editMode) {
            setTitleRightText("完成",view -> {
                switchEditMode();
            });
            showLeftBtn(R.drawable.nav_add, view -> {
                if (editMode) {
                    //到添加页面
                    Intent intent = new Intent(getActivityContext(), PlaceOrderProductListActivity.class);
                    Bundle bundle = new Bundle();
                    int size = mSelfHelpPlaceOrderAdapter.getList().size();
                    ArrayList<AddedProduct> addedList = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        AddedProduct bean = mSelfHelpPlaceOrderAdapter.getList().get(i);
                        addedList.add(bean);
                    }
                    bundle.putSerializable("ap", addedList);
                    intent.putExtra("apbundle", bundle);
                    startActivityForResult(intent, ADD_PRODUCT);
                } else {
                    back();
                }
            });
            mRlSelectBar.setVisibility(View.VISIBLE);
            ViewPropertyAnimator.animate(mRlBottom).setDuration(500).translationY(CommonUtils.dip2px(getActivityContext(), 55));
            ViewPropertyAnimator.animate(mRlSelectBar).setDuration(500).translationY(-CommonUtils.dip2px(getActivityContext(), 55));
            editMode = true;
        } else {
            //完成模式，清空上次选择的
            mSelfHelpPlaceOrderAdapter.clearSelect();
            InputMethodManager imm = (InputMethodManager) getSystemService(getActivityContext().INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
            setTitleRightText("编辑", view -> {
                switchEditMode();
            });
            ViewPropertyAnimator.animate(mRlBottom).setDuration(500).translationY(-CommonUtils.dip2px(getActivityContext(), 55));
            ViewPropertyAnimator.animate(mRlSelectBar).setDuration(500).translationY(CommonUtils.dip2px(getActivityContext(), 55));
            showBackBtn();
            editMode = false;
        }
        mSelfHelpPlaceOrderAdapter.setEditMode(editMode);
        mSelfHelpPlaceOrderAdapter.notifyDataSetChanged();
        if (mSelfHelpPlaceOrderAdapter != null && mSelfHelpPlaceOrderAdapter.getItemCount() == 0) {
            mRlBottom.setVisibility(View.GONE);
            setTitleRightText("");
            mRlSelfHelp.setVisibility(View.VISIBLE);
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

    private void back() {
        if (mSelfHelpPlaceOrderAdapter.getItemCount() > 0) {
            showDialog("提示", "确认取消下单？", new RunwiseDialog.DialogListener() {
                @Override
                public void doClickButton(Button btn, RunwiseDialog dialog) {
                    finish();
                }
            });
            return;
        }
        finish();
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

    int mReserveGoodsAdvanceDate;

    private void setTitleEditShow() {
        if (mSelfHelpPlaceOrderAdapter.getItemCount() == 0) {
            mRlSelfHelp.setVisibility(View.VISIBLE);
            mRvProduct.setVisibility(View.INVISIBLE);
        } else {
            if (editMode) {
                setTitleRightText("完成",view -> {
                    switchEditMode();
                });
            } else {
                setTitleRightText("编辑",view -> {
                    switchEditMode();
                });
            }
            mRlSelfHelp.setVisibility(View.INVISIBLE);
            mRvProduct.setVisibility(View.VISIBLE);
        }
    }

    private void onSuccessCallBack() {
        //停止动画
        handler.removeCallbacks(runnable);
        mIvLoading.setVisibility(View.GONE);
        mTvLoading.setVisibility(View.GONE);
        mRlBottom.setVisibility(View.VISIBLE);
        ViewPropertyAnimator.animate(mRlBottom).translationY(-CommonUtils.dip2px(getActivityContext(), 55));
        mRvProduct.setVisibility(View.VISIBLE);
    }
    int totalNum = 0;
    double totalMoney = 0;
    @Override
    public void countChanged() {
        totalNum = 0;
        totalMoney = 0;
        List<AddedProduct> list = mSelfHelpPlaceOrderAdapter.getList();
            for (AddedProduct addedProduct : list) {
                double price = addedProduct.getProduct().getPrice();
                int count = mSelfHelpPlaceOrderAdapter.getCountMap().get(addedProduct.getProduct().getProductID());
                addedProduct.setCount(count);
                totalNum += count;
                totalMoney += count * price;
                DecimalFormat df = new DecimalFormat("#.00");
                String formatMoney = df.format(totalMoney);
                mTvMoney.setText(formatMoney + "元");
                mTvCount.setText(totalNum + "件");
            }
        setTitleEditShow();

    }

    @Override
    public void showProductSuccess(ProductListResponse.Product product) {


    }


    @Override
    public void selectClicked(SelfHelpPlaceOrderAdapter.SELECTTYPE type) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            ArrayList<AddedProduct> backList = (ArrayList<AddedProduct>) bundle.getSerializable("backap");
            List<AddedProduct> newList = new ArrayList<>();
            if (backList != null) {
                newList.addAll(backList);
                for (AddedProduct pro : backList) {
                    Integer proId = Integer.valueOf(pro.getProductId());
                    Integer count = pro.getCount();
                    mSelfHelpPlaceOrderAdapter.getCountMap().put(proId, count);
                }
                mSelfHelpPlaceOrderAdapter.setData(newList);
            }
            if (backList != null && backList.size() > 0) {
                ToastUtil.show(getActivityContext(), "添加成功");
                mRlSelfHelp.setVisibility(View.GONE);
                mRlBottom.setVisibility(View.VISIBLE);
                ViewPropertyAnimator.animate(mRlBottom).translationY(-CommonUtils.dip2px(getActivityContext(), 55));
                mRvProduct.setVisibility(View.VISIBLE);
                mRlSelectBar.setVisibility(View.GONE);
            } else {
                mRlSelfHelp.setVisibility(View.VISIBLE);
                mRlBottom.setVisibility(View.GONE);
                mRlSelectBar.setVisibility(View.GONE);
            }
            setTitleEditShow();
        }
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
    public void commitOrderSuccess(OrderCommitResponse orderCommitResponse) {
        onSuccessCallBack();
        mBtnPlaceOrder.setBackgroundColor(Color.parseColor("#9ACC35"));
        mTvDate.setEnabled(true);
        startActivity(OrderCommitSuccessActivity.getStartIntent(this, -1, orderCommitResponse.getOrders()));
        finish();
    }

    @Override
    public void commitOrderError() {
        dismissLoadingDialog();
        showNoCancelDialog("提示", "网络连接失败，请查看首页订单列表，检查下单是否成功", "我知道啦", "", new RunwiseDialog.DialogListener() {
            @Override
            public void doClickButton(Button btn, RunwiseDialog dialog) {
                ActivityUtil.getInstance().returnHomePage(MainActivity.class);
            }
        });
    }


}
