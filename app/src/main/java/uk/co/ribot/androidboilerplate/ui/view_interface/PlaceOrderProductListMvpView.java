package uk.co.ribot.androidboilerplate.ui.view_interface;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by mike on 2017/11/16.
 */

public interface PlaceOrderProductListMvpView extends MvpView{
    void showCategorys(CategoryResponse categoryResponse);

    void showProducts(List<ProductListResponse.Product> products);

    void showProductsEmpty();

    void showError();
}
