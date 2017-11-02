package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.event.LogOutEvent;
import uk.co.ribot.androidboilerplate.ui.activity.ProductListActivity;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;

/**
 * Created by mike on 2017/10/31.
 * 更多页fragment
 */

public class MoreFragment extends BaseFragment {
    @BindView(R.id.btn_product_list)
    Button mBtnProductList;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_more, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_product_list, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_product_list:
                startActivity(ProductListActivity.getIntent(getActivity()));
                break;
            case R.id.btn_logout:
                BoilerplateApplication.get(getActivity()).getComponent().eventBus().post(new LogOutEvent());
                break;
        }
    }
}

