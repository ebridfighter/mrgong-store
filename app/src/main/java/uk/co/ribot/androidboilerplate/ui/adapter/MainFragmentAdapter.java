package uk.co.ribot.androidboilerplate.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.co.ribot.androidboilerplate.R;

/**
 * Created by mike on 2017/11/20.
 */

public class MainFragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private Context mContext;

    public MainFragmentAdapter(Context context, FragmentManager fm, List<String> titleList, List<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        mTitleList = titleList;
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable drawable = null;
        switch (position) {
            case 0:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.tab_1_selector);
                break;
            case 1:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.tab_2_selector);
                break;
            case 2:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.tab_3_selector);
                break;
            case 3:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.tab_4_selector);
                break;
            case 4:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.tab_5_selector);
                break;
            default:
                break;
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        SpannableString spannableString = new SpannableString("   " + mTitleList.get(position));
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }
}
