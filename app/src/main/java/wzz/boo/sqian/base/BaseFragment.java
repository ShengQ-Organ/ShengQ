package wzz.boo.sqian.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import wzz.boo.sqian.utils.LogUtils;

/**
 *
 */
public abstract class BaseFragment extends Fragment {

    //	public PreferenceUtil pu;
    public AppCompatActivity mContext;

    // 声明基于该帧布局对应的视图View对象
    public View view;

    // 用于fragment之间传递数据的
    public Bundle bundle;

    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 调用initView方法初始化视图对象
        if (getLayout() != 0) {
            view = initView(inflater.inflate(getLayout(), container, false));
            LogUtils.d("重新加载布局了......");
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // 当activity创建后后开始初始化数据
        super.onActivityCreated(savedInstanceState);
        setViews(savedInstanceState);
    }

    private boolean refresh = false;

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    private View initView(View childView) {
        return childView;
    }

    // -------------------------------------------------------------
    // 父类方法区

    // --------------------------------------------------------------
    // 公共方法区

    // -------------------------------------------------------------
    // 重写方法区

    /**
     * 返回本界面的布局文件
     *
     * @return
     */
    public abstract int getLayout();

    /**
     * 子类OnCreate方法
     *
     * @param savedInstanceState
     */
    public abstract void setViews(Bundle savedInstanceState);

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public <T extends View> T findAndCastView(View v, int resId) {

        return (T) v.findViewById(resId);
    }

    public <T extends View> T findAndCastView(int resId) {

        return (T) view.findViewById(resId);
    }

    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToast(String info) {
    }

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_SHORT).show();
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

}
