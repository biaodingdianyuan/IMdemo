package com.example.liuhaifeng.imdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.HX)
    LinearLayout HX;
    @InjectView(R.id.RLY)
    LinearLayout RLY;
    @InjectView(R.id.RY)
    LinearLayout RY;
    @InjectView(R.id.LC)
    LinearLayout LC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }




    @OnClick({R.id.HX, R.id.RLY, R.id.RY, R.id.LC})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.HX:
                //登陆

                EMClient.getInstance().login("000000","0000000",new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {

                        startActivity(new Intent(MainActivity.this,HXChattingActivity.class));
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登录聊天服务器失败！"+message);
                    }
                });
                break;
            case R.id.RLY:
                ECInitParams params = ECInitParams.createParams();
                params.setUserid("8018313400000007");
                params.setPwd("1ONqymZF");
                params.setAppKey("8a216da859204cc9015929b9cf1c059b");
                params.setToken("01ab28ade1f09a72247de099cd345743");
                //设置登陆验证模式：自定义登录方式
                params.setAuthType(ECInitParams.LoginAuthType.PASSWORD_AUTH);
                //LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO。使用方式详见注意事项）
                params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
                if(params.validate()) {
                    // 登录函数
                    ECDevice.login(params);
                }
                Initialized();
                break;
            case R.id.RY:
                RongIMClient.getInstance().connect("9xWdH062N6iCrmDZ5/HiDckw6NwCnGCkgc85gkUmVDEfOmBQMqBKVVAh7NjkcWEeKlZeLq2gmTVJ5zylVRJFdg==", new RongIMClient.ConnectCallback(){
                    @Override
                    public void onSuccess(String s) {
                        startActivity(new Intent(MainActivity.this,RYChattingActivity.class));
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                            Log.d("*********",errorCode.toString());
                    }

                    @Override
                    public void onTokenIncorrect() {

                    }
                });
                break;
            case R.id.LC:
                startActivity(new Intent(MainActivity.this,LCChattingActivity.class));
                break;
        }

    }
    public void Initialized(){
        //判断SDK是否已经初始化
        if(!ECDevice.isInitialized()) {

            ECDevice.initial(MainActivity.this, new ECDevice.InitListener() {
                @Override
                public void onInitialized() {
                    // SDK已经初始化成功
                    ;
                    //设置登录回调监听
                    ECDevice.setOnDeviceConnectListener(new ECDevice.OnECDeviceConnectListener() {
                        @Override
                        public void onConnect() {

                            startActivity(new Intent(MainActivity.this,RLYChattingActivity.class));

                        }
                        @Override
                        public void onDisconnect(ECError ecError) {

                        }
                        @Override
                        public void onConnectState(ECDevice.ECConnectState ecConnectState, ECError ecError) {

                        }
                    });

                }
                @Override
                public void onError(Exception exception) {
                    //在初始化错误的方法中打印错误原因
                    Log.i("","初始化SDK失败"+exception.getMessage());
                }
            });
        }

    }
}
