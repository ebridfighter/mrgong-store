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
import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.PandianResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.MakeInventoryDetailFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.MakeInventoryDetailAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MakeInventoryDetailFragmentPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryDetailFragmentMvpView;

import static uk.co.ribot.androidboilerplate.ui.activity.MakeInventoryDetailActivity.INTENT_KEY_INVENTORY_RESPONSE_LISTBEAN;

/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryDetailFragment extends BaseFragment implements MakeInventoryDetailFragmentMvpView {
    @Inject
    MakeInventoryDetailFragmentPresenter mMakeInventoryDetailFragmentPresenter;
    @Inject
    MakeInventoryDetailAdapter mMakeInventoryDetailAdapter;
    @BindView(R.id.rv_product)
    RefreshRecyclerView mRvProduct;
    @BindView(R.id.include_loading)
    View mIncludeLoading;
    Unbinder unbinder;

    public static final String BUNDLE_KEY_LIST = "bundle_key_list";
    List<PandianResponse.InventoryBean.LinesBean> mLinesBeans;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_make_inventory_detail, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MakeInventoryDetailFragmentComponent fragmentComponent = mFragmentBaseComponent.
                makeInventoryDetailFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
        mMakeInventoryDetailFragmentPresenter.attachView(this);
        mRvProduct.init(new LinearLayoutManager(getActivity()), null, null);
        InventoryResponse.ListBean listBean = (InventoryResponse.ListBean) getActivity().getIntent().getSerializableExtra(INTENT_KEY_INVENTORY_RESPONSE_LISTBEAN);
        if ("confirm".equals(listBean.getState())) {
            mMakeInventoryDetailAdapter.setReading(true);
        }
        mRvProduct.setAdapter(mMakeInventoryDetailAdapter);
        mLinesBeans = (List<PandianResponse.InventoryBean.LinesBean>) getArguments().getSerializable(BUNDLE_KEY_LIST);
        mMakeInventoryDetailAdapter.setData(mLinesBeans);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}