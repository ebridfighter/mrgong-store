package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;

/**
 * Created by mike on 2017/10/31.
 * 库存
 */

public class StockFragment extends BaseFragment {

    @Inject
    public StockFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock,null);
        return view;
    }
}
