package uk.co.ribot.androidboilerplate.data.model.net.exception;

/**
 * Created by mike on 2017/10/11.
 */

public class ApiException extends RuntimeException {
    private int mErrorCode;
    public static final int CODE_LOGOUT = 100;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isSessionExpried() {
        return mErrorCode == CODE_LOGOUT;
    }
}
