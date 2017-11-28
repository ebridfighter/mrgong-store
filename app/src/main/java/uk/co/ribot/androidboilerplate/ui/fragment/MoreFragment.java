package uk.co.ribot.androidboilerplate.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.runwise.commomlibrary.util.DensityUtil;
import com.runwise.commomlibrary.util.NumberUtil;
import com.runwise.commomlibrary.view.ObservableScrollView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.event.LogOutEvent;
import uk.co.ribot.androidboilerplate.data.model.net.response.ShopInfoResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.ImageManager;
import uk.co.ribot.androidboilerplate.tools.UserUtils;
import uk.co.ribot.androidboilerplate.ui.activity.LoginActivity;
import uk.co.ribot.androidboilerplate.ui.activity.OrderListActivity;
import uk.co.ribot.androidboilerplate.ui.activity.PriceListActivity;
import uk.co.ribot.androidboilerplate.ui.activity.ReturnOrderListActivity;
import uk.co.ribot.androidboilerplate.ui.activity.StatementAccountActivity;
import uk.co.ribot.androidboilerplate.ui.activity.UserInfoActivity;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MorePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MoreMvpView;
import uk.co.ribot.androidboilerplate.view.SystemUpgradeLayout;

/**
 * Created by mike on 2017/10/31.
 * 更多页fragment
 */

public class MoreFragment extends BaseFragment implements MoreMvpView {
    Unbinder unbinder;
    @Inject
    MorePresenter mMorePresenter;
    @BindView(R.id.sdv_head)
    SimpleDraweeView mSdvHead;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.sul)
    SystemUpgradeLayout mSul;
    @BindView(R.id.rb_peisong)
    RatingBar mRbPeisong;
    @BindView(R.id.tv_peisong)
    TextView mTvPeisong;
    @BindView(R.id.iv_peisong)
    ImageView mIvPeisong;
    @BindView(R.id.rb_zl)
    RatingBar mRbZl;
    @BindView(R.id.tv_zl)
    TextView mTvZl;
    @BindView(R.id.iv_zl)
    ImageView mIvZl;
    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.ll_1)
    LinearLayout mLl1;
    @BindView(R.id.ll_2)
    LinearLayout mLl2;
    @BindView(R.id.ll_3)
    LinearLayout mLl3;
    @BindView(R.id.ll_4)
    LinearLayout mLl4;
    @BindView(R.id.iv_right_row)
    ImageView mIvRightRow;
    @BindView(R.id.rl_stocktaking_record)
    RelativeLayout mRlStocktakingRecord;
    @BindView(R.id.iv_right_row1)
    ImageView mIvRightRow1;
    @BindView(R.id.rl_price_list)
    RelativeLayout mRlPriceList;
    @BindView(R.id.iv_right_row2)
    ImageView mIvRightRow2;
    @BindView(R.id.orderRed)
    View mOrderRed;
    @BindView(R.id.rl_bill)
    RelativeLayout mRlBill;
    @BindView(R.id.iv_right_row3)
    ImageView mIvRightRow3;
    @BindView(R.id.rl_procurement)
    RelativeLayout mRlProcurement;
    @BindView(R.id.iv_right_row4)
    ImageView mIvRightRow4;
    @BindView(R.id.rl_transfer)
    RelativeLayout mRlTransfer;
    @BindView(R.id.tv_money_sum)
    TextView mTvMoneySum;
    @BindView(R.id.tv_money_unit)
    TextView mTvMoneyUnit;
    @BindView(R.id.tv_show)
    TextView mTvShow;
    @BindView(R.id.ll_cai_gou_e)
    LinearLayout mLlCaiGouE;
    @BindView(R.id.ll_head)
    LinearLayout mLlHead;
    @BindView(R.id.osv)
    ObservableScrollView mOsv;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.ll_setting)
    LinearLayout mLlSetting;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.ll_icon)
    LinearLayout mLlIcon;
    @BindView(R.id.fl_title)
    FrameLayout mFlTitle;
    UserInfoResponse mUserInfoResponse;

    @Inject
    public MoreFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_more, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentBaseComponent.moreFragmentComponent(new ActivityModule(getActivity())).inject(this);
        mMorePresenter.attachView(this);
        if (mMorePresenter.isLogin()) {
            mUserInfoResponse = mMorePresenter.loadUser();
            setLoginStatus(mUserInfoResponse);
        } else {
            setLogoutStatus();
        }


        mFlTitle.setBackgroundColor(Color.TRANSPARENT);
        mOsv.setScreenHeight(DensityUtil.getScreenHeight(getActivity()));
        mOsv.setImageViews(mIvLeft, mIvRight, mTvTitle);
        mOsv.initAlphaTitle(mFlTitle, mLlHead, getResources().getColor(R.color.white), new int[]{226, 229, 232});
        mOsv.setSlowlyChange(true);
        mSul.setPageName("自采商品/门店调拨/修改个人信息功能");
    }

    private void setLogoutStatus() {
        ImageManager.load(getActivity(), mSdvHead);
        mTvPhone.setText("登录/注册");
        mRbPeisong.setRating(0);
        mTvPeisong.setText(0.0 + "");
        mIvPeisong.setImageResource(R.drawable.tag_up);

        mRbZl.setRating(0);
        mTvZl.setText(0.0 + "");
        mIvZl.setImageResource(R.drawable.tag_down);
    }

    private void setLoginStatus(UserInfoResponse userInfo) {
        if (userInfo != null) {
            ImageManager.load(getActivity(), mSdvHead, userInfo.getAvatarUrl());
            mTvPhone.setText(userInfo.getUsername());

            mRbPeisong.setRating((float) userInfo.getCateringServiceScore());
            mTvPeisong.setText(NumberUtil.formatOneBit(String.valueOf(userInfo.getCateringServiceScore())));
            if ("-1".equals(userInfo.getCateringServiceTrend())) {
                mIvPeisong.setImageResource(R.drawable.tag_down);
            } else {
                mIvPeisong.setImageResource(R.drawable.tag_up);
            }

            mRbZl.setRating((float) userInfo.getCateringQualityScore());
            mTvZl.setText(NumberUtil.formatOneBit(String.valueOf(userInfo.getCateringQualityScore())));
            if ("-1".equals(userInfo.getCateringQualityTrend())) {
                mIvZl.setImageResource(R.drawable.tag_down);
            } else {
                mIvZl.setImageResource(R.drawable.tag_up);
            }
        }
        mMorePresenter.getShopInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMorePresenter != null && mMorePresenter.isLogin()) {
            mMorePresenter.getUser();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mMorePresenter.detachView();
    }


    @Override
    public void logout() {
        getRxBus().post(new LogOutEvent());
    }

    @Override
    public void logoutError() {
        toast(R.string.toast_logout_fail);
    }

    @Override
    public void showShopInfo(ShopInfoResponse shopInfoResponse) {
        if (mMorePresenter.canSeePrice()) {
            if (shopInfoResponse.getTotal_amount() > 10000) {
                double price = shopInfoResponse.getTotal_amount() / 10000;
                mTvMoneySum.setText(UserUtils.formatPrice(price + "") + "");
                mTvMoneyUnit.setText("万元");
                mTvMoneyUnit.setVisibility(View.VISIBLE);
            } else {
                mTvMoneySum.setText(UserUtils.formatPrice(shopInfoResponse.getTotal_amount() + "") + "");
                mTvMoneyUnit.setVisibility(View.VISIBLE);
                mTvMoneyUnit.setText("元");
            }
            mTvShow.setText("上周采购额");
        } else {
            mTvMoneySum.setText(shopInfoResponse.getTotal_number() + "");
            mTvMoneyUnit.setText("件");
            mTvMoneyUnit.setVisibility(View.VISIBLE);
            mTvShow.setText("上周采购量");
        }
    }

    @Override
    public void showUserInfo(UserInfoResponse userInfoResponse) {
        if (userInfoResponse.isHasNewInvoice()) {
            mOrderRed.setVisibility(View.VISIBLE);
        } else {
            mOrderRed.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userInfoResponse.getIsZicai())&& Boolean.parseBoolean(userInfoResponse.getIsZicai())) {
            mRlProcurement.setVisibility(View.VISIBLE);
        } else {
            mRlProcurement.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userInfoResponse.getIsShopTransfer())&& Boolean.parseBoolean(userInfoResponse.getIsShopTransfer())) {
            mRlTransfer.setVisibility(View.VISIBLE);
        } else {
            mRlTransfer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProcumentPermission(UserInfoResponse userInfoResponse) {
        if (Boolean.parseBoolean(userInfoResponse.getIsZicai())) {
            //跳去采购页面
//            startActivity(new Intent(mContext, ProcurementActivity.class));
        } else {
            toast(R.string.toast_no_procument_permission);
        }
    }

    @Override
    public void showTransferPermission(UserInfoResponse userInfoResponse) {
        if (Boolean.parseBoolean(userInfoResponse.getIsShopTransfer())) {
//            startActivity(new Intent(mContext, TransferListActivity.class));
        } else {
            toast(R.string.toast_no_transfer_permission);
        }
    }


    @OnClick({R.id.tv_phone, R.id.sdv_head, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.iv_right_row, R.id.rl_stocktaking_record, R.id.iv_right_row1, R.id.rl_price_list, R.id.rl_bill, R.id.rl_procurement, R.id.rl_transfer, R.id.ll_cai_gou_e, R.id.ll_head, R.id.iv_left, R.id.iv_right, R.id.ll_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sdv_head:
                startActivity(UserInfoActivity.getStartIntent(getActivity()));
                break;
            case R.id.ll_1:
                startActivity(OrderListActivity.getStartIntent(getActivity(),1));
                break;
            case R.id.ll_2:
                startActivity(OrderListActivity.getStartIntent(getActivity(),2));
                break;
            case R.id.ll_3:
                startActivity(ReturnOrderListActivity.getStartIntent(getActivity()));
                break;
            case R.id.ll_4:
                startActivity(OrderListActivity.getStartIntent(getActivity(),0));
                break;
            case R.id.iv_right_row:
                break;
            case R.id.rl_stocktaking_record:
                break;
            case R.id.iv_right_row1:
                break;
            case R.id.rl_price_list:
                startActivity(PriceListActivity.getIntent(getActivity()));
                break;
            case R.id.rl_bill:
                startActivity(StatementAccountActivity.getStartIntent(getActivity()));
                break;
            case R.id.rl_procurement:
                mMorePresenter.getProcumentPermission();
                break;
            case R.id.rl_transfer:
                mMorePresenter.getTransferPermission();
                break;
            case R.id.ll_cai_gou_e:
                break;
            case R.id.ll_head:
                break;
            case R.id.iv_left:
                break;
            case R.id.iv_right:
                break;
            case R.id.ll_icon:
                break;
            case R.id.tv_phone:
                if (!mMorePresenter.isLogin()) {
                    startActivity(LoginActivity.getStartIntent(getActivity()));
                }
                break;
        }
    }
}

