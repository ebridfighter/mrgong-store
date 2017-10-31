package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;

/**
 * Created by mike on 2017/10/31.
 * 消息Frgament
 */

public class MessageFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_message,null);
        return view;
    }
}
