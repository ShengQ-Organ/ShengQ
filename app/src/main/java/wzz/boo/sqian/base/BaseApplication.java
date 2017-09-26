package wzz.boo.sqian.base;

import android.app.Application;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;

import wzz.boo.sqian.utils.LogUtils;

/**
 * Created by zy 2015/12/7.
 */
public class BaseApplication extends Application {

    private static BaseApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initTaoBao();
    }

    public static BaseApplication getInstance() {
        return app;
    }

    private void initTaoBao() {
        //电商SDK初始化
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                LogUtils.e("初始化成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                LogUtils.e("初始化失败,错误码=" + code + "错误消息=" + msg);
            }
        });
    }

}
