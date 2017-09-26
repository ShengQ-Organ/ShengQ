package wzz.boo.sqian.customView;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wzz.boo.sqian.R;

public class TitleBarView extends RelativeLayout {

    private static final String TAG = "TitleBarView";
    private Context mContext;
    private ImageView btnLeft;
    private ImageView btnRight;
    private TextView tv_center;

    private LinearLayout mLayoutLeft;

    private LinearLayout mLayoutRight;

//    private ImageView iv_title_left_open_drawerLayout;

    private TextView tv_right;

    public TitleBarView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.title_layout, this);
        btnLeft = (ImageView) findViewById(R.id.iv_title_left_back);
        btnRight = (ImageView) findViewById(R.id.iv_title_right_img);
        tv_center = (TextView) findViewById(R.id.title_name_text);
        mLayoutLeft = (LinearLayout) findViewById(R.id.ll_title_left_back);
        mLayoutRight = (LinearLayout) findViewById(R.id.ll_title_right_img);
//        iv_title_left_open_drawerLayout =
//                (ImageView) findViewById(R.id.iv_title_left_open_drawerLayout);
        tv_right = (TextView) findViewById(R.id.tv_title_right_text);
        setBtnLeftOnclickListener();
    }

    public void setBtnRight(int icon) {
//        Drawable img = mContext.getResources().getDrawable(icon);
//		int height=CommonUtils.dip2px(mContext, 30);
//		int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
//		img.setBounds(0, 0, width, height);
        btnRight.setImageResource(icon);
    }

    public void setBtnLeft(int icon) {
        btnLeft.setImageResource(icon);
    }

    public void setBtnRightShow(boolean show) {
        if (show) {
            btnRight.setVisibility(View.VISIBLE);
        } else {
            btnRight.setVisibility(View.GONE);
        }
    }

    public void setLayoutLeftShow(int visibility) {
        mLayoutLeft.setVisibility(visibility);
    }

    public void setCenterTxtShow(int visibility) {
        tv_center.setVisibility(visibility);
    }

    public void setTitleText(int txtRes) {
        tv_center.setText(getResources().getString(txtRes));
    }

    public void setTitleText(String txtRes) {
        tv_center.setText(txtRes);
    }

    public void setRightText(String txtRes) {
        btnRight.setVisibility(View.GONE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(txtRes);
    }

    public void setTitleText(String txtRes, int color) {
        tv_center.setText(txtRes);
        tv_center.setTextColor(mContext.getResources().getColor(color));
    }

    public void setBtnRightOnclickListener(OnClickListener listener) {
        mLayoutRight.setVisibility(View.VISIBLE);
        mLayoutRight.setOnClickListener(listener);
    }

    // 设置标题左侧打开侧滑菜单的按钮监听
    public void setOpenLeftMenuListener(OnClickListener listener) {
        btnLeft.setVisibility(View.GONE);
//        iv_title_left_open_drawerLayout.setVisibility(View.VISIBLE);
        mLayoutLeft.setVisibility(View.VISIBLE);
        mLayoutLeft.setOnClickListener(listener);
    }

    public ImageView getBtnRight() {
        if (btnRight != null) {
            return btnRight;
        }
        return null;
    }

    private void setBtnLeftOnclickListener() {
        mLayoutLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((Activity) mContext).finish();
            }
        });
    }

    public void setBtnLeftOnclickListener(OnClickListener listener) {
        mLayoutLeft.setOnClickListener(listener);
    }

    public void destoryView() {
        tv_center.setText(null);
    }

    public void setBtnLeftImg(int resId) {
        btnLeft.setImageResource(resId);
    }

    public void setBtnRightText(String txt) {
        mLayoutRight.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.GONE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(txt);
    }

}
