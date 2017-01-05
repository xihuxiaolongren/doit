package me.xihuxiaolong.justdoit.module.editphoto;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.library.utils.ActivityUtils;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolongren.photoga.MediaChoseActivity;

public class EditPhotoActivity extends BaseMvpActivity<EditPhotoContract.IView, EditPhotoContract.IPresenter> implements EditPhotoContract.IView {

    public static final String ARGUMENT_TARGET_NAME = "TARGET_NAME";

    EditPhotoComponent editPhotoComponent;

    @State
    String picUri;

    private String targetName;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contentET)
    MaterialEditText contentET;
    @BindView(R.id.rootView)
    RelativeLayout rootView;
    @BindView(R.id.picIV)
    ImageView picIV;
    @BindView(R.id.operIV)
    ImageView operIV;
    @BindView(R.id.picPRL)
    View picPRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        targetName = getIntent().getStringExtra(ARGUMENT_TARGET_NAME);
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        ButterKnife.bind(this);

        setToolbar(toolbar, true);

        picPRL.setVisibility(TextUtils.isEmpty(picUri) ? View.GONE : View.VISIBLE);
        if(savedInstanceState == null) {
            Intent intent = new Intent(this, MediaChoseActivity.class);
            //chose_mode选择模式 0单选 1多选
            intent.putExtra("chose_mode", 0);
            //是否显示需要第一个是图片相机按钮
            intent.putExtra("isNeedfcamera", true);
            startActivityForResult(intent, MediaChoseActivity.REQUEST_CODE_CAMERA);
        }else{
            setSingleImage(picUri, picIV);
        }
        operIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picUri == null) {
                    Intent intent = new Intent(EditPhotoActivity.this, MediaChoseActivity.class);
                    //chose_mode选择模式 0单选 1多选
                    intent.putExtra("chose_mode", 0);
                    //是否显示需要第一个是图片相机按钮
                    intent.putExtra("isNeedfcamera", true);
                    startActivityForResult(intent, MediaChoseActivity.REQUEST_CODE_CAMERA);
                }else{
                    picUri = null;
                    picPRL.setVisibility(View.GONE);
                    operIV.setImageResource(R.drawable.menu_add_pic);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == MediaChoseActivity.REQUEST_CODE_CAMERA) {
            if (result != null && !CollectionUtils.isEmpty(result.getStringArrayListExtra("data"))) {
                ArrayList<String> uris = result.getStringArrayListExtra("data");
                picUri = uris.get(0);
                if(picPRL != null) {
                    picPRL.setVisibility(View.VISIBLE);
                    setSingleImage(picUri, picIV);
                }
                if(operIV != null){
                    operIV.setImageResource(R.drawable.icon_delete);
                }

            }
        }
    }

    private void setSingleImage(String filepath, ImageView imageView){
        int mWidth = 0, mHeight = 0;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filepath, options);
            mWidth = options.outWidth;
            mHeight = options.outHeight;
        } catch (Exception e) {

        }
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) imageView.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        if (mWidth > mHeight) {
            info.widthPercent = 0.6f;
            info.aspectRatio = 1.5f;
        } else {
            info.widthPercent = 0.5f;
            info.aspectRatio = 0.66f;
        }
        info.aspectRatio = ((float) mWidth) / mHeight;
        info.widthPercent = Math.min(0.8f, Math.max(0.2f, ((float) mWidth) / mHeight * 0.6f));
        imageView.setLayoutParams(params);
        ImageUtils.loadImageFromFile(this, picIV, picUri, ImageView.ScaleType.FIT_CENTER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    protected void injectDependencies() {
        editPhotoComponent = DaggerEditPhotoComponent.builder().appComponent(ProjectActivityUtils.getAppComponent(this))
                .editPhotoModule(new EditPhotoModule(targetName)).build();
    }

    @NonNull
    @Override
    public EditPhotoContract.IPresenter createPresenter() {
        return editPhotoComponent.presenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_confirm:
                ActivityUtils.hideSoftKeyboard(this);
                presenter.savePhoto(contentET.getText().toString(), picUri);
                return true;
        }
        return false;
    }

    @Override
    public void saveSuccess() {
        finish();
    }

    @Override
    public void deleteSuccess() {
        finish();
    }

    @Override
    public void sharePhoto() {

    }

}
