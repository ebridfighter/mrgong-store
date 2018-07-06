package uk.co.ribot.androidboilerplate.ui.base;

import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;

public interface ProductCountSetter {
    void setCount(ProductBean bean, double count);

    void setRemark(ProductBean bean);

    double getCount(ProductBean bean);

    String getRemark(ProductBean bean);
}
