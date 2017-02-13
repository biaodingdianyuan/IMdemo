package com.example.liuhaifeng.imdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class RLYChattingActivity extends AppCompatActivity {
    protected File cameraFile;

        @InjectView(R.id.btn_ply_message)
    Button btnPlyMessage;
    @InjectView(R.id.btn_ply_image)
    Button btnPlyImage;
    @InjectView(R.id.btn_ply_voice)
    Button btnPlyVoice;
    @InjectView(R.id.btn_ply_file)
    Button btnPlyFile;

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

                // 组建一个待发送的ECMessage
                ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
                //如果需要跨应用发送消息，需通过appkey+英文井号+用户帐号的方式拼接，发送录音、发送群组消息等与此方式一致。
                //例如：appkey=20150314000000110000000000000010
//                帐号ID=john
//                传入帐号=20150314000000110000000000000010#john
                //msg.setTo(""appkey#John的账号");
                // 设置消息接收者
                msg.setTo("8a216da859204cc9015929b9cf1c059b#8018313400000009");

                // 创建一个文本消息体，并添加到消息对象中
                ECTextMessageBody msgBody = new ECTextMessageBody("这是文本消息");

                // 将消息体存放到ECMessage中
                msg.setBody(msgBody);
                // 调用SDK发送接口发送消息到服务器
                ECChatManager manager = ECDevice.getECChatManager();
                manager.sendMessage(msg, new ECChatManager.OnSendMessageListener() {
                    @Override
                    public void onSendMessageComplete(ECError error, ECMessage message) {
                        // 处理消息发送结果
                        if(message == null) {
                            return ;
                        }
                        // 将发送的消息更新到本地数据库并刷新UI
                    }

                    @Override
                    public void onProgress(String msgId, int totalByte, int progressByte) {
                        // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                    }
                });

                break;
            case R.id.btn_ply_image:
                ECMessage msg1 = ECMessage.createECMessage(ECMessage.Type.IMAGE);

                ECImageMessageBody msgBody1  = new ECImageMessageBody();
                // 设置附件名
                msgBody1.setFileName("Tony_2015.jpg");
                // 设置附件扩展名
                msgBody1.setFileExt("jpg");
                // 设置附件本地路径
                msgBody1.setLocalUrl("");
                msg1.setBody(msgBody1);
                // 调用SDK发送接口发送消息到服务器
                ECChatManager manager1 = ECDevice.getECChatManager();
                manager1.sendMessage(msg1, new ECChatManager.OnSendMessageListener() {
                    @Override
                    public void onSendMessageComplete(ECError error, ECMessage message) {
                        // 处理消息发送结果
                        if(message == null) {
                            return ;
                        }
                        // 将发送的消息更新到本地数据库并刷新UI
                    }

                    @Override
                    public void onProgress(String msgId, int totalByte, int progressByte) {
                        // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                    }
                });

                break;
            case R.id.btn_ply_voice:
                ECMessage msg2 = ECMessage.createECMessage(ECMessage.Type.VOICE);
                File file=null;
                ECVoiceMessageBody voiceMessage=new ECVoiceMessageBody(file,0);
                msg2.setBody(voiceMessage);

                ECChatManager manager2 = ECDevice.getECChatManager();
                manager2.sendMessage(msg2, new ECChatManager.OnSendMessageListener() {
                    @Override
                    public void onSendMessageComplete(ECError error, ECMessage message) {
                        // 处理消息发送结果
                        if(message == null) {
                            return ;
                        }
                        // 将发送的消息更新到本地数据库并刷新UI
                    }

                    @Override
                    public void onProgress(String msgId, int totalByte, int progressByte) {
                        // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                    }
                });
                break;
            case R.id.btn_ply_file:
                ECMessage msg3 = ECMessage.createECMessage(ECMessage.Type.FILE);

                ECFileMessageBody msgBody3  = new ECFileMessageBody();
                // 设置附件名
                msgBody3.setFileName("Tony_2015.zip");
                // 设置附件扩展名
                msgBody3.setFileExt("");
                // 设置附件本地路径
                msgBody3.setLocalUrl("../Tony_2015.zip");
                // 设置附件长度
               // msgBody3.setLength("");

                // 将消息体存放到ECMessage中
                msg3.setBody(msgBody3);
                // 调用SDK发送接口发送消息到服务器
                ECChatManager manager3 = ECDevice.getECChatManager();
                manager3.sendMessage(msg3, new ECChatManager.OnSendMessageListener() {
                    @Override
                    public void onSendMessageComplete(ECError error, ECMessage message) {
                        // 处理消息发送结果
                        if(message == null) {
                            return ;
                        }
                        // 将发送的消息更新到本地数据库并刷新UI
                    }

                    @Override
                    public void onProgress(String msgId, int totalByte, int progressByte) {
                        // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
