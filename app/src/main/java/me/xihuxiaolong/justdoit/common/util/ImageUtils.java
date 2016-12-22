package me.xihuxiaolong.justdoit.common.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by yangxiaolong on 15/12/17.
 */
public class ImageUtils {

    public ImageUtils(){}

    static int defaultPlaceholderId = -1;

    public static void loadImageFromUrl(Context context, String url, ImageView view) {
        loadImage(context, view, url, null, defaultPlaceholderId, DiskCacheStrategy.SOURCE, null);
    }

    public static void loadImageFromUrl(Context context, String url, ImageView view, int placeholderId) {
        loadImage(context, view, url, null, placeholderId, DiskCacheStrategy.SOURCE, null);
    }

    public static void loadImageFromUrl(Context context, String url, ImageView view, ImageView.ScaleType scaleType) {
        loadImage(context, view, url, scaleType, defaultPlaceholderId, DiskCacheStrategy.SOURCE, null);
    }

    public static void loadImageFromUrl(Context context, String url, ImageView view, ImageView.ScaleType scaleType,
                                    DiskCacheStrategy diskCacheStrategy) {
        loadImage(context, view, url, scaleType, defaultPlaceholderId, diskCacheStrategy, null);
    }



    public static void loadImageFromFile(Context context, ImageView view, String uri) {
        loadImage(context, view, "file://" + uri, null, defaultPlaceholderId, DiskCacheStrategy.NONE, null);
    }

    public static void loadImageFromFile(Context context, ImageView view, String uri, int placeholderId) {
        loadImage(context, view, "file://" + uri, null, placeholderId, DiskCacheStrategy.NONE, null);
    }

    public static void loadImageFromFile(Context context, ImageView view, String uri, ImageView.ScaleType scaleType) {
        loadImage(context, view, "file://" + uri, scaleType, defaultPlaceholderId, DiskCacheStrategy.NONE, null);
    }

    public static void loadImageFromFile(Context context, ViewTarget viewTarget, String uri, ImageView.ScaleType scaleType) {
        loadImage(context, viewTarget, "file://" + uri, scaleType, defaultPlaceholderId, DiskCacheStrategy.NONE);
    }

    public static void loadImageFromFile(Context context, ImageView imageView, String uri, ImageView.ScaleType scaleType, RequestListener requestListener) {
        loadImage(context, imageView, "file://" + uri, scaleType, defaultPlaceholderId, DiskCacheStrategy.NONE, requestListener);
    }

    public static void loadImage(Context context, ViewTarget viewTarget, String uri, ImageView.ScaleType scaleType,
                                 int placeholderId, DiskCacheStrategy diskCacheStrategy) {
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()
//                .into(viewTarget);
        BitmapTypeRequest<String> request = Glide.with(context).load(uri).asBitmap();
        BitmapRequestBuilder builder = request;
        if(ImageView.ScaleType.FIT_CENTER == scaleType){
            builder = request.fitCenter();
        }else if(ImageView.ScaleType.CENTER_CROP == scaleType){
            builder = request.centerCrop();
        }
//        else{
//            builder = request.centerCrop();
//        }
        if(placeholderId != -1)
            request.placeholder(placeholderId);
        builder.dontAnimate()
                .diskCacheStrategy(diskCacheStrategy)
                .into(viewTarget);
    }

    public static void loadImage(Context context, ImageView view, String uri, ImageView.ScaleType scaleType,
                                 int placeholderId, DiskCacheStrategy diskCacheStrategy, RequestListener requestListener) {
        DrawableTypeRequest<String> request = Glide.with(context).load(uri);
        DrawableRequestBuilder builder = request;
        if(ImageView.ScaleType.FIT_CENTER == scaleType){
            builder = request.fitCenter();
        }else if(ImageView.ScaleType.CENTER_CROP == scaleType){
            builder = request.centerCrop();
        }
//        else{
//            builder = request.centerCrop();
//        }
        if(placeholderId != -1)
            request.placeholder(placeholderId);
        builder.dontAnimate()
                .diskCacheStrategy(diskCacheStrategy)
                .listener(requestListener)
                .into(view);
    }

    public static Bitmap getBitmap(Context context, String uri) {
        Bitmap theBitmap = null;
        try {
            theBitmap = Glide.
                    with(context).
                    load(uri).
                    asBitmap().
                    into(1000, 2000). // Width and height
                    get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return theBitmap;
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "benpaowuliu");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
