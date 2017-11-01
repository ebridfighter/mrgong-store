package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
        mHomePagePresenter.attachView(this);
        mHomePagePresenter.syncOrders();
    }

    @Override
    public void showOrders(List<OrderListResponse.ListBean> orders) {
        mOrderAdapter.setRibots(orders);
    }

    @Override
    public void showOrdersEmpty() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mHomePagePresenter.detachView();
    }
}
