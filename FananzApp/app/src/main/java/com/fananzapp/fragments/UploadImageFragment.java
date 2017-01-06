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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fananzapp.R;
import com.fananzapp.utils.MultipartUtility;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by akshay on 06-01-2017.
 */
public class UploadImageFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = UploadImageFragment.class.getSimpleName();
    private ImageView img;
    private ProgressBar progressDialog;
    private int EDIT_PROFILE_MEDIA_PERMISSION_CODE = 19;
    private int EDIT_SELECT_IMAGE = 20;
    private String selectedPath = "No Pic";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_upload_images, container, false);
        img = (ImageView) view.findViewById(R.id.img);
        img.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.img:
                requestGrantPermission();
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
                    //boooo, cursor doesn't have rows ...
                }
                cursor.close();
                img.setImageURI(selectedImageUri);
                SendPicVerify sendPic = new SendPicVerify();
                sendPic.execute();
            }
        }
    }

    public class SendPicVerify extends AsyncTask<Void, Void, String> {

        String strResponce;
        JSONObject jsObjData;
        String strData;
        JSONObject jsObjResult;
        String strResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            File uploadFile = new File(selectedPath);
            System.out.println("##" + uploadFile.canRead());
            System.out.println("##" + uploadFile.isFile());
            try {
                MultipartUtility multipart = new MultipartUtility(
                        "http://192.168.1.6:8085/addportfoliophotos", "UTF-8");
                multipart.addHeaderField("Content-Type", "multipart/form-data");
                multipart.addFormField("json", "{\"portfolioId\": \"4\", \"subscriberId\" : \"1123\", \"emailId\" : \"mak@vibeosys.com\", \"password\": \"password\", \"isCoverImageUpload\" : \"true\"}");
                multipart.addFilePart("photo", uploadFile);
                String response = multipart.finish();
                System.out.println("SERVER REPLIED:" + response);
                jsObjData = new JSONObject(response);
                strResult = jsObjData.getString("Status");
                System.out.println("Status:" + strResult);
                Log.i(TAG, "" + multipart);
            } catch (Exception e) {
                Log.e(TAG, "##Error:" + e);
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            Log.d(TAG, "##" + result);
        }

    }
}
