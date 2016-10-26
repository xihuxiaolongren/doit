package me.xihuxiaolongren.photoga;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

/**
 * 调用媒体选择库
 * 需要在inten中传递2个参数
 * 1. 选择模式 chose_mode  0  //单选 1多选
 * 2. 选择张数 max_chose_count  多选模式默认 9 张
 */
public class MediaChoseActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CAMERA = 2001;
    public static final int REQUEST_CODE_CROP = 2002;
    public static final int CHOSE_MODE_SINGLE = 0;
    public static final int CHOSE_MODE_MULTIPLE = 1;

    Toolbar toolbar;

    public int max_chose_count = 1;
    public LinkedHashSet<String> imagesMap = new LinkedHashSet<>();
    PhotoGalleryFragment photoGalleryFragment;
    int choseMode = CHOSE_MODE_MULTIPLE;

    boolean isNeedCrop = false;
    boolean isNeedcamera = false;

    int crop_image_w, crop_image_h;

    Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_chose);
        if(savedInstanceState != null && savedInstanceState.getParcelable("currentUri") != null)
            currentUri = savedInstanceState.getParcelable("currentUri");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("图片");
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        choseMode = getIntent().getIntExtra("chose_mode", CHOSE_MODE_MULTIPLE);
        if (choseMode == CHOSE_MODE_MULTIPLE) {
            max_chose_count = getIntent().getIntExtra("max_chose_count", 9);
        }
        isNeedcamera = getIntent().getBooleanExtra("need_camera", true);
        //是否需要剪裁
        isNeedCrop = getIntent().getBooleanExtra("crop", false);
        crop_image_w = getIntent().getIntExtra("crop_image_w", 720);
        crop_image_h = getIntent().getIntExtra("crop_image_h", 720);

        photoGalleryFragment = PhotoGalleryFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean("need_camera", isNeedcamera);
        bundle.putInt("chose_mode", choseMode);
        bundle.putInt("max_chose_count", max_chose_count);
        bundle.putBoolean("crop", isNeedCrop);
        photoGalleryFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, photoGalleryFragment, PhotoGalleryFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private boolean isGif(String imageUrl){
        return imageUrl.contains(".") && "gif".equalsIgnoreCase(imageUrl.substring(imageUrl.lastIndexOf(".") + 1));
    }

    boolean isPriview = false;
    public void startPreview(LinkedHashSet<String> map, String currentimage) {
        if (isNeedCrop && !isCropOver && !isGif(currentimage)) {
            sendStartCrop(currentimage);
        } else {
            ArrayList<String> ims = new ArrayList<>();
            int pos = 0;
            int i = 0;
            for (String s : map) {
                ims.add(s);
                if (s.equals(currentimage)) {
                    pos = i;
                }
                i++;
            }
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, ImagePreviewFragemnt.newInstance(ims, pos), ImagePreviewFragemnt.class.getSimpleName());
            fragmentTransaction.addToBackStack("con");
            fragmentTransaction.commit();
            isPriview = true;
            invalidateOptionsMenu();
        }
    }

    public Fragment getCurrentFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public LinkedHashSet getImageChoseMap() {
        return imagesMap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_gallery, menu);
        if (isPriview && (choseMode == CHOSE_MODE_MULTIPLE)) {
            menu.findItem(R.id.menu_photo_delete).setVisible(true);
        } else {
            menu.findItem(R.id.menu_photo_delete).setVisible(false);
        }
        if (imagesMap.size() < 1) {
            menu.findItem(R.id.menu_photo_count).setEnabled(false);
            menu.findItem(R.id.menu_photo_count).setVisible(false);
        } else {
            menu.findItem(R.id.menu_photo_count).setEnabled(true);
            menu.findItem(R.id.menu_photo_count).setVisible(true);
            TextView textView = (TextView) menu.findItem(R.id.menu_photo_count).getActionView().findViewById(R.id.complete);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendImages();
                }
            });
            if (choseMode == CHOSE_MODE_MULTIPLE) {
                textView.setText("完成(" + imagesMap.size() + "/" + max_chose_count + ")");
            } else {
                textView.setText("完成(1)");
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isPriview) {
                popFragment();
            } else {
                finish();
            }
        } else if (item.getItemId() == R.id.menu_photo_delete) {
            ImagePreviewFragemnt fragemnt = (ImagePreviewFragemnt) getCurrentFragment(ImagePreviewFragemnt.class.getSimpleName());
            if (fragemnt != null) {
                String img = fragemnt.delete();
                if(imagesMap.contains(img)) {
                    imagesMap.remove(img);
                    invalidateOptionsMenu();
                }
            }
        }
//        else if (item.getItemId() == R.id.menu_photo_count) {
//            sendImages();
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        if (keyCode == KeyEvent.KEYCODE_BACK && fm.getBackStackEntryCount() > 0) {
            popFragment();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void log(String msg) {
        Log.i("gallery", msg);
    }

    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
        isPriview = false;
        invalidateOptionsMenu();
        if (photoGalleryFragment != null && choseMode == CHOSE_MODE_MULTIPLE) {
            photoGalleryFragment.notifyDataSetChanged();
        }
    }

    boolean isCropOver = false;

    public void sendImages() {
        Iterator iterator = imagesMap.iterator();
        File file = new File(iterator.next().toString());
        if (!file.exists()) {
            Toast.makeText(this, "获取文件失败", Toast.LENGTH_SHORT).show();
        }
        if (isNeedCrop && !isCropOver && !isGif(file.getAbsolutePath())) {
            sendStartCrop(file.getAbsolutePath());
        } else {
            Intent intent = new Intent();
            ArrayList<String> img = new ArrayList<>();
            Iterator<String> iterator1 = imagesMap.iterator();
            while (iterator1.hasNext()) {
                String key = iterator1.next();
                img.add(key);
            }
            intent.putExtra("data", img);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // 将数据保存到outState对象中, 该对象会在重建activity时传递给onCreate方法
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("currentUri", currentUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Crop.REQUEST_CROP && choseMode == CHOSE_MODE_SINGLE) {
            Intent intent = new Intent();
            ArrayList<String> imgs = new ArrayList<>();
            Uri uri = Crop.getOutput(data);
            String crop_path = uri.getPath();
            isCropOver = true;
            if (crop_path != null) {
                imgs.add(crop_path);
                intent.putExtra("data", imgs);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "截取图片失败", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA && choseMode == CHOSE_MODE_SINGLE) {
            if (currentUri != null) {
                if (isNeedCrop && !isCropOver) {
                    sendStartCrop(currentUri.getPath());
                } else {
                    Intent intent = new Intent();
                    ArrayList<String> img = new ArrayList<>();
                    img.add(currentUri.getPath());
                    intent.putExtra("data", img);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                Toast.makeText(MediaChoseActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA && (choseMode == CHOSE_MODE_MULTIPLE)) {

            if (currentUri != null) {
                Intent intent = new Intent();
                ArrayList<String> img = new ArrayList<>();
                img.add(currentUri.getPath());
                intent.putExtra("data", img);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(MediaChoseActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void sendStartCamera() {
        currentUri = getTempFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentUri);
        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop crop = Crop.of(source, destination);
        crop.asSquare();
        crop.start(this);
    }

    public void sendStartCrop(String path) {
        beginCrop(Uri.fromFile(new File(path)));
    }
    public Uri getTempFile() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String str = format.format(date);
        File file = null;
        try {
            file = File.createTempFile("IMG_GE_", ".jpg", Environment.getExternalStorageDirectory());
        } catch (IOException e) {
            return null;
        }
        if(file.exists())
            file.delete();
        return Uri.fromFile(file);
    }
    public File getCropFile() {
        return new File(getTmpPhotos());
    }
    /**
     * 获取tmp path
     * @return
     */
    public  String getTmpPhotos() {
        return new File(getCacheFile(), ".tmpcamara" + System.currentTimeMillis() + ".jpg").getAbsolutePath();
    }
    /**
     * 临时缓存目录
     * @return
     */
    public String getCacheFile() {
        return  getDir("post_temp", Context.MODE_PRIVATE).getAbsolutePath();
    }




}
