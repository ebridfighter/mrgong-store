package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.frament.ReturnOrderListFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.ReturnOrderListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ReturnOrderListMvpView;

/**
 * Created by mike on 2017/11/22.
 */
public class ReturnOrderListFragment extends BaseFragment implements ReturnOrderListMvpView {
    @Inject
    ReturnOrderListPresenter mReturnOrderListPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home_page, null);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ReturnOrderListFragmentComponent fragmentComponent = mFragmentBaseComponent.
                returnOrderListFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
    }
}