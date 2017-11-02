package uk.co.ribot.androidboilerplate.ui.view_interface;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * 库存列表
 *
 * Created by Dong on 2017/11/1.
 */

public interface StockListMvpView extends MvpView {
    /**
     * 显示刷新的库存
     */
    void showRefreshStocks();

    /**
     * 显示加载更多的库存
     */
    void showMoreStocks();

    void showError();
}
