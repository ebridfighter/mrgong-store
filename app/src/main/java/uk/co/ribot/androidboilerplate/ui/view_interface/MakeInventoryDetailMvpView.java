package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.database.Product;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/28.
 */
public interface MakeInventoryDetailMvpView extends MvpView {
    void showCategorys(CategoryResponse categoryResponse);

    void showProductsEmpty();

    void showProducts(List<Product> products);

    void showProductsError(String error);
}
