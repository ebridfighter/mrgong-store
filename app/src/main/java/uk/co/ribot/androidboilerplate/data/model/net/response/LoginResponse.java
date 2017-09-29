package uk.co.ribot.androidboilerplate.data.model.net.response;

import uk.co.ribot.androidboilerplate.data.model.UserInfo;

/**
 * Created by mike on 2017/9/28.
 */

public class LoginResponse {
    private String isSuccess;
    private String mobile;
    private UserInfo user;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
