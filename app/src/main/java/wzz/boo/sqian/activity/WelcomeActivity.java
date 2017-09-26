package wzz.boo.sqian.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import net.youmi.android.AdManager;
import net.youmi.android.nm.sp.SplashViewSettings;
import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;

import wzz.boo.sqian.R;
import wzz.boo.sqian.base.BaseActivity;
import wzz.boo.sqian.constants.AppConstantUtil;

/**
 * 欢迎页面
 *
 * @author zy
 * @Date 2016-3-7
 * @Time 下午3:56:00
 */
public class WelcomeActivity extends BaseActivity {

    private static final int EmptyMsg = 0x00;

    private RelativeLayout mRelativeLayout;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.finish();
        }

    };

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public int getChildView() {
        return R.layout.activity_welcome;
    }

    @Override
    public void findViews() {
        mRelativeLayout = findAndCastView(R.id.welcome_view);
        AdManager.getInstance(this).init(AppConstantUtil.YM_ID, AppConstantUtil.YM_SECRET, true);
    }

    @Override
    public void setViews(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 16) {
            // 隐藏状态栏、导航栏
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(option);
        }
        //WelcomeActivity.this.handler.sendEmptyMessageDelayed(EmptyMsg, 2000);
        initYouMi();
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public void doOtherEvents() {

    }

    private void initYouMi() {
        //实例化开屏广告设置类
        SplashViewSettings splashViewSettings = new SplashViewSettings();

        //设置展示失败是否自动跳转至设定的窗口
        //默认自动跳转
        splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);

        //设置开屏结束后跳转的窗口
        splashViewSettings.setTargetClass(MainActivity.class);

        //设置开屏控件容器
        // 使用默认布局参数
        splashViewSettings.setSplashViewContainer(mRelativeLayout);
        // 使用自定义布局参数
        //splashViewSettings.setSplashViewContainer(ViewGroup splashViewContainer,
        //      ViewGroup.LayoutParams splashViewLayoutParams);

        //展示开屏广告
        SpotManager.getInstance(this).showSplash(this, splashViewSettings,
                new SpotListener() {
                    @Override
                    public void onShowSuccess() {

                    }

                    @Override
                    public void onShowFailed(int i) {

                    }

                    @Override
                    public void onSpotClosed() {

                    }

                    @Override
                    public void onSpotClicked(boolean b) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(this).onDestroy();
    }

}
