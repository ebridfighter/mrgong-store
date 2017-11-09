package uk.co.ribot.androidboilerplate.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.OrderProductsAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;

public class OrderProductFragment extends BaseFragment {

    public static final String BUNDLE_KEY_LIST = "bundle_key_list";
    public static final String BUNDLE_KEY_ORDER_DATA = "bundle_key_order_data";
    @BindView(R.id.rv_product_list)
    RecyclerView mRvProductList;
    Unbinder unbinder;
    @Inject
    OrderProductsAdapter mOrderProductsAdapter;

    public OrderProductFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_product, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentBaseComponent.orderProductFragmentComponent(new ActivityModule(getActivity())).inject(this);
        ArrayList<OrderListResponse.ListBean.LinesBean> products = (ArrayList<OrderListResponse.ListBean.LinesBean>) getArguments().getSerializable(BUNDLE_KEY_LIST);
        mRvProductList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderProductsAdapter.setProducts(products);
        mRvProductList.setAdapter(mOrderProductsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
