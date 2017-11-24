package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.OrderListFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.OrderListAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.OrderListFragmentPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderListFragmentMvpView;

/**
 * Created by mike on 2017/11/22.
 */
public class OrderListFragment extends BaseFragment implements OrderListFragmentMvpView {
    @Inject
    OrderListFragmentPresenter mOrderListFragmentPresenter;
    @Inject
    OrderListAdapter mOrderListAdapter;

    public static final String ARGUMENT_KEY_START_TIME = "argument_key_start_time";
    public static final String ARGUMENT_KEY_END_TIME = "argument_key_end_time";

    String mStartTime;
    String mEndTime;
    int mPage = 1;
    final int mPageSize = 20;
    @BindView(R.id.rv_order)
    RefreshRecyclerView mRvOrder;
    @BindView(R.id.include_loading)
    View mIncludeLoading;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order_list, null);
        mStartTime = getArguments().getString(ARGUMENT_KEY_START_TIME);
        mEndTime = getArguments().getString(ARGUMENT_KEY_END_TIME);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OrderListFragmentComponent fragmentComponent = mFragmentBaseComponent.
                orderListFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
        mOrderListFragmentPresenter.attachView(this);

        mOrderListAdapter.setCanSeePrice(mOrderListFragmentPresenter.canSeePrice());
        mOrderListAdapter.setUserName(mOrderListFragmentPresenter.loadUser().getUsername());
        mRvOrder.init(new LinearLayoutManager(getActivity()), this, this);
        mRvOrder.setRefreshEnabled(true);
        mRvOrder.setAdapter(mOrderListAdapter);
        mPage = 1;
        mOrderListFragmentPresenter.getOrders(mPage, mPageSize, mStartTime, mEndTime);
        mIncludeLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mOrderListFragmentPresenter.getOrders(mPage, mPageSize, mStartTime, mEndTime);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        mOrderListFragmentPresenter.getOrders(mPage, mPageSize, mStartTime, mEndTime);
    }

    @Override
    public void showOrders(OrderResponse orderResponse) {
        mIncludeLoading.setVisibility(View.GONE);
        if (mPage == 1) {
            mOrderListAdapter.setData(orderResponse.getList());
            mRvOrder.setRefreshing(false);
        } else {
            mOrderListAdapter.appendData(orderResponse.getList());
            mRvOrder.setLoadingMore(false);
        }
        if (orderResponse.getList().size() < mPageSize) {
            mRvOrder.setLoadingMoreEnable(false);
        }
    }

    @Override
    public void showOrdersError() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}