package me.xihuxiaolongren.photoga;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import me.xihuxiaolong.library.utils.GridSpacingItemDecoration;
import me.xihuxiaolongren.photoga.mode.ImageFolder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by wangqiong on 15/3/27.
 * Updated by yxl on 12/5 2015.
 */
public class PhotoGalleryFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    private static final int RC_STORTAGE = 123;
    private static final int RC_SETTINGS_SCREEN = 125;
    public int maxChoseCount = 9;
    public boolean isNeedCamera = false;
    public boolean isNeedCrop = false;

    View rootView;
    RecyclerView mRecyclerView;
    View bottomView;
    TextView openGallery;
    TextView preview;

    PhotoAdapter adapter;

    List<String> currentImages = new ArrayList<>();
    private HashMap<String, ImageFolder> mHashImageFolders = new LinkedHashMap<>();

    int totalCount = 0;

    ListPopupWindow popupWindow;
    FolderAdapter folderAdapter;

    int choseMode = 1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImagePreviewFragemnt.
     */
    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photogallery_layout, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        bottomView = rootView.findViewById(R.id.bottomView);
        openGallery = (TextView) rootView.findViewById(R.id.open_gallery);
        preview = (TextView) rootView.findViewById(R.id.preview);
        if (adapter == null) {
            adapter = new PhotoAdapter(getActivity(), currentImages, choseMode);
            adapter.setNeedCamera(isNeedCamera);
            adapter.setMaxChoseCount(maxChoseCount);
        }
        int spacing = 4; // px
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacing, false));
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        openGallery.setText("所有图片");

        loadAllImages();
        initFolderPop();
        return rootView;
    }

    boolean isshowing = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isshowing) {
                    isshowing = false;
                    popupWindow.dismiss();
                } else {
                    isshowing = true;
                    popupWindow.show();
                }
            }
        });
    }


    public void log(String msg) {
        Log.i("gallery", msg);
    }

    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @AfterPermissionGranted(RC_STORTAGE)
    public void loadAllImages() {
        String perm = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(getContext(), perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_read_storage),
                    RC_STORTAGE, perm);
            return;
        }
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageFolder allImagesFolder = new ImageFolder();
        allImagesFolder.setDir("/所有图片");
        allImagesFolder.setImageList(currentImages);
        mHashImageFolders.put("", allImagesFolder);

        //只查询指定几种类型的图片
        String projection[] = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE +
                "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
        String selectionArgs[] = {"image/jpg", "image/jpeg", "image/png", "image/gif"};
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        if (cursor == null)
            cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, projection,
                    selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String photopPath = cursor.getString(dataColumn);
                File file = new File(photopPath);
                if (photopPath != null && file.exists()) {
                    allImagesFolder.getImageList().add(photopPath);
                    // 获取该图片的父路径名
                    String dirPath = file.getParentFile().getAbsolutePath();
                    if (mHashImageFolders.containsKey(dirPath)) {
                        mHashImageFolders.get(dirPath).getImageList().add(photopPath);
                    } else {
                        ImageFolder imageFolder = new ImageFolder();
                        imageFolder.setDir(dirPath);
                        imageFolder.setFirstImagePath(photopPath);
                        List<String> imageList = new ArrayList<>();
                        imageList.add(photopPath);
                        imageFolder.setImageList(imageList);
                        totalCount++;
                        mHashImageFolders.put(dirPath, imageFolder);
                    }
                }
            } while (cursor.moveToNext());
            allImagesFolder.setFirstImagePath(allImagesFolder.getImageList().get(0));
            adapter.setImageses(currentImages);
        }
        if (cursor != null)
            cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initFolderPop() {
        popupWindow = new ListPopupWindow(getActivity());
        popupWindow.setAnimationStyle(R.style.popup_animation_bottom);
        folderAdapter = new FolderAdapter(new ArrayList<>(mHashImageFolders.values()), getActivity());
        popupWindow.setAdapter(folderAdapter);
        int sWidthPix = getResources().getDisplayMetrics().widthPixels;
//        popupWindow.setContentWidth(sWidthPix);
        popupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setHeight(sWidthPix + 100);
        popupWindow.setAnchorView(bottomView);
        openGallery.setEnabled(true);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageFolder folder = (ImageFolder) parent.getAdapter().getItem(position);
                folderAdapter.setCheck(position);
                currentImages = folder.getImageList();
                adapter.setImageses(currentImages);
                mRecyclerView.setAdapter(adapter);
                openGallery.setText(folder.getName());
                popupWindow.dismiss();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle == null) return;
        choseMode = bundle.getInt("chose_mode");
        maxChoseCount = bundle.getInt("max_chose_count");
        isNeedCamera = (boolean) bundle.get("need_camera");
        isNeedCrop = (boolean) bundle.get("crop");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.rationale_read_storage_ask_again))
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

}
