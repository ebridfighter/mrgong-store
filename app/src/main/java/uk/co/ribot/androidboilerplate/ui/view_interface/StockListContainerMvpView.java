package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * 库存View
 *
 * Created by Dong on 2017/11/8.
 */

public interface StockListContainerMvpView extends MvpView {

    /**
     * 展示类别
     * @param categoryList
     */
    void showCategories(List<String> categoryList);
}
