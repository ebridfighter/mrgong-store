package uk.co.ribot.androidboilerplate.data.model.net.httpstatus;

/**
 * Created by mike on 2017/10/11.
 */

public class HttpStatus {
    private String state;
    private String error;
    private static final String SUCCESS_CODE = "A0006";

    public HttpStatus(String state, String error) {
        this.state = state;
        this.error = error;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return !SUCCESS_CODE.equals(state);
    }
}
