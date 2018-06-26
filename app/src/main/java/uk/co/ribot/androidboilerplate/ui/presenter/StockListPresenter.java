package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.business.StockItem;
import uk.co.ribot.androidboilerplate.data.model.net.response.StockListResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StockListMvpView;

/**
 * 库存列表presenter
 * <p>
 * Created by Dong on 2017/11/1.
 */
public class StockListPresenter extends BasePresenter<StockListMvpView> {

    private final DataManager mDataManager;
    private Subscription mGetStockSubscription;

    @Inject
    public StockListPresenter(DataManager dataManager) {
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
    public void getStocks(String category, int pz, final int limit, String keyword, final boolean isRefresh) {
        mDataManager.getStockList(pz, limit, category, keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<StockListResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e.getMessage());//TODO
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StockListResponse stockListResponse) {
                        List<StockItem> stockItemList = stockListResponse.getList();
                        if (stockItemList.size() == 0) {
                            if (isRefresh) getMvpView().showNoStocks();//没有数据
                            else getMvpView().showNoMoreStocks();//没有更多
                        } else if (stockItemList.size() < limit) {//没有更多
                            getMvpView().showStocks(stockItemList, isRefresh);
                            getMvpView().showNoMoreStocks();
                        } else {
                            getMvpView().showStocks(stockItemList, isRefresh);
                        }
                    }
                });
    }

    /**
     * 取消请求
     */
    public void cancel() {
        if (mGetStockSubscription != null) mGetStockSubscription.unsubscribe();
    }

}
