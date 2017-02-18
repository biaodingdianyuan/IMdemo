package com.example.liuhaifeng.imdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
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
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import static com.example.liuhaifeng.imdemo.Tools.LCSendFile;
import static com.example.liuhaifeng.imdemo.Tools.LCSendImage;
import static com.example.liuhaifeng.imdemo.Tools.LCSendMessage;
import static com.example.liuhaifeng.imdemo.Tools.LCSendVoice;
import static com.example.liuhaifeng.imdemo.Tools.getPath;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class LCChattingActivity extends AppCompatActivity {
    private MediaRecorder mr = null;
    int length=0;
    int i=0;
    private TimerTask timerTask;
    private Timer timer;
    @InjectView(R.id.btn_lc_message)
    Button btnLcMessage;
    @InjectView(R.id.btn_lc_image)
    Button btnLcImage;
    @InjectView(R.id.btn_lc_voice)
    Button btnLcVoice;
    @InjectView(R.id.btn_lc_file)
    Button btnLcFile;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final int IMAGE_CODE = 0;
    private String path=null;
    String VoicePath=null;

    private Handler mhandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            btnLcVoice.setText(msg.arg1+"");
            length=msg.arg1;
            starttime();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcchatting);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_lc_message, R.id.btn_lc_image, R.id.btn_lc_voice, R.id.btn_lc_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_lc_message:
              LCSendMessage();
                break;
            case R.id.btn_lc_image:
                setImage1();
                break;
            case R.id.btn_lc_voice:
                if(btnLcVoice.getText().equals("发送语言")){
                        startRecord();
                }else{
                    btnLcVoice.setText("发送语音");
                    stopRecord();
                    LCSendVoice(VoicePath,null);
                }
                break;
            case R.id.btn_lc_file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult( Intent.createChooser(intent, "请选择文件"), 1);


                break;
        }

}
    private void setImage1() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, IMAGE_CODE);
    }

    private void setImage() {
        // TODO Auto-generated method stub
        // 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片

        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);

        getAlbum.setType(IMAGE_UNSPECIFIED);

        startActivityForResult(getAlbum, IMAGE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;
        ContentResolver resolver = getContentResolver();
        if (requestCode == IMAGE_CODE) {
            try {
                Uri originalUri = data.getData(); // 获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                 path = cursor.getString(column_index);
              LCSendImage(path);
            } catch (IOException e) {
                Log.e("TAG-->Error", e.toString());
            }
            finally {
                return;
            }
        }else if (requestCode==1){
            Uri uri = data.getData();
            LCSendFile( getPath(this, uri));
        }
        super.onActivityResult(requestCode, resultCode, data);
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