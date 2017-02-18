package com.example.liuhaifeng.imdemo;

import android.content.ContentResolver;
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
import android.view.View;
import android.widget.Button;

import com.hyphenate.chat.EMClient;
import com.hyphenate.util.PathUtil;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.R.attr.path;
import static com.example.liuhaifeng.imdemo.Tools.LCSendImage;
import static com.example.liuhaifeng.imdemo.Tools.RLYSendFile;
import static com.example.liuhaifeng.imdemo.Tools.RLYSendImage;
import static com.example.liuhaifeng.imdemo.Tools.RLYSendMessage;
import static com.example.liuhaifeng.imdemo.Tools.RLYSendVoice;
import static com.example.liuhaifeng.imdemo.Tools.getPath;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class RLYChattingActivity extends AppCompatActivity {
    protected File cameraFile;
    private MediaRecorder mr = null;
    int length=0;
    int i=0;
    private TimerTask timerTask;
    private Timer timer;
    private String path=null;
    String VoicePath=null;
        @InjectView(R.id.btn_ply_message)
    Button btnPlyMessage;
    @InjectView(R.id.btn_ply_image)
    Button btnPlyImage;
    @InjectView(R.id.btn_ply_voice)
    Button btnPlyVoice;
    @InjectView(R.id.btn_ply_file)
    Button btnPlyFile;
    private Handler mhandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            btnPlyVoice.setText(msg.arg1+"");
            length=msg.arg1;
            starttime();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plychatting);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_ply_message, R.id.btn_ply_image, R.id.btn_ply_voice, R.id.btn_ply_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ply_message:
                RLYSendMessage();

                break;
            case R.id.btn_ply_image:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 0);


                break;
            case R.id.btn_ply_voice:
                if(btnPlyVoice.getText().equals("发送语言")){
                    startRecord();
                }else {
                    btnPlyVoice.setText("发送语言");
                    stopRecord();
                    RLYSendVoice(VoicePath);
                }
                break;
            case R.id.btn_ply_file:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("*/*");
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult( Intent.createChooser(intent1, "请选择文件"), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;
        ContentResolver resolver = getContentResolver();
        if(requestCode==1){
            Uri uri=data.getData();
            String path=getPath(RLYChattingActivity.this,uri);
            RLYSendFile(path);

        }else if(requestCode==3){

            try {
                Uri originalUri = data.getData(); // 获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
                RLYSendImage(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
