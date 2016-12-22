package me.xihuxiaolong.justdoit.module.images;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class BigImageActivity extends BaseActivity {

    @BindView(R.id.showSingleImage)
    ImageView showSingleImage;

    PhotoViewAttacher mAttacher;
    @BindView(R.id.rootView)
    RelativeLayout rootView;

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
        mAttacher = new PhotoViewAttacher(showSingleImage);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                supportFinishAfterTransition();
            }
        });
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
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
