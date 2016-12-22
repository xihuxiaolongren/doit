package me.xihuxiaolong.justdoit.module.images;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;

public class BigImageActivity extends BaseActivity {

    @BindView(R.id.showSingleImage)
    ImageView showSingleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ButterKnife.bind(this);
        ActivityCompat.postponeEnterTransition(this);
        String imageUrl = getIntent().getStringExtra("imageUrl");
        ImageUtils.loadImageFromFile(this, showSingleImage, imageUrl, ImageView.ScaleType.FIT_CENTER, new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                ActivityCompat.startPostponedEnterTransition(BigImageActivity.this);
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                ActivityCompat.startPostponedEnterTransition(BigImageActivity.this);
                return false;
            }
        });
//        showSingleImage.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
//            @Override
//            public void onSingleTapConfirmed() {
//                supportFinishAfterTransition();
//            }
//        });
    }

}
