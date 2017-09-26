package wzz.boo.sqian.utils;

import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.taobao.applink.util.TBAppLinkUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 类说明：
 * Created by zy
 *
 * @Date 2016/12/15
 * @Time: 14:12
 */
public class AlibcUtils {

    public static boolean isLogin() {
        Session session = getSession();
        return session != null && StringUtil.isNotBlank(session.openId);
    }

    public static Session getSession() {
        return AlibcLogin.getInstance().getSession();
    }

    /**
     * 分域显示我的订单，或者全部显示我的订单
     */
    public static void showOrder(Activity activity) {
        showPageBasePage(activity, new AlibcMyOrdersPage(0, true),
                getShowParams(0), null, getTrackParam());
    }

    /**
     * 分域显示我的购物车
     */
    public static void showShoppingCart(Activity activity) {
        showPageBasePage(activity, new AlibcMyCartsPage(),
                getShowParams(0), null, getTrackParam());
    }

    /**
     * 显示我的购物车
     */
    public static void showShoppingCart(Activity activity, WebView webView,
                                        WebViewClient webViewClient, WebChromeClient webChromeClient) {
        showPageWebView(activity, webView, webViewClient, webChromeClient,
                new AlibcMyCartsPage(), getShowParams(0), null, getTrackParam());
    }

    //页面打开方式， 0 默认，1 H5，2 taobao, 3 tmall
    private static AlibcShowParams getShowParams(int index) {
        AlibcShowParams alibcShowParams;
        switch (index) {
            case 0:
                alibcShowParams = new AlibcShowParams(OpenType.Auto, false);
                break;
            case 1:
                alibcShowParams = new AlibcShowParams(OpenType.H5, false);
                break;
            case 2:
                alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                alibcShowParams.setClientType(TBAppLinkUtil.TAOBAO_SCHEME);
                break;
            case 3:
                alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                alibcShowParams.setClientType(TBAppLinkUtil.TMALL_SCHEME);
                break;
            default:
                alibcShowParams = new AlibcShowParams(OpenType.Auto, false);
                break;
        }
        return alibcShowParams;
    }

    private static Map<String, String> getTrackParam() {
        Map<String, String> exParams;//yhhpass参数
        exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        return exParams;
    }

    /**
     * 外部传入webview方式打开电商组件
     * 注意：当传入webviewClient，并重载shouldOverrideUrlLoading方法时，
     * 遇到淘系链接情况下(即访问淘宝、天猫、登录、购物车等页面时)，
     * 该方法返回值要为false，否则可能会出现业务流程错误问题。
     */
    public static void showPageWebView(Activity activity, WebView webView, WebViewClient webViewClient,
                                       WebChromeClient webChromeClient, AlibcBasePage tradePage,
                                       AlibcShowParams showParams, AlibcTaokeParams taokeParams, Map trackParam) {
        /**
         * 打开电商组件,支持使用外部webview
         *
         * @param activity             必填
         * @param webView              外部 webView
         * @param webViewClient        webview的webViewClient
         * @param webChromeClient      webChromeClient客户端
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        AlibcTrade.show(activity, webView, webViewClient, webChromeClient,
                tradePage, showParams, taokeParams, trackParam, new AlibcTradeCallback() {

                    @Override
                    public void onTradeSuccess(TradeResult tradeResult) {
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                    }
                });
    }

    /**
     * 提供默认的webview打开电商组件
     */
    public static void showPageBasePage(Activity activity, AlibcBasePage tradePage,
                                        AlibcShowParams showParams, AlibcTaokeParams taokeParams, Map trackParam) {
        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        AlibcTrade.show(activity, tradePage, showParams, taokeParams, trackParam, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
            }
        });
    }

}
