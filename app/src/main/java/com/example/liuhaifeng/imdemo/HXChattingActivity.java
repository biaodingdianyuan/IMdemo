package com.example.liuhaifeng.imdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.PathUtil;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class HXChattingActivity extends AppCompatActivity {

    protected static final int REQUEST_CODE_CAMERA = 2;
    protected File cameraFile;
    protected static final int REQUEST_CODE_LOCAL = 3;

    @InjectView(R.id.btn_send_message)
    Button btnSendMessage;
    @InjectView(R.id.btn_send_image)
    Button btnSendImage;
    @InjectView(R.id.btn_send_Voice)
    Button btnSendVoice;
    @InjectView(R.id.btn_send_File)
    Button btnSendFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_hxchatting);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.btn_send_message, R.id.btn_send_image, R.id.btn_send_Voice, R.id.btn_send_File})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_message:
                EMMessage message = EMMessage.createTxtSendMessage("这是文本消息", "111111");

                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                break;
            case R.id.btn_send_image:
                cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                        + System.currentTimeMillis() + ".jpg");
                //noinspection ResultOfMethodCallIgnored
                cameraFile.getParentFile().mkdirs();
                startActivityForResult(
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                    REQUEST_CODE_CAMERA);

                //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true

                break;
            case R.id.btn_send_Voice:
               // EMMessage message1 = EMMessage.createVoiceSendMessage(filePath, length, "111111");

               // EMClient.getInstance().chatManager().sendMessage(message1);
                break;
            case R.id.btn_send_File:
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("txt/*");

                } else {
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                startActivityForResult(intent, REQUEST_CODE_LOCAL);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            }


        }
    }

    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = HXChattingActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {

                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {

                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }
    protected void sendImageMessage(String imagePath) {
        EMMessage message2 = EMMessage.createImageSendMessage(imagePath, false, "111111");
        EMClient.getInstance().chatManager().sendMessage(message2);
    }
    protected void sendFileByUri(Uri uri){
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;

            try {
                cursor = HXChattingActivity.this.getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
           // Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        //limit the size < 10M
        if (file.length() > 10 * 1024 * 1024) {
         //   Toast.makeText(getActivity(), R.string.The_file_is_not_greater_than_10_m, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message3 = EMMessage.createFileSendMessage(filePath,"111111");
        EMClient.getInstance().chatManager().sendMessage(message3);
    }
}

//融云