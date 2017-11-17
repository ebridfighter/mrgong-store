package uk.co.ribot.androidboilerplate.data.model.business;

import java.io.Serializable;

import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;

/**
 * Created by libin on 2017/7/10.
 */

public class AddedProduct implements Serializable {
    private String productId;       //当前购物车里面产品id
    private int count;              //当前购物车里面件数

    public ProductListResponse.Product getProduct() {
        return product;
    }

    public void setProduct(ProductListResponse.Product product) {
        this.product = product;
    }

    ProductListResponse.Product product;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AddedProduct(String id, int count){
        productId = id;
        this.count = count;
    }

    public AddedProduct(){}




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddedProduct product = (AddedProduct) o;

        return productId != null ? productId.equals(product.productId) : product.productId == null;

    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }
}
