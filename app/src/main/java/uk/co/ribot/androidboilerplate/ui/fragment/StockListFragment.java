package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Dong on 2017/11/8.
 */

public class StockListFragment extends AbstractStockListFragment {
    public static final String ARG_CURRENT = "arg_current";
    private static final int PAGE_LIMIT = 500;
    private boolean isFirstLoaded = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
