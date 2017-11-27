package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderDoAction;
import uk.co.ribot.androidboilerplate.data.model.business.OrderListWrap;
import uk.co.ribot.androidboilerplate.data.model.net.response.DashBoardResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.FinishReturnResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.HomePageFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.FrescoImageLoader;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;
import uk.co.ribot.androidboilerplate.ui.activity.OrderDetailActivity;
import uk.co.ribot.androidboilerplate.ui.adapter.OrderAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.HomePagePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.HomePageMvpView;
import uk.co.ribot.androidboilerplate.view.RunwiseDialog;


/**
 * Created by mike on 2017/10/18.
 * 首页Fragment
 */

public class HomePageFragment extends BaseFragment implements HomePageMvpView, OrderAdapter.DoActionInterface {
    @Inject
    HomePagePresenter mHomePagePresenter;
    @Inject
    OrderAdapter mOrderAdapter;
    @BindView(R.id.rv_product)
    RefreshRecyclerView mRvProduct;
    Unbinder unbinder;

    HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    ViewHolder mViewHolder;
    int mCurrentRequestFinishCount = 0;
    public static final int REQUEST_FINISH_COUNT = 4;
    private boolean mIsForeground = false;

    @Inject
    public HomePageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home_page, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomePageFragmentComponent homePageFragmentComponent = mFragmentBaseComponent.
                homePageFragmentComponent(new ActivityModule(getActivity()));
        homePageFragmentComponent.inject(this);

        FrecoFactory.getInstance(getActivity());

        View headerAdvertisementView = getLayout(R.layout.header_advertisement);
        mViewHolder = new ViewHolder(headerAdvertisementView);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mOrderAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headerAdvertisementView);
        mOrderAdapter.setDoActionInterface(this);
        mOrderAdapter.setOnItemClickListener((view, position) -> {
            OrderListWrap orderListWrap = mOrderAdapter.getItem(position);
            if (orderListWrap.getOrderListBean() != null) {
                startActivity(OrderDetailActivity.getStartIntent(getActivity(), orderListWrap.getOrderListBean().getOrderID()));
            } else {
                startActivity(OrderDetailActivity.getStartIntent(getActivity(), orderListWrap.getReturnOrderListBean().getReturnOrderID()));
            }
        });

        mRvProduct.init(new LinearLayoutManager(getActivity()), this, null);
        mRvProduct.setRefreshEnabled(true);
        mRvProduct.setAdapter(mHeaderAndFooterWrapper);
        mRvProduct.getRecyclerView().setItemAnimator(new DefaultItemAnimator());


        mHomePagePresenter.attachView(this);
//        mHomePagePresenter.syncOrders();
//        mHomePagePresenter.syncReturnOrders();
        mHomePagePresenter.getHomePageBanner(getString(R.string.tag_meal_side));
        mHomePagePresenter.getDashBoard();
        mHomePagePresenter.pollingOrders();
        mHomePagePresenter.pollingReturnOrders();
    }

    private void refreshCurrentRequestFinishCount() {
        mCurrentRequestFinishCount++;
        if (mCurrentRequestFinishCount == REQUEST_FINISH_COUNT) {
            mRvProduct.setRefreshing(false);
            mCurrentRequestFinishCount = 0;
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mHomePagePresenter.isViewAttached()) {
                mHomePagePresenter.pollingOrders();
                mHomePagePresenter.pollingReturnOrders();
            }
        }
    }

    @Override
    public boolean isFragmentVisible() {
        return getUserVisibleHint() && mIsForeground;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsForeground = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsForeground = true;
    }

    @Override
    public void showOrders(List<OrderListResponse.ListBean> orders) {
        mOrderAdapter.setOrders(orders);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showReturnOrders(List<ReturnOrderListResponse.ListBean> orders) {
        mOrderAdapter.setReturnOrders(orders);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showHomePageBanner(List<String> imageUrls) {
        mViewHolder.mBannerAdvertisement.setImages(imageUrls).setImageLoader(new FrescoImageLoader()).start();
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showDashBoard(DashBoardResponse dashBoardResponse) {
        DecimalFormat df = new DecimalFormat("#.##");
        double adventValue = dashBoardResponse.getAdventValue();
        double maturityValue = dashBoardResponse.getMaturityValue();
        mViewHolder.mTvLastWeekBuy.setText(df.format(dashBoardResponse.getPurchaseAmount() / 10000));//万元单位
        mViewHolder.mTvLastMonthBuy.setText(df.format(adventValue));
        mViewHolder.mTvPayAccount.setText(df.format(maturityValue));
        refreshCurrentRequestFinishCount();

    }

    @Override
    public void showDashBoardWithoutPrice(DashBoardResponse dashBoardResponse) {
        mViewHolder.mTvLastWeek.setText(R.string.last_week_purchase_amount);
        mViewHolder.mTvLqCount.setText(R.string.advent_food_amount);
        mViewHolder.mTvDqCount.setText(R.string.expire_food_amount);
        mViewHolder.mTvLastWeekBuy.setText(String.valueOf((int) dashBoardResponse.getTotalNumber()));
        int adventNum = (int) dashBoardResponse.getAdventNum();
        int maturityNum = (int) dashBoardResponse.getMaturityNum();
        mViewHolder.mTvLastMonthBuy.setText(String.valueOf(adventNum));
        mViewHolder.mTvPayAccount.setText(String.valueOf(maturityNum));
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void cancelOrderSuccess() {
        dismissLoadingDialog();
        toast(R.string.toast_cancel_order_success);
        mOrderAdapter.removeOrder(mCancelOrderId);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void finishReturnOrderSuccess(FinishReturnResponse finishReturnResponse) {
        dismissLoadingDialog();
        toast(R.string.toast_finish_return_order_success);
        mOrderAdapter.removeReturnOrder(finishReturnResponse.getReturnOrder().getReturnOrderID());
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void showOrdersEmpty() {
        toast(R.string.toast_order_empty);
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showReturnOrdersEmpty() {
//        toast(R.string.toast_return_order_empty);
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showOrdersError() {
        toast(R.string.toast_get_order_list_error);
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showReturnOrdersError() {
        toast(R.string.toast_get_return_order_list_error);
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showHomePageBannerError() {
        toast(R.string.toast_get_banner_error);
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void showDashBoardError() {
        toast(R.string.toast_get_dashboard_error);
        refreshCurrentRequestFinishCount();
    }

    @Override
    public void cancelOrderError() {
        toast(R.string.toast_cancel_order_fail);
        dismissLoadingDialog();
    }

    @Override
    public void finishReturnOrderError() {
        dismissLoadingDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mHomePagePresenter.detachView();
    }

    @Override
    public void onRefresh() {
        mCurrentRequestFinishCount = 0;
        mHomePagePresenter.syncOrders();
        mHomePagePresenter.syncReturnOrders();
        mHomePagePresenter.getHomePageBanner(getString(R.string.tag_meal_side));
        mHomePagePresenter.getDashBoard();
    }

    int mCancelOrderId;

    @Override
    public void doAction(OrderDoAction action, final int position) {
        switch (action) {
            case CANCLE:
                showDialog(getString(R.string.dialog_title_tip), getString(R.string.dialog_message_cancel_order), new RunwiseDialog.DialogListener() {
                    @Override
                    public void doClickButton(Button btn, RunwiseDialog mDialog) {
                        showLoadingDialog();
                        mCancelOrderId = mOrderAdapter.getItem(position).getOrderListBean().getOrderID();
                        mHomePagePresenter.cancelOrder(mCancelOrderId);
                        mDialog.dismiss();
                    }
                });
                break;
            case UPLOAD:
//                Intent uIntent = new Intent(mContext, UploadPayedPicActivity.class);
//                int ordereId = ((OrderResponse.ListBean) adapter.getItem(position)).getOrderID();
//                String orderNmae = ((OrderResponse.ListBean) adapter.getItem(position)).getName();
//                uIntent.putExtra("orderid", ordereId);
//                uIntent.putExtra("ordername", orderNmae);
//                startActivity(uIntent);
                break;
            case LOOK:
//                hasattachment
//                Intent lIntent = new Intent(mContext, UploadPayedPicActivity.class);
//                int ordereId2 = ((OrderResponse.ListBean) adapter.getItem(position)).getOrderID();
//                String orderNmae2 = ((OrderResponse.ListBean) adapter.getItem(position)).getName();
//                lIntent.putExtra("orderid", ordereId2);
//                lIntent.putExtra("ordername", orderNmae2);
//                lIntent.putExtra("hasattachment", true);
//                startActivity(lIntent);

                break;
            case TALLY:
                //点货，计入结算单位
//                Intent tIntent = new Intent(mContext, ReceiveActivity.class);
//                Bundle tBundle = new Bundle();
//                tBundle.putParcelable("order", (OrderResponse.ListBean) adapter.getItem(position));
//                tBundle.putInt("mode", 1);
//                tIntent.putExtras(tBundle);
//                startActivity(tIntent);
                break;
            case TALLYING:
                String name = mOrderAdapter.getItem(position).getOrderListBean().getTallyingUserName();
                showDialog("", name + getString(R.string.dialog_message_tallying), getString(R.string.dialog_btn_i_know), null);
                break;
            case RATE:
                //评价
//                Intent rIntent = new Intent(mContext, EvaluateActivity.class);
//                final OrderResponse.ListBean bean = (OrderResponse.ListBean) adapter.getList().get(position);
//                Bundle rBundle = new Bundle();
//                rBundle.putParcelable("order", bean);
//                rIntent.putExtras(rBundle);
//                startActivity(rIntent);
                break;
            case RECEIVE://正常收货
//                Intent intent = new Intent(mContext, ReceiveActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("order", (OrderResponse.ListBean) adapter.getItem(position));
//                bundle.putInt("mode", 0);
//                intent.putExtras(bundle);
//                startActivity(intent);
                break;
            case SETTLERECEIVE:
                //点货，计入结算单位
//                Intent sIntent = new Intent(mContext, ReceiveActivity.class);
//                Bundle sBundle = new Bundle();
//                sBundle.putParcelable("order", (OrderResponse.ListBean) adapter.getItem(position));
//                sBundle.putInt("mode", 2);
//                sIntent.putExtras(sBundle);
//                startActivity(sIntent);
                break;
            case SELFTALLY:
                showDialog("", getString(R.string.dialog_message_the_goods_have_been_ordered), null);
                break;
            case FINISH_RETURN:
                showDialog(getString(R.string.dialog_title_tip), "确认数量一致?", new RunwiseDialog.DialogListener() {
                    @Override
                    public void doClickButton(Button btn, RunwiseDialog mDialog) {
                        showLoadingDialog();
                        mHomePagePresenter.finishOrder(mOrderAdapter.getItem(position).getReturnOrderListBean().getReturnOrderID());
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void call(String phone) {

    }


    static class ViewHolder {
        @BindView(R.id.banner_advertisement)
        Banner mBannerAdvertisement;
        @BindView(R.id.tv_last_week)
        TextView mTvLastWeek;
        @BindView(R.id.tv_last_week_buy)
        TextView mTvLastWeekBuy;
        @BindView(R.id.ll_procurement)
        LinearLayout mLlProcurement;
        @BindView(R.id.tv_lq_count)
        TextView mTvLqCount;
        @BindView(R.id.lastMonthBuy)
        TextView mTvLastMonthBuy;
        @BindView(R.id.ll_lq)
        LinearLayout mLlLq;
        @BindView(R.id.tv_dq_count)
        TextView mTvDqCount;
        @BindView(R.id.tv_pay_account)
        TextView mTvPayAccount;
        @BindView(R.id.ll_dq)
        LinearLayout mLlDq;
        @BindView(R.id.ll_nums)
        LinearLayout mLlNums;
        @BindView(R.id.v_space)
        View mVSpace;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
