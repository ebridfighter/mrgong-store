package uk.co.ribot.androidboilerplate.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import uk.co.ribot.androidboilerplate.R;

/**
 * Created by Dong on 2017/10/19.
 */

public class SystemUpgradeHelper {
    private static final String PREF_NAME = "_PREF_SYSTEM_UPGRADE_";
    private static final String KEY_DATAID = "key_dataid";
    private static final String KEY_START_TIMESTAMP = "key_start_timestamp";
    private static final String KEY_END_TIMESTAMP = "key_end_timestamp";
    private SharedPreferences preferences;
    private static SystemUpgradeHelper instance;

    public static synchronized SystemUpgradeHelper getInstance(Context context){
        if(instance==null){
            instance = new SystemUpgradeHelper(context);
        }
        return instance;
    }

    private SystemUpgradeHelper(Context context){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void create(String dataid){
        String[] times = dataid.split("-");
        long startTimeStamp = Long.valueOf(getLongString(times[0]));
        long endTimeStamp = Long.valueOf(getLongString(times[1]));
        preferences.edit().clear()
                .putString(KEY_DATAID,dataid)
                .putLong(KEY_START_TIMESTAMP,startTimeStamp)
                .putLong(KEY_END_TIMESTAMP,endTimeStamp)
                .apply();
    }

    private String getLongString(String str){
        int dotIndex = str.indexOf(".");
        if(dotIndex>=0){
            return str.substring(0,dotIndex);
        }
        return str;
    }

    public boolean isUpgradingTime(){
        long current = System.currentTimeMillis()/1000;
        long startTime = getStartTime();
        long endTime = getEndTime();
        return current > startTime && current < endTime;
    }

    public long getStartTime(){
        return preferences.getLong(KEY_START_TIMESTAMP,0);
    }

    public long getEndTime(){
        return preferences.getLong(KEY_END_TIMESTAMP,0);
    }

    public boolean needShowNotice(String pageName){
        boolean iii = !preferences.getBoolean(pageName+"_is_read",false);
        Log.d("haha","current:"+ System.currentTimeMillis()/1000+" end:"+getEndTime()+" "+iii);
        return System.currentTimeMillis()/1000 < getEndTime() && !preferences.getBoolean(pageName+"_is_read",false);
    }

    public void setIsRead(String pageName){
        preferences.edit().putBoolean(pageName+"_is_read",true).apply();
    }

    public boolean check(Context context){
        if(!isUpgradingTime())return true;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Toast.makeText(context,context.getString(R.string.system_upgrading_toast,sdf.format(getEndTime()*1000)), Toast.LENGTH_LONG).show();
        return false;
    }
}
