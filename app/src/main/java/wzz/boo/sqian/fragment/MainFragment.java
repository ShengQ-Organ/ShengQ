package wzz.boo.sqian.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import wzz.boo.sqian.R;
import wzz.boo.sqian.activity.WebActivity;
import wzz.boo.sqian.base.BaseFragment;
import wzz.boo.sqian.constants.AppConstantUtil;
import wzz.boo.sqian.constants.GlobalValue;
import wzz.boo.sqian.net.NetworkUtils;
import wzz.boo.sqian.utils.LogUtils;

public class MainFragment extends BaseFragment {

    private String url;
    private WebView mWeb;

    private ProgressBar pb;

    @Override
    public int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void setViews(Bundle savedInstanceState) {
        url = AppConstantUtil.URL_HOME;
        mWeb = findAndCastView(R.id.mWeb_view);
        pb = findAndCastView(R.id.progressbar);
        initView();
    }

    @SuppressLint("AddJavascriptInterface")
    private void initView() {
        mWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.e("newWeb:" + url);

                if (!NetworkUtils.hasNetWork(getActivity())) {
                    showToast("请检查网络");
                } else if (AppConstantUtil.URL_HOME.equals(url) || AppConstantUtil.URL_HOME_LONG.equals(url)) {
                    return false;
                } else {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(WebActivity.URL_WEB, url);
                    startActivity(intent);
                    return true;
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

}
