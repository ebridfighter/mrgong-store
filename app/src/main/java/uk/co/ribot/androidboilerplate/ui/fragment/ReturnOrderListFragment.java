package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.ReturnOrderListFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.ReturnOrderListAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;

/**
 * Created by mike on 2017/11/22.
 */
public class ReturnOrderListFragment extends BaseFragment {

    @BindView(R.id.rv_return_order)
    RefreshRecyclerView mRvReturnOrder;
    @BindView(R.id.include_loading)
    View mIncludeLoading;
    Unbinder unbinder;
    @Inject
    ReturnOrderListAdapter mReturnOrderListAdapter;

    public static final String BUNDLE_KEY_LIST = "bundle_key_list";
    public static final String BUNDLE_KEY_CAN_SEE_PRICE = "bundle_key_can_see_price";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_return_order_list, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ReturnOrderListFragmentComponent fragmentComponent = mFragmentBaseComponent.
                returnOrderListFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
        mRvReturnOrder.init(new LinearLayoutManager(getActivity()),this,null);
        mRvReturnOrder.setAdapter(mReturnOrderListAdapter);
        mReturnOrderListAdapter.setData((List<ReturnOrderListResponse.ListBean>) getArguments().getSerializable(BUNDLE_KEY_LIST));
        mReturnOrderListAdapter.setCanSeePrice(getArguments().getBoolean(BUNDLE_KEY_CAN_SEE_PRICE));
        mIncludeLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}