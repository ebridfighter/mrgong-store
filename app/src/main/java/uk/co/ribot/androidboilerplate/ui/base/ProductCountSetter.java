package uk.co.ribot.androidboilerplate.ui.base;

import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;

public interface ProductCountSetter {
    void setCount(ProductBean bean, double count);


    double getCount(ProductBean bean);

}
