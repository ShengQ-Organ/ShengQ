package wzz.boo.sqian.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import net.youmi.android.AdManager;
import net.youmi.android.nm.sp.SpotManager;

import java.util.ArrayList;
import java.util.List;

import wzz.boo.sqian.R;
import wzz.boo.sqian.constants.AppConstantUtil;
import wzz.boo.sqian.fragment.MainFragment;
import wzz.boo.sqian.utils.AlibcUtils;
import wzz.boo.sqian.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;

    private TextView mToolBarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdManager.getInstance(this).init(AppConstantUtil.YM_ID, AppConstantUtil.YM_SECRET, true);
        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();
        addFragment(new MainFragment(), true, R.id.container);
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icon_close);

        MenuObject send = new MenuObject("购物车");
        send.setResource(R.drawable.icon_shopping_cart);

        MenuObject like = new MenuObject("我的订单");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_order);
        like.setBitmap(b);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        return menuObjects;
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.drawable.icon_search);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra(WebActivity.URL_WEB, AppConstantUtil.URL_SEARCH);
                startActivity(intent);
            }
        });
        if (AlibcUtils.isLogin())
            mToolBarTextView.setText(AlibcUtils.getSession().nick);
        else
            mToolBarTextView.setText("点击登陆");
        mToolBarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AlibcUtils.isLogin()) {
                    mToolBarTextView.setText(AlibcUtils.getSession().nick);
                    return;
                }
                AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(MainActivity.this, new AlibcLoginCallback() {
                    @Override
                    public void onSuccess() {
                        mToolBarTextView.setText(AlibcUtils.getSession().nick);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.e("登录失败,错误码=" + code + "错误消息=" + msg);
                    }
                });
            }
        });
    }

    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (AlibcUtils.isLogin()) {
                    mToolBarTextView.setText(AlibcUtils.getSession().nick);
                    if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                        mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                    }
                } else {
                    AlibcLogin alibcLogin = AlibcLogin.getInstance();
                    alibcLogin.showLogin(MainActivity.this, new AlibcLoginCallback() {
                        @Override
                        public void onSuccess() {
                            mToolBarTextView.setText(AlibcUtils.getSession().nick);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            LogUtils.e("登录失败,错误码=" + code + "错误消息=" + msg);
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        //Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
        if (1 == position)
            AlibcUtils.showShoppingCart(this);
        else if (2 == position)
            AlibcUtils.showOrder(this);
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        //Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * 第一次按返回键的时间
     */
    private long firstTime;

    @Override
    public void onBackPressed() {
        // 两次按返回键退出应用处理
        long secondTime = System.currentTimeMillis();
        // 如果两次按键时间间隔大于2秒，则不退出
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 更新firstTime
            firstTime = secondTime;
        } else {
            AlibcTradeSDK.destory();
            SpotManager.getInstance(this).onAppExit();
            finish();
        }
    }
}
