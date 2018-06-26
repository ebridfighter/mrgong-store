package uk.co.ribot.androidboilerplate.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.OrderState;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.OrderProductsAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;

public class OrderProductFragment extends BaseFragment {

    public static final String BUNDLE_KEY_LIST = "bundle_key_list";
    public static final String BUNDLE_KEY_ORDER_DATA = "bundle_key_order_data";
    public static final String BUNDLE_KEY_CAN_SEE_PRICE = "bundle_key_can_see_price";
    @BindView(R.id.rv_product_list)
    RecyclerView mRvProductList;
    Unbinder unbinder;
    @Inject
    OrderProductsAdapter mOrderProductsAdapter;
    HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    
    OrderListResponse.ListBean mListBean;

    @Inject
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
        final ArrayList<OrderListResponse.ListBean.LinesBean> products = (ArrayList<OrderListResponse.ListBean.LinesBean>) getArguments().getSerializable(BUNDLE_KEY_LIST);
        mListBean = (OrderListResponse.ListBean) getArguments().getSerializable(BUNDLE_KEY_ORDER_DATA);
        mRvProductList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderProductsAdapter.setProducts(products);
        mOrderProductsAdapter.setStatus(mListBean.getState());
        mOrderProductsAdapter.setCanSeePrice(getArguments().getBoolean(BUNDLE_KEY_CAN_SEE_PRICE));
        mOrderProductsAdapter.setOnItemClickListener((view, position) -> {
            //发货状态订单
            if (OrderState.PEISONG.getName().equals(mListBean.getState())) {
                String deliveryType = mListBean.getDeliveryType();
                if (deliveryType.equals(OrderListResponse.ListBean.TYPE_STANDARD) || deliveryType.equals(OrderListResponse.ListBean.TYPE_THIRD_PART_DELIVERY)
                        || deliveryType.equals(OrderListResponse.ListBean.TYPE_FRESH) || deliveryType.equals(OrderListResponse.ListBean.TYPE_FRESH_THIRD_PART_DELIVERY)) {
                    if (!mListBean.getState().equals(OrderState.PEISONG.getName()) && !mListBean.getState().equals(OrderState.DONE.getName()) && !mListBean.getState().equals(OrderState.RATED.getName())) {
                        return;
                    }
                }
                if (deliveryType.equals(OrderListResponse.ListBean.TYPE_FRESH_VENDOR_DELIVERY) || deliveryType.equals(OrderListResponse.ListBean.TYPE_VENDOR_DELIVERY)) {
                    if ((mListBean.getState().equals(OrderState.DONE.getName()) || mListBean.getState().equals(OrderState.RATED.getName())) && (products.get(position).getLotList() != null && products.get(position).getLotList().size() == 0)) {
                       toast(R.string.toast_product_without_batch);
                        return;
                    }
                    if (mListBean.getState().equals(OrderState.PEISONG.getName()) || mListBean.getState().equals(OrderState.DRAFT.getName()) || mListBean.getState().equals(OrderState.SALE.getName())) {
                        return;
                    }
                }
//                    Intent intent = new Intent(context, LotListActivity.class);
//                    intent.putExtra("title", basicBean.getName());
//                    intent.putExtra("bean", (Parcelable) bean);
//                    context.startActivity(intent);
            }
        });
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mOrderProductsAdapter);
        mRvProductList.setAdapter(mHeaderAndFooterWrapper);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
