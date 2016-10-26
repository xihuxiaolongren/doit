package me.xihuxiaolongren.photoga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import me.xihuxiaolongren.photoga.mode.ImageFolder;

/**
 * Created by wangqiong on 15/3/30.
 */
public class FolderAdapter extends BaseAdapter {


    List<ImageFolder> folders;
    LayoutInflater inflater;
    Context context;
    int imgsize=0;
    public FolderAdapter(List<ImageFolder> folders, Context context){
        this.folders=folders;
        this.context=context;
        inflater=LayoutInflater.from(context);
        imgsize=dip2px(context,96);
    }
    int ckpos=0;
    public void setCheck(int pos){
        ckpos=pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Object getItem(int position) {
        return folders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void displayImage(String url, ImageView view) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(imgsize,imgsize)
                .into(view);

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderFolder holderFolder = null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.listitem_dir_item,null);
            holderFolder=new ViewHolderFolder(convertView);
            convertView.setTag(holderFolder);
        }else{
            holderFolder= (ViewHolderFolder) convertView.getTag();
        }
        ImageFolder folder=folders.get(position);
        displayImage(folder.getFirstImagePath(),holderFolder.iv_folderimage);
        holderFolder.tv_foldername.setText(folder.getName().substring(1));
        holderFolder.tv_foldercount.setText(folder.getCount()+"");
        if(position==ckpos){
            holderFolder.is_checked.setVisibility(View.VISIBLE);
        }else {
            holderFolder.is_checked.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public static  class  ViewHolderFolder {
        ImageView iv_folderimage,is_checked;
        TextView tv_foldername,tv_foldercount;
        public ViewHolderFolder(View convertView){
            is_checked= (ImageView) convertView.findViewById(R.id.is_checked);
            iv_folderimage= (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            tv_foldercount= (TextView) convertView.findViewById(R.id.id_dir_item_count);
            tv_foldername= (TextView) convertView.findViewById(R.id.id_dir_item_name);
        }
    }



}
