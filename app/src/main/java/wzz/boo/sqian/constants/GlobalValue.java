package wzz.boo.sqian.constants;

import android.webkit.WebSettings;

public class GlobalValue {

    /**
     * WebView缓存模式</br>
     * WebSettings.LOAD_DEFAULT : 默认模式，有缓存
     * WebSettings.LOAD_NO_CACHE: 不使用缓存
     * WebSettings.LOAD_CACHE_ELSE_NETWORK: 只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
     */
    public static int WEBVIEW_CACHE_MODEL = WebSettings.LOAD_DEFAULT;

}
