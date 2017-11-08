package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runwise.commomlibrary.swipetoloadlayout.OnRefreshListener;
import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.StockItem;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.StockListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StockListMvpView;

/**
 * 库存列表fragment
 * 一个fragment对应一个category
 *
 * Created by Dong on 2017/11/1.
 */
public class AbstractStockListFragment extends BaseFragment implements StockListMvpView,OnRefreshListener{

    public static final String ARG_CATEGORY = "arg_category";
    private static final int LIMIT = 500;
    private View mRootView;

    @Inject
    StockListPresenter mStockListPresenter;
    @BindView(R.id.rv_stock_list)
    RefreshRecyclerView mRvStockList;
    StockListAdapter mStockListAdapter = new StockListAdapter();
    Unbinder unbinder;
    private List<StockItem> mStockList = new ArrayList<>();
    protected String mCategory;
    protected int pz = 0;
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
        pz = 0;
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
        int lastEndPos = mStockList.size();
        mStockList.addAll(stockItemList);
        mStockListAdapter.notifyItemRangeInserted(lastEndPos,stockItemList.size());
    }

    @Override
    public void showNoStocks() {
        mRvStockList.showEmpty(null);
    }

    @Override
    public void showNoMoreStocks() {

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
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
