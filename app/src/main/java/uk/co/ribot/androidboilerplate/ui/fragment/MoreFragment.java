package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.event.LogOutEvent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.ProductListActivity;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MorePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MoreMvpView;

/**
 * Created by mike on 2017/10/31.
 * 更多页fragment
 */

public class MoreFragment extends BaseFragment implements MoreMvpView {
    @BindView(R.id.btn_product_list)
    Button mBtnProductList;
    Unbinder unbinder;
    @Inject
    MorePresenter mMorePresenter;

    @Inject
    public MoreFragment(){}

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mMorePresenter.detachView();
    }

    @OnClick({R.id.btn_product_list, R.id.btn_logout,R.id.btn_logout_local})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_product_list:
                startActivity(ProductListActivity.getIntent(getActivity()));
                break;
            case R.id.btn_logout:
                mMorePresenter.logout();
                break;
            case R.id.btn_logout_local:
                mMorePresenter.logoutLocal();
                break;
        }
    }

    @Override
    public void logout() {
        BoilerplateApplication.get(getActivity()).getComponent().eventBus().post(new LogOutEvent());
    }

    @Override
    public void logoutError() {
        toast(R.string.toast_logout_fail);
    }
}

