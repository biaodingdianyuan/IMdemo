package com.example.liuhaifeng.imdemo;

import android.content.Intent;

import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import io.rong.imlib.IRongCallback.ISendMediaMessageCallback;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static com.example.liuhaifeng.imdemo.Tools.RYSendFile;
import static com.example.liuhaifeng.imdemo.Tools.RYSendImage;
import static com.example.liuhaifeng.imdemo.Tools.RYSendMessage;
import static com.example.liuhaifeng.imdemo.Tools.RYSendVoice;
import static com.example.liuhaifeng.imdemo.Tools.getPath;
import static com.example.liuhaifeng.imdemo.Tools.getPicByUri;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class RYChattingActivity extends AppCompatActivity {
    @InjectView(R.id.btn_ry_image)
    Button btnRyImage;
    @InjectView(R.id.btn_ry_voice)
    Button btnRyVoice;
    @InjectView(R.id.btn_ry_file)
    Button btnRyFile;
    @InjectView(R.id.btn_ry_message)
    Button btnRyMessage;
    private MediaRecorder mr = null;
    int length=0;
    int i=0;
    private TimerTask timerTask;
    private Timer timer;
    Uri VoicePath=null;
    File soundFile;
    private Handler mhandler=new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            length=msg.arg1;
            btnRyVoice.setText(length+"");
            starttime();






        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rychatting);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_ry_image, R.id.btn_ry_voice, R.id.btn_ry_file, R.id.btn_ry_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ry_image:
                Intent intent=new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivity(intent);

                break;
            case R.id.btn_ry_voice:
               if(btnRyVoice.getText().equals("发送语音")){
                   startRecord();
               }else{
                   stopRecord();
                   btnRyVoice.setText("发送语音");
                   RYSendVoice(VoicePath,length+"","");
               }
                break;
            case R.id.btn_ry_file:
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("*/*");
                in.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult( Intent.createChooser(in, "请选择文件"), 1);
                break;
            case R.id.btn_ry_message:
                    RYSendMessage("","");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            Uri uri=data.getData();
            String path=getPath(RYChattingActivity.this,uri);
            RYSendFile(uri,"");

        }else if(requestCode==3){
            if(data!=null){
                Uri uri=data.getData();
                if(uri!=null){
                   String path= getPicByUri(uri,RYChattingActivity.this);
                    if(path==null){
                        Toast.makeText(this,"图片路劲获取失败",Toast.LENGTH_SHORT).show();
                    }else{
                        RYSendImage(uri,RYChattingActivity.this);
                    }

                }


            }
        }




        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startRecord(){
        if(mr == null){
            File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
            if(!dir.exists()){
                dir.mkdirs();
            }
            soundFile = new File(dir,System.currentTimeMillis()+".amr");
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
            VoicePath=Uri.fromFile(soundFile);
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
                android.os.Message message= android.os.Message.obtain();
                message.arg1=i;
                mhandler.sendMessage(message);
            }
        };
        timer.schedule(timerTask,1000);
    }
}
