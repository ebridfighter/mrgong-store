package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.database.CategoryBean;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

public interface PlaceOrderProductListImproveMvpView extends MvpView {
    void showCategorys(List<CategoryBean> categoryBeans);

    void showCategorysError();
    void showCategorysEmpty();

}
