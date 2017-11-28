package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderState;
import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.MakeInventoryFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.SystemUpgradeHelper;
import uk.co.ribot.androidboilerplate.ui.activity.MakeInventoryDetailActivity;
import uk.co.ribot.androidboilerplate.ui.adapter.MakeInventoryAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MakeInventoryFragmentPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryFragmentMvpView;

/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryFragment extends BaseFragment implements MakeInventoryFragmentMvpView {
    @Inject
    MakeInventoryFragmentPresenter mMakeInventoryFragmentPresenter;
    @Inject
    MakeInventoryAdapter mMakeInventoryAdapter;
    public static final String BUNDLE_KEY_TYPE = "bundle_key_type";
    public static final int TYPE_ALL = 0;
    public static final int TYPE_BEN_ZHOU = 1;
    public static final int TYPE_SHANG_ZHOU = 2;
    public static final int TYPE_GENG_ZAO = 3;
    int mPage = 1;
    public static final int PAGE_SIZE = 20;
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.rv_inventory)
    RefreshRecyclerView mRvInventory;
    @BindView(R.id.include_loading)
    View mIncludeLoading;
    Unbinder unbinder;
    InventoryResponse mInventoryResponse;
    int mType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_make_inventory, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MakeInventoryFragmentComponent fragmentComponent = mFragmentBaseComponent.
                makeInventoryFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
        mMakeInventoryFragmentPresenter.attachView(this);
        mIncludeLoading.setVisibility(View.VISIBLE);
        mRvInventory.init(new LinearLayoutManager(getActivity()), this, this);
        UserInfoResponse userInfoResponse = mMakeInventoryFragmentPresenter.loadUser();
        mMakeInventoryAdapter.setCanSeePrice(userInfoResponse.isCanSeePrice());
        mMakeInventoryAdapter.setUserName(userInfoResponse.getUsername());
        mRvInventory.setAdapter(mMakeInventoryAdapter);
        mMakeInventoryAdapter.setOnChildItemClickListener((childView, position) -> {
            if (!SystemUpgradeHelper.getInstance(getActivity()).check(getActivity())){
                return;
            }
            showLoadingDialog();
            mMakeInventoryFragmentPresenter.cancelMakeInventory(mInventoryResponse.getList().get(position).getInventoryID(), OrderState.DRAFT.getName());
        });
        mMakeInventoryAdapter.setOnItemClickListener((view, position) -> {
            InventoryResponse.ListBean listBean = mInventoryResponse.getList().get(position);
            if ("confirm".equals(listBean.getState()) && listBean.getCreateUser().equals(userInfoResponse.getUsername())) {
//                Intent intent = new Intent(mContext, EditRepertoryListActivity.class);
//                intent.putExtra("checkBean", bean);
//                startActivity(intent);
            }
            else {
                startActivity(MakeInventoryDetailActivity.getStartIntent(getActivity(), listBean));
            }
        });
        mType = getArguments().getInt(BUNDLE_KEY_TYPE);
        mMakeInventoryFragmentPresenter.getInventoryList(mPage, PAGE_SIZE, mType);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPage = 1;
        mMakeInventoryFragmentPresenter.getInventoryList(mPage, PAGE_SIZE, mType);
    }
    @Override
    public void onLoadMore() {
        mPage++;
        mMakeInventoryFragmentPresenter.getInventoryList(mPage, PAGE_SIZE, mType);
    }

    @Override
    public void showInventoryList(InventoryResponse inventoryResponse) {
        mInventoryResponse = inventoryResponse;
        mMakeInventoryAdapter.setData(inventoryResponse.getList());
        mIncludeLoading.setVisibility(View.GONE);
        mRvInventory.setRefreshing(false);
        mRvInventory.setLoadingMore(false);
    }

    @Override
    public void showInventoryListError(String error) {
        toast(R.string.toast_get_make_inventory_list_fail);
        mIncludeLoading.setVisibility(View.GONE);
        mRvInventory.setRefreshing(false);
        mRvInventory.setLoadingMore(false);
    }

    @Override
    public void showInventoryListEmpty() {
        mIncludeLoading.setVisibility(View.GONE);
        mRvInventory.setRefreshing(false);
        mRvInventory.setLoadingMore(false);
    }

    @Override
    public void cancelMakeInventory() {
        dismissLoadingDialog();
        toast(R.string.toast_cancel_make_inventory_success);
    }

    @Override
    public void cancelMakeInventoryError(String error) {
        dismissLoadingDialog();
        toast(error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}