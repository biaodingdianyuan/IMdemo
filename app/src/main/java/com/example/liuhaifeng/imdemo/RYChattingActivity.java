package com.example.liuhaifeng.imdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

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

                File imageFileSource = new File(getCacheDir(), "source.jpg");
                File imageFileThumb = new File(getCacheDir(), "thumb.jpg");

                try {
                    // 读取图片。
                    InputStream is = getAssets().open("emmy.jpg");

                    Bitmap bmpSource = BitmapFactory.decodeStream(is);

                    imageFileSource.createNewFile();

                    FileOutputStream fosSource = new FileOutputStream(imageFileSource);

                    // 保存原图。
                    bmpSource.compress(Bitmap.CompressFormat.JPEG, 100, fosSource);

                    // 创建缩略图变换矩阵。
                    Matrix m = new Matrix();
                    m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);

                    // 生成缩略图。
                    Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);

                    imageFileThumb.createNewFile();

                    FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);

                    // 保存缩略图。
                    bmpThumb.compress(Bitmap.CompressFormat.JPEG, 60, fosThumb);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ImageMessage imgMsg = ImageMessage.obtain(Uri.fromFile(imageFileThumb), Uri.fromFile(imageFileSource));

/**
 * 发送图片消息。
 *
 * @param conversationType         会话类型。
 * @param targetId                 会话目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
 * @param imgMsg                   消息内容。
 * @param pushContent              接收方离线时需要显示的push消息内容。
 * @param pushData                 接收方离线时需要在push消息中携带的非显示内容。
 * @param SendImageMessageCallback 发送消息的回调。
 */
//                RongIMClient.getInstance().sendImageMessage(conversationType, targetId, imgMsg, pushContent, pushData, new RongIMClient.SendImageMessageCallback() {
//
//                    @Override
//                    public void onAttached(Message message) {
//                        //保存数据库成功
//                    }
//
//                    @Override
//                    public void onError(Message message, RongIMClient.ErrorCode code) {
//                        //发送失败
//                    }
//
//                    @Override
//                    public void onSuccess(Message message) {
//                        //发送成功
//                    }
//
//                    @Override
//                    public void onProgress(Message message, int progress) {
//                        //发送进度
//                    }
//                });

                break;
            case R.id.btn_ry_voice:
                break;
            case R.id.btn_ry_file:
                break;
            case R.id.btn_ry_message:

//                RongIMClient.getInstance().sendMessage(conversationType, targetId, TextMessage.obtain("我是消息内容"), pushContent, pushData,
//                        new RongIMClient.SendMessageCallback() {
//                            @Override
//                            public void onError(Integer messageId, RongIMClient.ErrorCode e) {
//
//                            }
//
//                            @Override
//                            public void onSuccess(Integer integer) {
//
//                            }
//
//                        }, new RongIMClient.ResultCallback<Message>() {
//                            @Override
//                            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                            }
//
//                            @Override
//                            public void onSuccess(Message message) {
//
//                            }
//
//                        });

                break;
        }
    }
}
