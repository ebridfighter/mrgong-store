package uk.co.ribot.androidboilerplate.data.model.net.request;

/**
 * 获取类别请求
 * Created by mike on 2017/9/7.
 */

public class GetCategoryRequest {

    public GetCategoryRequest(){};

    public GetCategoryRequest(int uid){
        user_id = uid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    int user_id;
}
