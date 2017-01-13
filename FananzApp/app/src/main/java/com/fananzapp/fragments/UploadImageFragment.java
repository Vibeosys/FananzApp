package com.fananzapp.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fananzapp.R;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.requestdata.CustomerReqDTO;
import com.fananzapp.data.requestdata.DeletePhotoReqDTO;
import com.fananzapp.data.requestdata.SigninSubReqDTO;
import com.fananzapp.data.requestdata.SigninUserReqDTO;
import com.fananzapp.data.requestdata.UpdatePhotosReqDTO;
import com.fananzapp.data.requestdata.UploadPhotosReqDTO;
import com.fananzapp.data.requestdata.UserRequestDTO;
import com.fananzapp.data.responsedata.ImageDataReqDTO;
import com.fananzapp.utils.CustomVolleyRequestQueue;
import com.fananzapp.utils.DialogUtils;
import com.fananzapp.utils.MultipartUtility;
import com.fananzapp.utils.NetworkUtils;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.fananzapp.utils.UserType;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by akshay on 06-01-2017.
 */
public class UploadImageFragment extends BaseFragment implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    private static final String TAG = UploadImageFragment.class.getSimpleName();
    public static final String IMAGE_DATA = "imageData";
    public static final String PORTFOLIO_ID = "portId";
    public static final String IS_COVER_IMG = "isCoverImg";
    public static final String IS_NEW_DATA = "newData";
    private NetworkImageView img;
    private int EDIT_PROFILE_MEDIA_PERMISSION_CODE = 19;
    private int EDIT_SELECT_IMAGE = 20;
    private String selectedPath = "No Pic";
    private ProgressDialog progressDialog;
    private ImageDataReqDTO imgData;
    private TextView btnDelete, btnChange;//, txtCoverImg;
    private ImageLoader mImageLoader;
    private long portId;
    private boolean isCoverImg, isNewData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = DialogUtils.getFragmentDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_upload_images, container, false);
        Bundle bundle = this.getArguments();
        img = (NetworkImageView) view.findViewById(R.id.img);
        btnDelete = (TextView) view.findViewById(R.id.btnDelete);
        btnChange = (TextView) view.findViewById(R.id.btnChange);
        btnDelete.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        isNewData = bundle.getBoolean(IS_NEW_DATA);
        if (isNewData) {
            portId = bundle.getLong(PORTFOLIO_ID);
            isCoverImg = bundle.getBoolean(IS_COVER_IMG);
            img.setImageResource(R.drawable.default_img);
        } else {
            portId = bundle.getLong(PORTFOLIO_ID);
            imgData = (ImageDataReqDTO) bundle.getSerializable(IMAGE_DATA);
            if (imgData.isCoverImg() == 0) {
                isCoverImg = false;
            } else {
                isCoverImg = true;
            }
            setupUi();
        }
        if (isCoverImg) {
            // txtCoverImg.setVisibility(View.VISIBLE);
        } else {
            // txtCoverImg.setVisibility(View.GONE);
        }
        return view;
    }

    private void setupUi() {
        img.setDefaultImageResId(R.drawable.default_img);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext())
                .getImageLoader();
        String url = "";
        try {
            url = imgData.getPhotoUrl();
        } catch (NullPointerException e) {
            url = "";
        }

        if (url == "null" || url == null || url.equals("") || url == "") {
            img.setDefaultImageResId(R.drawable.default_img);
        } else if (TextUtils.isEmpty(url)) {
            img.setDefaultImageResId(R.drawable.default_img);
        } else if (url != null && !url.isEmpty()) {
            try {
                mImageLoader.get(url, ImageLoader.getImageListener(img,
                        R.drawable.default_img, R.drawable.default_img));
                img.setImageUrl(url, mImageLoader);
            } catch (Exception e) {
                img.setDefaultImageResId(R.drawable.default_img);
            }
        } else {
            img.setDefaultImageResId(R.drawable.default_img);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnChange:
                requestGrantPermission();
                break;
            case R.id.btnDelete:
                if (imgData != null) {
                    if (isCoverImg) {
                        Toast.makeText(getContext(), getString(R.string.str_cannot_delete_cover), Toast.LENGTH_SHORT).show();
                    } else {
                        if (NetworkUtils.isActiveNetworkAvailable(getContext())) {
                            progressDialog.show();
                            DeletePhotoReqDTO deletePhotoReqDTO = new DeletePhotoReqDTO(imgData.getPhotoId());
                            Gson gson = new Gson();
                            String serializedJsonString = gson.toJson(deletePhotoReqDTO);
                            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                            baseRequestDTO.setData(serializedJsonString);
                            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_DELETE_PHOTO,
                                    mSessionManager.deletePhotoUrl(), baseRequestDTO);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.str_err_net_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    private void requestGrantPermission() {

        requestPermissions(new String[]{Manifest.permission.MEDIA_CONTENT_CONTROL,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                EDIT_PROFILE_MEDIA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EDIT_PROFILE_MEDIA_PERMISSION_CODE && grantResults[1] == 0) {
            openGallery();
        } else if (requestCode == EDIT_PROFILE_MEDIA_PERMISSION_CODE && grantResults[1] != 0) {
            Toast toast = Toast.makeText(getActivity(),
                    "User denied permission", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), EDIT_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == EDIT_SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedPath = cursor.getString(columnIndex);
                } else {
                    Toast.makeText(getContext(), "Image not found", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                img.setImageURI(selectedImageUri);
                uploadImage();
            }
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_DELETE_PHOTO:
                Toast.makeText(getContext(), getString(R.string.str_photo_delete_success), Toast.LENGTH_SHORT).show();
                img.setDefaultImageResId(R.drawable.default_img);
                break;
        }
    }

    public class SendPicVerify extends AsyncTask<String, Void, String> {
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String jsonData = params[0];
            String url = params[1];
            Log.d(TAG, "##" + jsonData);
            Log.d(TAG, "##" + url);
            File uploadFile = new File(selectedPath);
            System.out.println("##" + uploadFile.canRead());
            System.out.println("##" + uploadFile.isFile());
            try {
                MultipartUtility multipart = new MultipartUtility(
                        url, "UTF-8");
                multipart.addHeaderField("Content-Type", "multipart/form-data");
                multipart.addFormField("json", jsonData);
                multipart.addFilePart("photo", uploadFile);
                response = multipart.finish();

            } catch (Exception e) {
                Log.e(TAG, "##Error:" + e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "##" + result);
        }

    }

    private void uploadImage() {
        if (isNewData) {
            UploadPhotosReqDTO uploadPhotosReqDTO = new UploadPhotosReqDTO(portId,
                    mSessionManager.getSubId(), mSessionManager.getEmail(),
                    mSessionManager.getPassword(), isCoverImg);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(uploadPhotosReqDTO);
            SendPicVerify sendPic = new SendPicVerify();
            sendPic.execute(serializedJsonString, mSessionManager.uploadPhotos());
        } else {
            UpdatePhotosReqDTO signinSubReqDTO = new UpdatePhotosReqDTO(mSessionManager.
                    getSubId(), mSessionManager.getEmail(), mSessionManager.getPassword(),
                    imgData.getPhotoId());
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(signinSubReqDTO);
            SendPicVerify sendPic = new SendPicVerify();
            sendPic.execute(serializedJsonString, mSessionManager.changePhotos());
        }
    }
}
