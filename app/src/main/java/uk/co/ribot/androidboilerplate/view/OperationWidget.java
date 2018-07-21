package uk.co.ribot.androidboilerplate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.runwise.commomlibrary.util.NumberUtil;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;

public class OperationWidget extends FrameLayout {

    private View sub;
    private TextView tv_count;
    private double count;
    private AddButton addbutton;
    private boolean sub_anim, circle_anim;
    private ProductBean mProductBean;

    public interface OnClick {

        void onAddClick(View view, ProductBean productBean,double count);

        void onSubClick(ProductBean productBean,double count);
    }
    private OnClick onAddClick;

    public OperationWidget(@NonNull Context context) {
        super(context);
    }

    public OperationWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_operation_widget, this);
        addbutton = findViewById(R.id.addbutton);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OperationWidget);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.OperationWidget_circle_anim:
                    circle_anim = a.getBoolean(R.styleable.OperationWidget_circle_anim, false);
                    break;
                case R.styleable.OperationWidget_sub_anim:
                    sub_anim = a.getBoolean(R.styleable.OperationWidget_sub_anim, false);
                    break;
            }

        }
        a.recycle();
        sub = findViewById(R.id.iv_sub);
        tv_count = findViewById(R.id.tv_count);
        addbutton.setAnimListener(new AddButton.AnimListener() {
            @Override
            public void onStop() {
                if (count == 0f) {
                    ViewAnimator.animate(sub)
                            .translationX(addbutton.getLeft() - sub.getLeft(), 0)
                            .rotation(360)
                            .alpha(0, 255)
                            .duration(300)
                            .interpolator(new DecelerateInterpolator())
                            .andAnimate(tv_count)
                            .translationX(addbutton.getLeft() - tv_count.getLeft(), 0)
                            .rotation(360)
                            .alpha(0, 255)
                            .interpolator(new DecelerateInterpolator())
                            .duration(300)
                            .start();
                }
                count++;
                tv_count.setText(NumberUtil.getIOrD(count));
                if (onAddClick != null) {
                    onAddClick.onAddClick(addbutton, mProductBean,count);
                }
            }
        });
        sub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0f) {
                    return;
                }
                if (count == 1f && sub_anim) {
                    subAnim();
                }
                count--;
                tv_count.setText(count == 0f ? "1" : NumberUtil.getIOrD(count));
                if (onAddClick != null) {
                    onAddClick.onSubClick(mProductBean,count);
                }
            }
        });
    }
    private void subAnim(){
        ViewAnimator.animate(sub)
                .translationX(0, addbutton.getLeft() - sub.getLeft())
                .rotation(-360)
                .alpha(255, 0)
                .duration(300)
                .interpolator(new AccelerateInterpolator())
                .andAnimate(tv_count)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        if (circle_anim) {
                            addbutton.expendAnim();
                        }
                    }
                })
                .translationX(0, addbutton.getLeft() - tv_count.getLeft())
                .rotation(-360)
                .alpha(255, 0)
                .interpolator(new AccelerateInterpolator())
                .duration(300)
                .start();
    }
    public void setData(OnClick onAddClick, ProductBean productBean,double tempCount) {
        this.mProductBean = productBean;
        this.onAddClick = onAddClick;
        count = tempCount;
        if (count == 0) {
            sub.setAlpha(0);
            tv_count.setAlpha(0);
        } else {
            sub.setAlpha(1f);
            tv_count.setAlpha(1f);
            tv_count.setText(count + "");
        }
    }
    public void setState(long count) {
        addbutton.setState(count > 0);
    }

    public void expendAdd(long count) {
        this.count = count;
        tv_count.setText(count == 0 ? "1" : count + "");
        if (count == 0) {
            subAnim();
        }
    }

}
