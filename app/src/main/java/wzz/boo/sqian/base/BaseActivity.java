package wzz.boo.sqian.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import wzz.boo.sqian.R;
import wzz.boo.sqian.customView.TitleBarView;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog myCustomProgressDialog;

    public Intent intent;

    public TitleBarView mTitleBarView;

    /**
     * 主要用于友盟统计
     */
    protected boolean hasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getChildView() != 0) {
            setContentView(getChildView());
        }
        intent = getIntent();
        mTitleBarView=findAndCastView(R.id.title_bar);

        findViews();
        setViews(savedInstanceState);
        registerListeners();
        doOtherEvents();
    }

    // -------------------------------------------------------------
    // 父类方法区

    /**
     * 给控件设置监听
     */
    public View setViewClick(int resId, OnClickListener listener) {
        View view = findViewById(resId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return view;
    }

    // --------------------------------------------------------------
    // 公共方法区

    /**
     * 显示正在加载的进度条
     */
    public void showProgressDialog() {
        showProgressDialog("Loading...");
    }

    /**
     * 显示正在加载的进度条
     */
    public void showProgressDialog(int resId) {
        showProgressDialog(getResources().getString(resId));
    }

    /**
     * 显示正在加载的进度条
     *
     * @param msg 进度条提示信息
     */
    public void showProgressDialog(String msg) {
        if (myCustomProgressDialog != null) {
            return;
        }
        myCustomProgressDialog = new ProgressDialog(this);
        myCustomProgressDialog.setMessage(msg);
        //myCustomProgressDialog.setCancelable(false);
        myCustomProgressDialog.show();
    }

    /**
     * 取消对话框显示
     */
    public void disMissDialog() {
        if (myCustomProgressDialog != null) {
            myCustomProgressDialog.dismiss();
            myCustomProgressDialog = null;
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToast(String info) {
        //UIUtils.showToastSafe(info);

    }

    public String getEditTextContent(EditText editText){
        return editText.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//		App.getApp().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // -------------------------------------------------------------
    // 重写方法区

    /**
     * 返回本界面的布局文件
     */
    public abstract int getChildView();

    /**
     * 查找View
     */
    protected abstract void findViews();

    /**
     * 子类OnCreate方法
     */
    public abstract void setViews(Bundle savedInstanceState);

    /**
     * 控件的点击事件
     */
    public abstract void registerListeners();

    /**
     * 添加其他业务逻辑，如注册广播、启动定时器等
     */
    public abstract void doOtherEvents();

    /**
     * 查找和强转View
     */
    protected <T extends View> T findAndCastView(int resId) {
        return (T) findViewById(resId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Toast.makeText(BaseActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void showToastHandler(String info) {
        Message msg = msgHandler.obtainMessage();
        msg.arg1 = 0;
        msg.obj = info;
        msgHandler.sendMessage(msg);
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写dispatchTouchEvent</p>
     * <p>参照以下注释代码</p>
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
