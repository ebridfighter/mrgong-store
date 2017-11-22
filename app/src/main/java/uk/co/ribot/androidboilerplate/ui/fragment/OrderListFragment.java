package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.frament.OrderListFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.OrderListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderListMvpView;

/**
 * Created by mike on 2017/11/22.
 */
public class OrderListFragment extends BaseFragment implements OrderListMvpView {
    @Inject
    OrderListPresenter mOrderListPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order_list, null);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OrderListFragmentComponent fragmentComponent = mFragmentBaseComponent.
                orderListFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
    }
}