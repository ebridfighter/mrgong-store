package uk.co.ribot.androidboilerplate.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.runwise.commomlibrary.swipetoloadlayout.OnRefreshListener;
import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;
import com.runwise.commomlibrary.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.StockItem;
import uk.co.ribot.androidboilerplate.data.remote.RunwiseService;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.fresco.FrecoFactory;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.StockListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StockListMvpView;
import uk.co.ribot.androidboilerplate.util.DateFormateUtil;

/**
 * 库存列表fragment
 * 一个fragment对应一个category
 *
 * Created by Dong on 2017/11/1.
 */
public class AbstractStockListFragment extends BaseFragment implements StockListMvpView,OnRefreshListener{

    public static final String ARG_CATEGORY = "arg_category";
    private static final int LIMIT = 50;
    private View mRootView;

    @Inject
    StockListPresenter mStockListPresenter;
    @BindView(R.id.rv_stock_list)
    RefreshRecyclerView mRvStockList;
    StockListAdapter mStockListAdapter = new StockListAdapter();
    Unbinder unbinder;
    private List<StockItem> mStockList = new ArrayList<>();
    protected String mCategory;
    protected int pz = 1;
    protected int limit = LIMIT;
    protected String mKeyword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_stock_list,container,false);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCategory = getArguments().getString(ARG_CATEGORY);
        mFragmentBaseComponent
                .stockListFragmentComponent(new ActivityModule(getActivity()))
                .inject(this);
        mStockListPresenter.attachView(this);
    }

    protected void initView(){
        mRvStockList.init(new LinearLayoutManager(getActivity()),this, this);
        mRvStockList.setRefreshEnabled(true);
        mRvStockList.setAdapter(mStockListAdapter);
        mRvStockList.setLoadingMoreEnable(true);
    }

    /**
     * 当转换为当前tab的时候，被StockListContainerFragment调用
     */
    public void refresh(String keyword){}

    /**
     * 刷新
     * @param showLoading 显示loading layout
     */
    protected void refresh(boolean showLoading){
        pz = 1;
        mStockListPresenter.getStocks(mCategory,pz,limit,mKeyword,true);
    }

    /**
     * 清空数据并显示无数据页面
     */
    protected void clear(){
        mStockListPresenter.cancel();
        mStockList.clear();
        mStockListAdapter.notifyDataSetChanged();
//        loadingLayout.onSuccess(adapter.getCount(), "哎呀！这里是空哒~~", R.drawable.default_icon_goodsnone);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        refresh(false);//下拉刷新不用显示loading layout
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        pz++;
        mStockListPresenter.getStocks(mCategory,pz,limit,mKeyword,false);
    }

    @Override
    public void showStocks(List<StockItem> stockItemList, boolean isRefresh) {
        if(isRefresh){
            mStockList.clear();
        }
        mStockList.addAll(stockItemList);
        mStockListAdapter.notifyDataSetChanged();
        mRvStockList.setRefreshing(false);
        mRvStockList.setLoadingMore(false);
    }

    @Override
    public void showNoStocks() {
        mRvStockList.showEmpty(null);
    }

    @Override
    public void showNoMoreStocks() {
        mRvStockList.setLoadingMoreEnable(false);
    }

    @Override
    public void showError(String msg) {
        //TODO
    }

    /*
     * 列表Adapter
     */
    private class StockListAdapter extends RecyclerView.Adapter<ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ViewHolder(inflater.inflate(R.layout.item_stock,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final StockItem bean = mStockList.get(position);
            StockItem.Product productBean = bean.getProduct();
            if (productBean != null) {
                if (!TextUtils.isEmpty(mKeyword)) {
                    int index = productBean.getName().indexOf(mKeyword);
                    if (index != -1) {
                        SpannableString spannStr = new SpannableString(productBean.getName());
                        spannStr.setSpan(new ForegroundColorSpan(Color.parseColor("#6bb400")), index, index + mKeyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.name.setText(spannStr);
                    }
                } else {
                    viewHolder.name.setText(productBean.getName());
                }
                viewHolder.number.setText(productBean.getDefaultCode() + " | ");
                viewHolder.content.setText(productBean.getUnit());
                if (productBean.getImage() !=null){
                    FrecoFactory.getInstance(getActivity()).disPlay(viewHolder.sDv, RunwiseService.ENDPOINT + productBean.getImage().getImageSmall());
                }
            }
            viewHolder.value.setText(NumberUtil.getIOrD(String.valueOf(bean.getQty())));
            viewHolder.uom.setText(bean.getUom());
            if (TextUtils.isEmpty(bean.getLotNum())){
                viewHolder.dateNumber.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.dateNumber.setText(bean.getLotNum());
                viewHolder.dateNumber.setVisibility(View.VISIBLE);
            }
            viewHolder.dateLate.setText(DateFormateUtil.getLaterFormat(bean.getLifeEndDate()));
        }

        @Override
        public int getItemCount() {
            return mStockList.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.productImage)
        SimpleDraweeView sDv;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.value)
        TextView value;
        @BindView(R.id.uom)
        TextView uom;
        @BindView(R.id.dateNumber)
        TextView dateNumber;
        @BindView(R.id.dateLate)
        TextView dateLate;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
