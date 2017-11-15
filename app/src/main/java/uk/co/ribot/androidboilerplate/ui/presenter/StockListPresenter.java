package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.business.StockItem;
import uk.co.ribot.androidboilerplate.data.model.net.response.StockListResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StockListMvpView;

/**
 * 库存列表presenter
 *
 * Created by Dong on 2017/11/1.
 */
public class StockListPresenter extends BasePresenter<StockListMvpView> {

    private final DataManager mDataManager;
    private Subscription mGetStockSubscription;

    @Inject
    public StockListPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(StockListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     * 刷新
     */
    public void getStocks(String category, int pz, final int limit, String keyword, final boolean isRefresh){

        if(mGetStockSubscription!=null)mGetStockSubscription.unsubscribe();
        mGetStockSubscription = mDataManager.getStockList(pz,limit,category,keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<StockListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e.getMessage());//TODO
                    }

                    @Override
                    public void onNext(StockListResponse stockListResponse) {
                        List<StockItem> stockItemList = stockListResponse.getList();
                        if(stockItemList.size()==0){
                            if(isRefresh)getMvpView().showNoStocks();//没有数据
                            else getMvpView().showNoMoreStocks();//没有更多
                        }else if(stockItemList.size()<limit){//没有更多
                            getMvpView().showStocks(stockItemList,isRefresh);
                            getMvpView().showNoMoreStocks();
                        }else{
                            getMvpView().showStocks(stockItemList,isRefresh);
                        }
                    }
                });
    }

    /**
     * 取消请求
     */
    public void cancel(){
        if(mGetStockSubscription!=null)mGetStockSubscription.unsubscribe();
    }

}
