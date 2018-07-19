package uk.co.ribot.androidboilerplate.view;


import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.ui.adapter.ProductListAdapter;
import uk.co.ribot.androidboilerplate.ui.adapter.TypeAdapter;
import uk.co.ribot.androidboilerplate.ui.base.ProductCountSetter;
import uk.co.ribot.androidboilerplate.util.CommonUtils;
import uk.co.ribot.androidboilerplate.util.ToastUtil;

public class LinkageListContainer extends LinearLayout {

    public TypeAdapter typeAdapter;

    private ListView mProductListView;
    private List<ProductBean> mProductBeanList;
    private List<String> mCategoryList;
    OperationWidget.OnClick mOnClick;
    private boolean move;
    private int index;
    private Context mContext;

    public ListView getProductListView() {
        return mProductListView;
    }
    public ProductListAdapter getProductListAdapter() {
        return mProductListAdapter;
    }

    public void setProductListAdapter(ProductListAdapter productListAdapter) {
        mProductListAdapter = productListAdapter;
    }

    public ProductListAdapter mProductListAdapter;
    private TextView tvStickyHeaderView;
    private View stickView;

    private float MILLISECONDS_PER_INCH = 0.01f;  //修改可以改变数据,越大速度越慢

    public LinkageListContainer(Context context) {
        super(context);
    }

    public void init(String category, List<ProductBean> foodBeanList, List<String> categoryList, OperationWidget.OnClick onClick,ProductCountSetter productCountSetter) {
        this.mProductBeanList = foodBeanList;
        mCategoryList = categoryList;
        mOnClick = onClick;
        typeAdapter = new TypeAdapter(categoryList, category);
        RecyclerView recyclerView1 = findViewById(R.id.recycler1);
        mProductListView = findViewById(R.id.recycler2);
        if (mCategoryList == null||mCategoryList.size() == 0) {
            recyclerView1.setVisibility(View.GONE);
            findViewById(R.id.stick_header).setVisibility(View.GONE);
        }
        if (foodBeanList.size() == 0) {
            mProductListView.setVisibility(View.GONE);
            return;
        }
        recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
        View view = new View(mContext);
        view.setMinimumHeight(CommonUtils.dip2px(mContext, 50));
        typeAdapter.addFooterView(view);
        typeAdapter.bindToRecyclerView(recyclerView1);
        recyclerView1.addItemDecoration(new SimpleDividerDecoration(mContext));
        ((DefaultItemAnimator) recyclerView1.getItemAnimator()).setSupportsChangeAnimations(false);
        List<ProductBean> finalFoodBeanList = foodBeanList;
        recyclerView1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (!scrollFlag) {
                    typeAdapter.fromClick = true;
                    typeAdapter.setChecked(i);
                    String type = view.getTag().toString();
                    boolean findIt = false;
                    for (int ii = 0; ii < finalFoodBeanList.size(); ii++) {
                        ProductBean typeBean = finalFoodBeanList.get(ii);
                        if (typeBean.getCategoryChild().equals(type)) {
                            findIt = true;
                            index = ii;
                            moveToPosition(index);
                            break;
                        }
                    }
                    if (!findIt) {
                        ToastUtil.show(getContext(), "该分类下没有商品!");
                    }
                }
            }
        });
        setUpProductRecyclerView();
        mProductListAdapter.setProductCountSetter(productCountSetter);
    }


    public LinkageListContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate(mContext, R.layout.view_listcontainer, this);
    }

    private void moveToPosition(int position) {
        mProductListView.setSelection(position);
    }

    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition;// 标记上次滑动位置
    public void setUpProductRecyclerView() {
        mProductListAdapter = new ProductListAdapter(mProductBeanList,mOnClick,false);
        stickView = findViewById(R.id.stick_header);
        mProductListView.setAdapter(mProductListAdapter);
        tvStickyHeaderView = (TextView) findViewById(R.id.tv_header);
        tvStickyHeaderView.setText(mProductBeanList.get(0).getCategoryChild());
        mProductListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                typeAdapter.fromClick = false;
                return false;
            }
        });
        mProductListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        scrollFlag = true;
                        break;
                    case SCROLL_STATE_FLING:
                        scrollFlag = false;
                        break;
                    case SCROLL_STATE_IDLE:
                        scrollFlag = false;
                        break;
                    default:
                        break;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.DONUT)
            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    View stickyInfoView = findChildViewUnder(listView, stickView.getMeasuredWidth() / 2, 5);
                    if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                        tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
                        typeAdapter.setType(String.valueOf(stickyInfoView.getContentDescription()));
                    }

                    View transInfoView = findChildViewUnder(listView, stickView.getMeasuredWidth() / 2, stickView.getMeasuredHeight
                            () + 1);
                    if (transInfoView != null && transInfoView.getTag() != null) {
                       View positionView = transInfoView.findViewById(R.id.food_main);
                        int transViewStatus = (int) positionView.getTag();
                        int dealtY = transInfoView.getTop() - stickView.getMeasuredHeight();
                        if (transViewStatus == ProductListAdapter.HAS_STICKY_VIEW) {
                            if (transInfoView.getTop() > 0) {
                                stickView.setTranslationY(dealtY);
                            } else {
                                stickView.setTranslationY(0);
                            }
                        } else if (transViewStatus == ProductListAdapter.NONE_STICKY_VIEW) {
                            stickView.setTranslationY(0);
                        }
                    }
//                }
            }
        });
    }

    public View findChildViewUnder(AbsListView listView, float x, float y) {
        final int count = listView.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = listView.getChildAt(i);
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);
            //判断该点是否在childView的范围内
            if (x >= child.getLeft() + translationX &&
                    x <= child.getRight() + translationX &&
                    y >= child.getTop() + translationY &&
                    y <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }


    public TypeAdapter getTypeAdapter() {
        return typeAdapter;
    }

    public void setTypeAdapter(TypeAdapter typeAdapter) {
        this.typeAdapter = typeAdapter;
    }
}
