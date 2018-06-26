package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.util.List;

/**
 * 商品类别
 *
 * Created by mike on 2017/9/7.
 */

public class CategoryResponse {
    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    List<String> categoryList;
}
