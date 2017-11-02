package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.HomePageFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.OrderAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.HomePagePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.HomePageMvpView;

/**
 * Created by mike on 2017/10/18.
 * 首页Fragment
 */

public class HomePageFragment extends BaseFragment implements HomePageMvpView {
    @Inject
    HomePagePresenter mHomePagePresenter;
    @Inject
    OrderAdapter mOrderAdapter;
    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    Unbinder unbinder;


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
        mRvProduct.setAdapter(mOrderAdapter);
        mRvProduct.setLayoutManager(new LinearLayoutManager(getActivity()));

        mHomePagePresenter.attachView(this);
        mHomePagePresenter.syncOrders();
        mHomePagePresenter.syncReturnOrders();
    }

    @Override
    public void showOrders(List<OrderListResponse.ListBean> orders) {
        mOrderAdapter.setOrders(orders);
        mOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void showReturnOrders(List<ReturnOrderListResponse.ListBean> orders) {
        mOrderAdapter.setReturnOrders(orders);
        mOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void showOrdersEmpty() {
        toast(R.string.toast_order_empty);
    }

    @Override
    public void showReturnOrdersEmpty() {
        toast(R.string.toast_return_order_empty);
    }

    @Override
    public void showOrdersError() {
        toast(R.string.toast_get_order_list_error);
    }

    @Override
    public void showReturnOrdersError() {
        toast(R.string.toast_get_return_order_list_error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mHomePagePresenter.detachView();
    }
}
