package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProcurementResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.ProcurementFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.ProcurementListAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.ProcurementFragmentPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ProcurementFragmentMvpView;

/**
 * Created by mike on 2018/1/4.
 */
public class ProcurementFragment extends BaseFragment implements ProcurementFragmentMvpView {
    @Inject
    ProcurementFragmentPresenter mProcurementFragmentPresenter;
    @Inject
    ProcurementListAdapter mProcurementListAdapter;
    Unbinder mUnbinder;
    public static final String ARGUMENT_KEY_TYPE = "argument_key_type";
    @BindView(R.id.rv_product)
    RefreshRecyclerView mRvProduct;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.id_tv_loadingmsg)
    TextView mIdTvLoadingmsg;
    @BindView(R.id.include_loading)
    View mIncludeLoading;
    private List<ProcurementResponse.ListBean.ProductsBean> dataList = new ArrayList<>();
    ArrayList<Integer> mSelection = new ArrayList<>();
    HashMap<Integer,ProcurementResponse.ListBean> mHeadMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_procurement, null);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProcurementFragmentComponent fragmentComponent = mFragmentBaseComponent.
                procurementFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
        mProcurementFragmentPresenter.attachView(this);
        mProcurementFragmentPresenter.getZiCaiList(getArguments().getInt(ARGUMENT_KEY_TYPE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProcurementFragmentPresenter.detachView();
    }

    @Override
    public void showList(ProcurementResponse procurementResponse) {
        for (ProcurementResponse.ListBean listBean : procurementResponse.getList()){
            mSelection.add(dataList.size());
            mHeadMap.put(dataList.size(),listBean);
            for (ProcurementResponse.ListBean.ProductsBean productsBean:listBean.getProducts()){
                dataList.add(productsBean);
            }
        }
        mProcurementListAdapter.setData(dataList);
        mProcurementListAdapter.setHeadMap(mHeadMap);
        mProcurementListAdapter.setSelection(mSelection);

    }

    @Override
    public void showError(String message) {
        toast(message);
        mIncludeLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}