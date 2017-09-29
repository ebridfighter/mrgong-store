package uk.co.ribot.androidboilerplate.data.model.net.request;

/**
 * Created by mike on 2017/9/28.
 */

public class LoginRequest{
    private String login;//	varchar	N		手机号
    private String password;//	int	Y		登录类型: 0手机号; 1qq; 2新浪微博; 3微信
    private String registrationID;

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
