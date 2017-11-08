package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;

/**
 * Created by Dong on 2017/11/8.
 */

public class StockListFragment extends AbstractStockListFragment {
    public static final String ARG_CURRENT = "arg_current";
    private static final int PAGE_LIMIT = 500;
    private boolean isFirstLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().getBoolean(ARG_CURRENT,false)){
            isFirstLoaded = true;
            refresh(true);
        }
    }

    /**
     * 首次点击时刷新
     * @param keyword 搜索词
     */
    @Override
    public void refresh(String keyword){
        if(!isFirstLoaded){
            isFirstLoaded = true;
            refresh(true);
            return;
        }
    }
}
