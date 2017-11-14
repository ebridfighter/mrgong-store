package uk.co.ribot.androidboilerplate.data.model.net.request;

import java.util.List;

/**
 * Created by libin on 2017/7/9.
 */

public class CommitOrderRequest {

    /**
     * estimated_time : 2017-02-22 05:42:44
     * order_type_id : 121
     * products : [{"qty":3,"product_id":101}]
     */

    private String estimated_time;
    private List<ProductsBean> products;
    private String order_type_id;
    public String getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(String estimated_time) {
        this.estimated_time = estimated_time;
    }

    public String getOrder_type_id() {
        return order_type_id;
    }

    public void setOrder_type_id(String order_type_id) {
        this.order_type_id = order_type_id;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ProductsBean {
        /**
         * qty : 3
         * product_id : 101
         */

        private int qty;
        private int product_id;

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }
    }
}
