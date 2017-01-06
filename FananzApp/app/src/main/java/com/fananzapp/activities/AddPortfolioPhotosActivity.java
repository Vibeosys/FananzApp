package com.fananzapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fananzapp.R;
import com.fananzapp.fragments.PortfolioListFragment;
import com.fananzapp.fragments.UploadImageFragment;
import com.fananzapp.utils.BaseVolleyRequest;
import com.fananzapp.utils.MultipartUtility;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPortfolioPhotosActivity extends BaseActivity {

    private Button btnUpload, btnSend;
    ImageView img;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Evote";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String TAG = "ImageVerifyActivity";
    private Uri imageUri;
    String youFilePath = "No Pic";
    private FrameLayout fragmentFrameLay, fragmentFrameLay1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_photos);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        btnSend = (Button) findViewById(R.id.btn_send);
        img = (ImageView) findViewById(R.id.img);
        fragmentFrameLay = (FrameLayout) findViewById(R.id.fragment_frame_lay);
        fragmentFrameLay = (FrameLayout) findViewById(R.id.fragment_frame_lay1);
        UploadImageFragment upl = new UploadImageFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_frame_lay, upl, "Upload").commit();
        UploadImageFragment upl1 = new UploadImageFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_frame_lay1, upl1, "Upload").commit();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendPicVerify sendPic = new SendPicVerify();
                sendPic.execute();
            }
        });
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        System.out.println("url:" + imageUri);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Exicuting after gallary
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("requet" + requestCode + "result" + resultCode);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("result image");
                System.out.println("imageUri:" + imageUri.getPath());
                img.setImageURI(imageUri);
                String fullPath = imageUri.toString();
                int start = fullPath.indexOf("s");
                youFilePath = fullPath.substring(start - 1);
                System.out.println("##:" + youFilePath);
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
            progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            File uploadFile = new File(youFilePath);
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
            progressDialog.dismiss();
            Log.d(TAG, "##" + result);
        }

    }
}
