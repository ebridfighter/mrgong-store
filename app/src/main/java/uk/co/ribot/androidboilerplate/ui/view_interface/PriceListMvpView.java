package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/24.
 */
public interface PriceListMvpView extends MvpView {
    void showProducts(List<ProductBean> products);

    void showProductsEmpty();

    void showError();
}
