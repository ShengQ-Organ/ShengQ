package wzz.boo.sqian.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;

import wzz.boo.sqian.R;
import wzz.boo.sqian.base.BaseActivity;
import wzz.boo.sqian.constants.GlobalValue;
import wzz.boo.sqian.net.NetworkUtils;
import wzz.boo.sqian.utils.LogUtils;

public class WebActivity extends BaseActivity {

    public static final String URL_WEB = "url";

    private String url;
    private WebView mWeb;

    private ProgressBar pb;

    @Override
    public int getChildView() {
        return R.layout.activity_web;
    }

    @Override
    protected void findViews() {
        url = getIntent().getStringExtra(URL_WEB);
        mTitleBarView = findAndCastView(R.id.title_bar);
        mWeb = findAndCastView(R.id.mWeb_view);
        pb = findAndCastView(R.id.progressbar);
    }

    @Override
    public void setViews(Bundle savedInstanceState) {
        mTitleBarView.setLayoutLeftShow(View.VISIBLE);
        //mTitleBarView.setTitleText("详情");
        initView();
        initYouMi();
    }

    @Override
    public void registerListeners() {
//        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//        Intent intent = new Intent(Intent.ACTION_PICK, uri);
//        startActivityForResult(intent, 0);
    }

    @Override
    public void doOtherEvents() {

    }

    /**
     * 跳转联系人列表的回调函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null)
                    return;
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                if (contacts != null) {
                    showToast(contacts[0] + " " + contacts[1]);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone.close();
            }
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    @SuppressLint("AddJavascriptInterface")
    private void initView() {
        mWeb.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.e("newWeb:" + url);

                if (!NetworkUtils.hasNetWork(WebActivity.this)) {
                    showToast("请检查网络");
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 隐藏进度条
                if (null != pb) {
                    pb.setVisibility(View.GONE);
                }
                super.onPageFinished(view, url);
            }
        });

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtils.e("title" + title);
                mTitleBarView.setTitleText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == pb.getMax()) {
                    pb.setVisibility(View.GONE);
                } else {
                    if (pb.getVisibility() == View.GONE) {
                        pb.setVisibility(View.VISIBLE);
                    }
                    pb.setProgress(newProgress);
                }
            }
        };
        // 设置setWebChromeClient对象
        mWeb.setWebChromeClient(wvcc);
        //mWeb.addJavascriptInterface(this, "TTY");

        WebSettings settings = mWeb.getSettings();
        webViewSetting(settings);
        mWeb.loadUrl(url);

        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeb.canGoBack()) {
                    mWeb.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * webView的一些设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void webViewSetting(WebSettings webSettings) {
        //设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(GlobalValue.WEBVIEW_CACHE_MODEL);
        //支持的语言类型
        webSettings.setDefaultTextEncodingName("UTF-8");
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    }

    @Override
    public void onBackPressed() {
        // 如果有需要，可以点击后退关闭插播广告。
        if (SpotManager.getInstance(this).isSpotShowing()) {
            SpotManager.getInstance(this).hideSpot();
            return;
        }
        if (mWeb.canGoBack()) {
            mWeb.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 插屏广告
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 插屏广告
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 插屏广告
        SpotManager.getInstance(this).onDestroy();
    }

    private void initYouMi() {
        //如果插屏广告设置了横图模式，则当广告有横图图片数据时，会使用横图广告图片数据。
        //竖图： SpotManager.IMAGE_TYPE_VERTICAL
        //横图： SpotManager.IMAGE_TYPE_HORIZONTAL
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        //默认为高级动画
        //没有动画： SpotManager.ANIMATION_TYPE_NONE
        //简单动画： SpotManager.ANIMATION_TYPE_SIMPLE
        //高级动画： SpotManager.ANIMATION_TYPE_ADVANCED
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

        //展示插屏广告¶
        SpotManager.getInstance(this).showSpot(this,
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

}
