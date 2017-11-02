package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StockListMvpView;

/**
 * 库存列表presenter
 *
 * Created by Dong on 2017/11/1.
 */
public class StockListPresenter extends BasePresenter<StockListMvpView> {

    private final DataManager mDataManager;

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
    public void refresh(){

    }

    /**
     * 加载更多
     */
    public void loadMore(int pageIndex,int pageNum){

    }
}
