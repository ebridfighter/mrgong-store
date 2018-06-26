package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.business.StockItem;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * 库存列表
 *
 * Created by Dong on 2017/11/1.
 */

public interface StockListMvpView extends MvpView {

    /**
     * 显示库存
     */
    void showStocks(List<StockItem> stockItemList, boolean isRefresh);

    /**
     * 没有数据
     */
    void showNoStocks();

    /**
     * 下拉没有更多了
     */
    void showNoMoreStocks();

    void showError(String msg);
}
