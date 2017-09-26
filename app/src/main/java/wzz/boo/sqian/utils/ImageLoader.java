package wzz.boo.sqian.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import wzz.boo.sqian.R;
import wzz.boo.sqian.base.BaseApplication;

/**
 * 图片加载类
 */
public class ImageLoader {
    /**
     * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
     */
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * 操作文件相关类对象的引用
     */
    private FileHelper mFileUtil;

    /**
     * 从网络下载图片成功后，在回调接口中更新UI
     */
    private Handler mHandler;

    /**
     * 1暂存正在下载的url，避免重复地下载、写入文件和回调接口。<br>
     * 2在图片下载成功后，查找View展示。
     */
    private HashMap<String, ImageView> mHashMap;

    private HashMap<ImageView, String> mHashMap2;

    private static ImageLoader instance;

    /**
     * 分配 给图片的最大内存
     */
    private int mCacheSize;

    /**
     * 暂存正在下载的url，避免重复地下载、写入文件和回调接口
     */
    private HashSet<String> mSet;

    private int size;

    private double maxSize = 300.00;

    synchronized public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private ImageLoader() {
        // 获取系统分配给每个应用程序的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        LogUtils.d("memory", "" + maxMemory / 1024 / 1024);
        mCacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
            // 必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mHashMap = new HashMap<String, ImageView>();
        mHashMap2 = new HashMap<ImageView, String>();
        mSet = new HashSet<String>();
        mFileUtil = FileHelper.getInstance();
        mHandler = new Handler();
    }

    /**
     * 先从内存缓存中查找，没有从硬盘缓存查找，还没有从网络下载
     *
     * @param url  完整的图片下载网址
     * @param name 图片名称
     * @param view 展示图片的ImageView
     */
    public void loadImage(String url, String name, ImageView view,
                          int defaultresid) {

        if (url == null || url.isEmpty()) {
            // setDefault
            view.setImageResource(defaultresid);
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(defaultresid)
                .showImageOnFail(defaultresid)
                .showImageOnLoading(defaultresid)
                .bitmapConfig(Config.RGB_565).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(url, view, options);
    }

    /**
     * 先从内存缓存中查找，没有从硬盘缓存查找，还没有从网络下载
     *
     * @param url  完整的图片下载网址
     * @param name 图片名称
     * @param view 展示图片的ImageView
     */
    @SuppressLint("NewApi")
    public void loadImageResource(Context context, String url, String name,
                                  ImageView view, int defaultresid) {

        if (url == null || url.isEmpty()) {
            // setDefault

            view.setImageResource(defaultresid);
            return;
        }

        Bitmap bitmap = getBitmapFromMemCache(name);//从内存中获取图片
        if (bitmap != null) {
            /*view 的setBackground()方法要求API 16*/
            view.setImageDrawable(new BitmapDrawable(context.getResources(),
                    bitmap));
            return;//表示方法的结束
        }
        bitmap = getBitmapFromFile(name, view);//从文件获取图片，二级缓存
        if (bitmap != null) {
            addBitmapToMemoryCache(name, bitmap);
            /*view 的setBackground()方法要求API 16*/
            view.setImageDrawable(new BitmapDrawable(context.getResources(),
                    bitmap));
            return;
        }

        view.setBackgroundResource(defaultresid);
        //getBitmapFormUrl(url, view);
    }

    /**
     * 先从内存缓存中查找，没有从硬盘缓存查找，还没有从网络下载
     *
     * @param url          完整的图片下载网址
     * @param type         图片类型，决定了内存缓存的前缀、硬盘缓存的目录
     * @param view         展示图片的ImageView
     * @param defaultResId 默认图片
     */
    public void loadImage(String url, String type, ImageView view,
                          int defaultResId, String ivKeeper) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(defaultResId);
            return;
        }
        String name = StringUtil.getNameFromUrl(url);
        if (TextUtils.isEmpty(name)) {
            view.setImageResource(defaultResId);
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(defaultResId)
                .showImageOnFail(defaultResId)
                .showImageOnLoading(defaultResId)
//        .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                .bitmapConfig(Config.RGB_565).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(url, view, options);
    }

    /**
     * 先从内存缓存中查找，没有从硬盘缓存查找，还没有从网络下载
     *
     * @param url          完整的图片下载网址
     * @param view         展示图片的ImageView
     * @param defaultResId 默认图片
     */
    public void loadImage(String url, ImageView view,
                          int defaultResId) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(defaultResId);
            return;
        }
        String name = StringUtil.getNameFromUrl(url);
        if (TextUtils.isEmpty(name)) {
            view.setImageResource(defaultResId);
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(defaultResId)
                .showImageOnFail(defaultResId)
                .showImageOnLoading(defaultResId)
//        .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                .bitmapConfig(Config.RGB_565).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(url, view, options);
    }

    /**
     * 先从内存缓存中查找，没有从硬盘缓存查找，还没有从网络下载
     *
     * @param url  完整的图片下载网址
     * @param view 展示图片的ImageView
     */
    public void loadImage(String url, ImageView view) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String name = StringUtil.getNameFromUrl(url);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Config.RGB_565).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(url, view, options);
    }

    /**
     * 界面销毁前要移除ImageLoader对界面中View的引用
     *
     * @param set
     */
    public void removeViewReference2(HashSet<ImageView> set) {
        if (mHashMap2.isEmpty() || set.isEmpty()) {
            return;
        }
        for (ImageView iv : set) {
            mHashMap2.remove(iv);
        }
    }

    /**
     * 从内存缓存中获取一个Bitmap
     */
    public Bitmap getBitmapFromMemCache(String type, String name) {
        return mMemoryCache.get(type + File.separator + name);
    }

    /**
     * 添加Bitmap到内存缓存
     *
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String type, String name, Bitmap bitmap) {
        if (getBitmapFromMemCache(type, name) == null && bitmap != null) {

            mMemoryCache.put(type + File.separator + name, bitmap);
        }
    }

    /**
     * 从缓冲的本地文件获取图片
     *
     * @param type 图片类型，决定了内存缓存的前缀、硬盘缓存的目录
     * @param name 图片名称
     * @return
     */
    public Bitmap getBitmapFromFile(String type, String name) {
        String path = FileHelper.getInstance().getBitmapPath(type, name);
        if (!new File(path).exists()) {
            return null;
        }
        //如果文件中没有图片
        Options options = new Options();
        options.inJustDecodeBounds = true;
        //        BitmapFactory.decodeFile(path, options);
        Double multi = (options.outWidth * options.outHeight * 2)
                / (mCacheSize / 20d);
        multi = Math.sqrt(multi);
        for (int i = 0; ; i++) {
            if (multi < Math.pow(2, i)) {
                options.inSampleSize = (int) Math.pow(2, i);
                break;
            }
        }
        options.inPreferredConfig = Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 添加Bitmap到内存缓存
     *
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String name, Bitmap bitmap) {
        if (getBitmapFromMemCache(name) == null && bitmap != null) {
            mMemoryCache.put(name, bitmap);
        }
    }

    /**
     * 界面销毁前要移除ImageLoader对界面中View的引用
     * @param set
     */

    /**
     * 从内存缓存中获取一个Bitmap
     */
    public Bitmap getBitmapFromMemCache(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        return mMemoryCache.get(name);
    }

    /**
     * 从本地文件获取图片
     *
     * @param name 图片名称
     * @param view 展示图片的ImageView
     * @return
     */
    public Bitmap getBitmapFromFile(String name, ImageView view) {
        String path = mFileUtil.getBitmapPath(name);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        //        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = (int) ((options.outWidth * options.outHeight * 4) / (mCacheSize / 8d));
        options.inPreferredConfig = Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 从本地文件获取图片
     *
     * @param path 图片path
     * @param view 展示图片的ImageView
     * @return
     */
    public void getBitmapFromPath(String path, ImageView view) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        //        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = (int) ((options.outWidth * options.outHeight * 4) / (mCacheSize / 8d));
        options.inPreferredConfig = Config.RGB_565;
        options.inJustDecodeBounds = false;
        view.setImageBitmap(BitmapFactory.decodeFile(path, options));
    }

    /**
     * 移除网络连接已经结束的或者在没有网络时添加的图片url，注意保持线程同步
     *
     * @param url
     */
    public void removeFinishedUrl(String url) {
        mHashMap.remove(url);
    }

    /**
     * 界面销毁前要移除ImageLoader对界面中View的引用
     *
     * @param set
     */
    public void removeViewReference(HashSet<String> set) {
        if (mHashMap.isEmpty() || set.isEmpty()) {
            return;
        }
        for (String url : set) {
            mHashMap.remove(url);
        }
    }

    /**
     * 根据url查找正确的View，并从ImageLoader移除对View的引用
     */
    public ImageView getRightView(String url) {
        ImageView iv = mHashMap.get(url);
        mHashMap.remove(url);
        return iv;
    }

    public Bitmap getAddIcon() {
        Bitmap bitmap = mMemoryCache.get("add_icon");
        if (bitmap == null) {
            BitmapDrawable drawable = (BitmapDrawable) BaseApplication.getInstance()
                    .getResources()
                    .getDrawable(R.drawable.app_icon);
            bitmap = drawable.getBitmap();
        }
        return bitmap;
    }

    public Bitmap getLocalBitmap(String path, String type) {
        Bitmap bitmap = mMemoryCache.get(path);
        if (bitmap != null) {
            return bitmap;
        }

        mMemoryCache.put(path, bitmap);
        return bitmap;
    }

    /**
     * 从本地文件（非缓存文件）获取图片
     *
     * @param path
     * @param type
     * @return
     */
    public Bitmap getLocalBitmap2(String path, String type) {
        Bitmap bitmap = mMemoryCache.get(type + File.separator + path);
        if (bitmap != null) {
            return bitmap;
        }
        //计算图片长宽
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        Double multi = (options.outWidth * options.outHeight * 2)
                / (mCacheSize / 20d);
        multi = Math.sqrt(multi);
        //设置适当参数，压缩图片，以减少解码图片占用的内存
        for (int i = 0; ; i++) {
            if (multi <= Math.pow(2, i)) {
                options.inSampleSize = (int) Math.pow(2, i);
                break;
            }
        }
        options.inPreferredConfig = Config.RGB_565;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap != null) {
            Log.d("ImageLoader", "size=" +
                    String.valueOf(bitmap.getByteCount() / 1024) + "KB");
            /*bitmap 的getByteCount()方法要求API 12及以上*/
            size = bitmap.getByteCount();
            //如果图片大于300kb,进行压缩处理
            if (size / 1024 > maxSize) {
                double i = size / 1024 / maxSize;
                bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i),
                        bitmap.getHeight() / Math.sqrt(i));

            }
            // bitmap = ImageUtil.centerSquareScaleBitmap(path, BaseApplication.getInstance());
            if (type.equals(FileHelper.HEAD_ROUND_TYPE)) {
                //bitmap = ImageUtils.getRoundedCornerBitmap(bitmap);
            }
            mMemoryCache.put(type + File.separator + path, bitmap);
        }
        return bitmap;
    }

    //    当图片大于200kb执行此方法
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

//    public Bitmap getLocalPicThumbnail(LocalPic item) {
//        Bitmap bitmap;
//        if (!TextUtils.isEmpty(item.getThumbnailPath())) {
//            bitmap = mMemoryCache.get(item.getThumbnailPath());
//            if (bitmap == null) {
//                Options options = new Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(item.getThumbnailPath(), options);
//                options.inSampleSize = (int) Math.max(options.outWidth / 250d,
//                        options.outHeight / 250d);
//                options.inPreferredConfig = Config.RGB_565;
//                options.inJustDecodeBounds = false;
//                bitmap = BitmapFactory.decodeFile(item.getThumbnailPath(),
//                        options);
//                if (bitmap != null) {
//                    mMemoryCache.put(item.getThumbnailPath(), bitmap);
//                }
//            }
//        } else if (!TextUtils.isEmpty(item.getImagePath())) {
//            bitmap = mMemoryCache.get("thumbnail" + item.getImagePath());
//            if (bitmap == null) {
//                Options options = new Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(item.getImagePath(), options);
//                options.inSampleSize = (int) Math.max(options.outWidth / 250d,
//                        options.outHeight / 250d);
//                options.inPreferredConfig = Config.RGB_565;
//                options.inJustDecodeBounds = false;
//                bitmap = BitmapFactory.decodeFile(item.getImagePath(), options);
//                if (bitmap != null) {
//                    mMemoryCache.put("thumbnail" + item.getImagePath(), bitmap);
//                }
//            }
//        } else {
//            return null;
//        }
//        return bitmap;
//    }

    public void clearmemory() {
        //清空universalimageloader的图片缓存
//    	com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearMemoryCache();
//    	com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearDiskCache();
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                mMemoryCache.trimToSize(mCacheSize);
                //FileHelper.delAllFiles(FileHelper.cachePath);
                Toast.makeText(BaseApplication.getInstance(),
                        "缓存已清理",
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(BaseApplication.getInstance(),
                        "没有可清理缓存",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

}
