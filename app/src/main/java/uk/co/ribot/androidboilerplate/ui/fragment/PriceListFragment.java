package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.injection.component.frament.PriceListFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.ProductsAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.PriceListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.PriceListMvpView;

/**
 * Created by mike on 2017/11/24.
 */
public class PriceListFragment extends BaseFragment implements PriceListMvpView {
    public static final String BUNDLE_KEY_LIST = "bundle_key_list";
    public static final String BUNDLE_KEY_CAN_SEE_PRICE = "bundle_key_can_see_price";
    @Inject
    PriceListPresenter mPriceListPresenter;
    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    @Inject
    ProductsAdapter mProductsAdapter;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_price_list, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PriceListFragmentComponent fragmentComponent = mFragmentBaseComponent.
                priceListFragmentComponent(new ActivityModule(getActivity()));
        fragmentComponent.inject(this);
        mRvProduct.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));
        mRvProduct.getItemAnimator().setAddDuration(500);
        mRvProduct.getItemAnimator().setRemoveDuration(500);
        mRvProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvProduct.setAdapter(mProductsAdapter);
        mProductsAdapter.setRibots((List<ProductListResponse.Product>) getArguments().getSerializable(BUNDLE_KEY_LIST));
        mProductsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showProducts(List<ProductListResponse.Product> products) {

    }

    @Override
    public void showProductsEmpty() {

    }

    @Override
    public void showError() {

    }
}