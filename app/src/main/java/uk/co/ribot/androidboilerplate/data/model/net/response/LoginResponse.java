package uk.co.ribot.androidboilerplate.data.model.net.response;

/**
 * Created by mike on 2017/9/28.
 */

public class LoginResponse {
    private String isSuccess;
    private String mobile;
    private UserInfoResponse user;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public UserInfoResponse getUser() {
        return user;
    }

    public void setUser(UserInfoResponse user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
