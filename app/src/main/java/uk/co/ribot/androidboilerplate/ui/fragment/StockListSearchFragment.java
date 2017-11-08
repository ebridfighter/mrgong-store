package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * 库存列表搜索
 * 对应单个类别
 *
 * Created by Dong on 2017/11/8.
 */

public class StockListSearchFragment extends AbstractStockListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始为空数据
        clear();
    }

    @Override
    public void refresh(String keyword) {

        //搜索词为空的时候，不显示数据
        if(TextUtils.isEmpty(keyword)){
            mKeyword = keyword;
            //清空,显示空数据
            clear();
            return;
        }

        //搜索词有修改的话，刷新
        if(!mKeyword.equals(keyword)){
            mKeyword = keyword;
            super.refresh(true);
            return;
        }
    }
}
