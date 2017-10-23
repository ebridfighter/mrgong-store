package uk.co.ribot.androidboilerplate.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import uk.co.ribot.androidboilerplate.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";
    public static final String PREF_KEY_DATABASE = "pref_key_database";
    public static final String PREF_KEY_COOKIES = "pref_key_cookies";
    public static final String DEFAULT_DATABASE_NAME = "LBZTest1012";


    private  static SharedPreferences mPref = null;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(getCookie());
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
     * 得到当前的cookies
     *
     * @return
     */
    public static String getCookie() {
        return mPref.getString(PREF_KEY_COOKIES, "");
    }

    /**
     * 设置当前的cookies
     * @return
     */
    public static void setCookie(String cookie) {
        mPref.edit().putString(PREF_KEY_COOKIES,cookie).commit();
    }


    public void clear() {
        mPref.edit().clear().apply();
    }

}
