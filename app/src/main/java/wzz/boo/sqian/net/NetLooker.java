package wzz.boo.sqian.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import wzz.boo.sqian.base.BaseApplication;

public class NetLooker
{
    /**
     * 描述：判断网络是否有
     */
    public static boolean isNetworkAvailable()
    {
        try
        {
            ConnectivityManager connectivity = (ConnectivityManager) BaseApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected())
                {
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
