package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.io.Serializable;
import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;

/**
 * Created by mike on 2017/9/29.
 */

public class ProductListResponse implements Serializable {

    private List<ProductBean> products;
    int version;
    List<CategoryBean> category;
    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public class CategoryBean implements Serializable{
        private String categoryParent;
        private List<String> categoryChild;

        public String getCategoryParent() {
            return categoryParent;
        }

        public void setCategoryParent(String categoryParent) {
            this.categoryParent = categoryParent;
        }
        public List<String> getCategoryChild() {
            return categoryChild;
        }

        public void setCategoryChild(List<String> categoryChild) {
            this.categoryChild = categoryChild;
        }
    }

}
