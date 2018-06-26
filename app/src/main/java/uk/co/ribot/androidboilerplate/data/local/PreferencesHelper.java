package uk.co.ribot.androidboilerplate.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.ApplicationContext;
import uk.co.ribot.androidboilerplate.util.ObjectTransformUtil;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_runwise_pref_file";
    public static final String PREF_KEY_DATABASE = "pref_key_database";
    public static final String PREF_KEY_HOST = "pref_key_host";
    public static final String PREF_KEY_COOKIES = "pref_key_cookies";
    public static final String PREF_KEY_USER_INFO = "pref_key_user_info";
    public static final String PREF_KEY_PRODUCT_LIST_VERSION = "pref_key_product_list_version";
    public static final String DEFAULT_DATABASE_NAME = "gethost";
    public static final String DEFAULT_HOST = "http://gethost.runwise.cn";


    private static SharedPreferences mPref = null;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLogin() {
        String cookie = getCookie();
        return !TextUtils.isEmpty(cookie);
    }

    /**
     * 得到当前数据库
     *
     * @return
     */
    public static String getCurrentDataBaseName() {
        return mPref.getString(PREF_KEY_DATABASE, DEFAULT_DATABASE_NAME);
    }

    /**
     * 设置当前数据库
     *
     * @return
     */
    public static void setCurrentDataBaseName(String dataBaseName) {
        mPref.edit().putString(PREF_KEY_DATABASE, dataBaseName).commit();
    }

    /**
     * 得到当前的cookies
     *
     * @return
     */
    public static String getCookie() {
        return mPref.getString(PREF_KEY_COOKIES, "");
    }

    /**
     * 设置当前的cookies
     *
     * @return
     */
    public static void setCookie(String cookie) {
        mPref.edit().putString(PREF_KEY_COOKIES, cookie).commit();
    }

    public static void setUserInfo(UserInfoResponse userInfo) {
        mPref.edit().putString(PREF_KEY_USER_INFO, ObjectTransformUtil.toString(userInfo)).commit();
    }

    public static UserInfoResponse getUserInfo() {
        String userInfoStr = mPref.getString(PREF_KEY_USER_INFO, "");
        return (UserInfoResponse) ObjectTransformUtil.toObject(userInfoStr, UserInfoResponse.class);
    }

    public static void setProductListVersion(int version){
        mPref.edit().putInt(PREF_KEY_PRODUCT_LIST_VERSION, version).commit();
    }

    public static int getProductListVersion(){
        int version = mPref.getInt(PREF_KEY_PRODUCT_LIST_VERSION, 0);
        return version;
    }

    public static String getHost() {
        return mPref.getString(PREF_KEY_HOST, DEFAULT_HOST);
    }

    public static void setHost(String host) {
        mPref.edit().putString(PREF_KEY_HOST, host).commit();
    }


    public void clear() {
        mPref.edit().clear().apply();
    }

}
