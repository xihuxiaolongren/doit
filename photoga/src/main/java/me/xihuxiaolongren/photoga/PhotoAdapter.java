package me.xihuxiaolongren.photoga;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.widget.CheckBox;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by wangqiong on 15/3/27.
 */
public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public int maxChoseCount = 9;

    LayoutInflater inflater;
    List<String> imageses;
    Context context;
//    public static RecyclerView.LayoutParams params;
    LinkedHashSet hashmap;
    public int currentChoseMode;
    int sWidthPix;
    public PhotoAdapter(Context context, List<String> imageses, int chosemode) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.imageses = imageses;
        sWidthPix = context.getResources().getDisplayMetrics().widthPixels;
//        params = new RecyclerView.LayoutParams(sWidthPix / 3, sWidthPix / 3);
//        int dp3 = dip2px(context, 1);
//        params.setMargins(dp3, dp3, dp3, dp3);
        currentChoseMode = chosemode;
        hashmap = ((MediaChoseActivity) context).getImageChoseMap();
    }

    public void setImageses(List<String> imageses) {
        this.imageses = imageses;
        notifyDataSetChanged();
    }

    public LinkedHashSet getChoseImages() {
        return hashmap;
    }

    public void setMaxChoseCount(int maxChoseCount) {
        this.maxChoseCount = maxChoseCount;
    }

    boolean isNeedCamera = true;

    /**
     * 是否在第一个item现实相机
     *
     * @param isNeedCamera
     */
    public void setNeedCamera(boolean isNeedCamera) {
        this.isNeedCamera = isNeedCamera;
    }

    /**
     * DIP转换成PX
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_CAMERA) {
            holder = new CameraViewHolder(inflater.inflate(R.layout.item_photo_camera_layout, parent, false));
        } else {
            holder = new ImageViewHolder(inflater.inflate(R.layout.item_photo_image_layout, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_IMAGE) {
            final ImageViewHolder ivholder = (ImageViewHolder) holder;
            final String images = getItem(position);
            displayImage(images, ivholder.iv_image);
            if (currentChoseMode == MediaChoseActivity.CHOSE_MODE_MULTIPLE) {
                ivholder.checkBox.setVisibility(View.VISIBLE);
                ivholder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hashmap.contains(images)) {
                            hashmap.remove(images);
                            ivholder.alpha_view.setVisibility(View.GONE);

                        } else {
                            if (hashmap.size() >= maxChoseCount) {
                                ivholder.checkBox.setCheckedImmediately(false);
//                                ivholder.checkBox.setChecked(false);
                                Toast.makeText(context, "你最多只能选择" + maxChoseCount + "张照片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            hashmap.add(images);
                            ivholder.alpha_view.setVisibility(View.VISIBLE);
                        }
                        ((Activity) context).invalidateOptionsMenu();
                    }
                });
                if (hashmap.contains(images)) {
                    ivholder.alpha_view.setVisibility(View.VISIBLE);
                    ivholder.checkBox.setCheckedImmediately(true);
                } else {
                    ivholder.alpha_view.setVisibility(View.GONE);
                    ivholder.checkBox.setCheckedImmediately(false);
                }
                ivholder.alpha_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MediaChoseActivity) context).startPreview(getChoseImages(), images);
                    }
                });
            } else {
                ivholder.checkBox.setVisibility(View.GONE);
                ivholder.alpha_view.setVisibility(View.GONE);
                ivholder.iv_image.setClickable(true);
                ivholder.iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getChoseImages().clear();
                        getChoseImages().add(images);
                        ((MediaChoseActivity) context).sendImages();
                    }
                });
            }
        }else{
            CameraViewHolder holder1= (CameraViewHolder) holder;
            holder1.camera_lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentChoseMode == MediaChoseActivity.CHOSE_MODE_MULTIPLE){
                        if(getChoseImages().size() < maxChoseCount){
                            ((MediaChoseActivity)context).sendStartCamera();
                        }else{
                            Toast.makeText(context, "你最多只能选择" + maxChoseCount + "张照片", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(getChoseImages().size()>0){
                            getChoseImages().clear();
                        }
                        ((MediaChoseActivity)context).sendStartCamera();
                    }
                }
            });
        }
    }

    public void displayImage(String url, ImageView view) {
        Glide.with(context).load(url)
                .centerCrop()
                .crossFade()
//                .override(sWidthPix/3,sWidthPix/3)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.placeholder)
                .into(view);
    }

    public static int TYPE_IMAGE = 10;
    public static int TYPE_CAMERA = 11;

    public String getItem(int position){
        return imageses.get(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isNeedCamera) {
            return TYPE_CAMERA;
        }
        return TYPE_IMAGE;
    }

    @Override
    public int getItemCount() {
        return imageses.size() + 1;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        CheckBox checkBox;
        View alpha_view;

        public ImageViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            alpha_view = itemView.findViewById(R.id.alpha_view);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkimages);
        }
    }
    public static class CameraViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout camera_lin;
        public CameraViewHolder(View itemView) {
            super(itemView);
            camera_lin= (LinearLayout) itemView.findViewById(R.id.camera_lin);
        }
    }


}
