package com.example.liuhaifeng.imdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.liuhaifeng.imdemo.Tools.HXSendFile;
import static com.example.liuhaifeng.imdemo.Tools.HXSendImage;
import static com.example.liuhaifeng.imdemo.Tools.HXSendMessage;
import static com.example.liuhaifeng.imdemo.Tools.HXSendVoice;
import static com.example.liuhaifeng.imdemo.Tools.getPath;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class HXChattingActivity extends AppCompatActivity {

    protected static final int REQUEST_CODE_CAMERA = 2;
    protected File cameraFile;
    protected static final int REQUEST_CODE_LOCAL = 3;
    private MediaRecorder mr = null;
    int length=0;
    int i=0;
    private TimerTask timerTask;
    private Timer timer;
    String VoicePath=null;
    @InjectView(R.id.btn_send_message)
    Button btnSendMessage;
    @InjectView(R.id.btn_send_image)
    Button btnSendImage;
    @InjectView(R.id.btn_send_File)
    Button btnSendFile;
    @InjectView(R.id.btn_send_Voice)
    Button btnSendVoice;
    private Handler mhandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            btnSendVoice.setText(msg.arg1+"");
            length=msg.arg1;
            starttime();
        }
    };
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
                HXSendMessage("这是文本信息","111111");
                break;
            case R.id.btn_send_image:
                cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                        + System.currentTimeMillis() + ".jpg");
                //noinspection ResultOfMethodCallIgnored
                cameraFile.getParentFile().mkdirs();
                startActivityForResult(
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                        REQUEST_CODE_CAMERA);
                break;
            case R.id.btn_send_Voice:
                    if(btnSendVoice.getText().equals("开始录音")){
                        startRecord();
                    }else {
                        btnSendVoice.setText("开始录音");
                        stopRecord();
                        HXSendVoice(VoicePath,length,"111111");
                     }
                break;
            case R.id.btn_send_File:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult( Intent.createChooser(intent, "请选择文件"), 1);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
               HXSendImage(cameraFile.getAbsolutePath(),false,"111111");
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        getPicByUri(selectedImage);
                    }
                }
            }else if(requestCode==1){
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = getPath(this, uri);
                    HXSendFile(path,"111111");
                }
            }
        }
    }
    protected void getPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
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
            HXSendImage(picturePath,false,"111111");
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {

                return;

            }
            HXSendImage(file.getAbsolutePath(),false,"111111");

        }

    }
    //开始录制
    private void startRecord(){
        if(mr == null){
            File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
            if(!dir.exists()){
                dir.mkdirs();
            }
            File soundFile = new File(dir,System.currentTimeMillis()+".amr");
            if(!soundFile.exists()){
                try {
                    soundFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mr = new MediaRecorder();
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);  //音频输入源
            mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);   //设置输出格式
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);   //设置编码格式
            mr.setOutputFile(soundFile.getAbsolutePath());
            VoicePath=soundFile.getAbsolutePath();
            try {
                mr.prepare();
                mr.start();
                starttime();
                //开始录制
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
    //停止录制，资源释放
    private void stopRecord(){
        if(mr != null){
            mr.stop();
            mr.release();
            mr = null;
            timer.cancel();
            i=0;
        }
    }
    public void starttime(){
        if(timer==null){
            timer=new Timer();
        }
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                i++;
                Message message=Message.obtain();
                message.arg1=i;
                mhandler.sendMessage(message);
            }
        };
        timer.schedule(timerTask,1000);
    }

}

