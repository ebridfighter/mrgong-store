package com.runwise.commomlibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.runwise.commomlibrary.R;

/**
 * Created by mike on 2017/11/6.
 */

public class LoadingDialog  extends Dialog {

    //	private AnimationDrawable anim;
    private TextView tvMsg;
    //	private HoldTimer holderTimer;
    public LoadingDialog(Context context) {
        super(context, R.style.DialogNoBg);
        setContentView(R.layout.dialog_loading);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setCanceledOnTouchOutside(false);
//		ImageView image = (ImageView) this.findViewById(R.id.loadingImageView);
//		anim = (AnimationDrawable) image.getBackground();
        tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 提示内容
     * @param strMessage
     * @return
     *
     */
    public void setMsg(String strMessage) {
        tvMsg.setText(strMessage);
    }

}
