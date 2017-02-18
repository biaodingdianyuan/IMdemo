package com.example.liuhaifeng.imdemo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMFileMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static com.avos.avoscloud.AVPersistenceUtils.getCacheDir;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class Tools {
    static boolean flg = false;

    /**
     * 环信
     */
    public static boolean HXSendMessage(String content, String toChatUsername) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        return flg;
    }

    public static boolean HXSendImage(String imagePath, boolean is, String toChatUsername) {

        EMMessage message2 = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        EMClient.getInstance().chatManager().sendMessage(message2);
        return flg;
    }

    public static boolean HXSendVoice(String filePath, int length, String toChatUsername) {
        EMMessage message1 = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);

        EMClient.getInstance().chatManager().sendMessage(message1);

        return flg;
    }

    public static boolean HXSendFile(String filePath, String toChatUsername) {
        EMMessage message3 = EMMessage.createFileSendMessage(filePath, toChatUsername);
        EMClient.getInstance().chatManager().sendMessage(message3);

        return flg;
    }

    /**
     * 融云
     */
    public static boolean RYSendMessage(String content, String toChatUsername) {
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, "1001", TextMessage.obtain("我是消息内容"), null, null,
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer messageId, RongIMClient.ErrorCode e) {
                    }
                    @Override
                    public void onSuccess(Integer integer) {
                    }

                }, new RongIMClient.ResultCallback<Message>() {
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                    @Override
                    public void onSuccess(Message message) {
                    }

                });

        return flg;
    }

    public static boolean RYSendImage(Uri imagePath, Context context) {
//        File imageFileSource = new File(getCacheDir(), "source.jpg");
//        File imageFileThumb = new File(getCacheDir(), "thumb.jpg");
//        try {
//            // 读取图片。
//            InputStream is = context.getAssets().open("emmy.jpg");
//            Bitmap bmpSource = BitmapFactory.decodeStream(is);
//            imageFileSource.createNewFile();
//            FileOutputStream fosSource = new FileOutputStream(imageFileSource);
//            // 保存原图。
//            bmpSource.compress(Bitmap.CompressFormat.JPEG, 100, fosSource);
//            // 创建缩略图变换矩阵。
//            Matrix m = new Matrix();
//            m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);
//            // 生成缩略图。
//            Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);
//            imageFileThumb.createNewFile();
//            FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);
//            // 保存缩略图。
//            bmpThumb.compress(Bitmap.CompressFormat.JPEG, 60, fosThumb);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ImageMessage imgMsg = ImageMessage.obtain(imagePath, imagePath,true);
        Message message = Message.obtain("1001", Conversation.ConversationType.PRIVATE, imgMsg);

        RongIMClient.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, "1001", imgMsg, null, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //保存数据库成功
                Log.d("*****************","保存数据库成功");

            }
            @Override
            public void onError(Message message, RongIMClient.ErrorCode code) {
                //发送失败
                Log.d("*****************","发送失败");
            }
            @Override
            public void onSuccess(Message message) {
                //发送成功
                Log.d("*****************","发送成功");
            }
            @Override
            public void onProgress(Message message, int progress) {
                //发送进度
                Log.d("*****************","发送进度");
            }
        });

        return flg;
    }

    public static boolean RYSendVoice(Uri filePath, String length, String toChatUsername) {
        VoiceMessage vocMsg = VoiceMessage.obtain(filePath, Integer.parseInt(length));
       Message message= Message.obtain("1001", Conversation.ConversationType.PRIVATE,vocMsg);
        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });
        return flg;
    }

    public static boolean RYSendFile(Uri filePath, String toChatUsername) {
        Uri filepath = filePath;
        FileMessage fileMessage=FileMessage.obtain(filepath);
        Message message = Message.obtain("1001", Conversation.ConversationType.PRIVATE, fileMessage);

        RongIMClient.getInstance().sendMediaMessage(message, (String)null, (String)null, new IRongCallback.ISendMediaMessageCallback() {
                    @Override
                    public void onProgress(Message message, int i) {

                    }

                    @Override
                    public void onCanceled(Message message) {

                    }

                    @Override
                    public void onAttached(Message message) {

                    }

                    @Override
                    public void onSuccess(Message message) {
                        Log.d("***","发送成功");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        Log.d("***","发送失败");

                    }
                }

        );


        return flg;
    }

    /**
     * 容联云
     */
    public static boolean RLYSendMessage() {
        // 组建一个待发送的ECMessage
        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
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
                    Log.d("-------------","发送成功");
                    return ;
                }else {
                    Log.d("-------------",error+"");
                }
                // 将发送的消息更新到本地数据库并刷新UI
            }
            @Override
            public void onProgress(String msgId, int totalByte, int progressByte) {
                // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
            }
        });

        return flg;
    }

    public static boolean RLYSendImage(String imagePath) {
        ECMessage msg1 = ECMessage.createECMessage(ECMessage.Type.IMAGE);
        msg1.setTo("8a216da859204cc9015929b9cf1c059b#8018313400000009");
        ECImageMessageBody msgBody1  = new ECImageMessageBody();
        msgBody1.setLocalUrl(imagePath);
        msg1.setBody(msgBody1);
        // 调用SDK发送接口发送消息到服务器
        ECChatManager manager1 = ECDevice.getECChatManager();
        manager1.sendMessage(msg1, new ECChatManager.OnSendMessageListener() {
            @Override
            public void onSendMessageComplete(ECError error, ECMessage message) {
                // 处理消息发送结果
                if(message == null) {
                    Log.d("-------------","发送成功");
                    return ;
                }else{
                    Log.d("-------------",error+"");

                }
                // 将发送的消息更新到本地数据库并刷新UI
            }

            @Override
            public void onProgress(String msgId, int totalByte, int progressByte) {
                // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
            }
        });

        return flg;
    }

    public static boolean RLYSendVoice(String filePath) {
        ECMessage msg2 = ECMessage.createECMessage(ECMessage.Type.VOICE);
        msg2.setTo("8a216da859204cc9015929b9cf1c059b#8018313400000009");
        File file=new File(filePath);
        ECVoiceMessageBody voiceMessage=new ECVoiceMessageBody(file,0);
        msg2.setBody(voiceMessage);

        ECChatManager manager2 = ECDevice.getECChatManager();
        manager2.sendMessage(msg2, new ECChatManager.OnSendMessageListener() {
            @Override
            public void onSendMessageComplete(ECError error, ECMessage message) {
                // 处理消息发送结果
                if(message == null) {
                    Log.d("-------------","发送成功");
                    return ;
                }else{
                    Log.d("-------------",error+"");
                }
                // 将发送的消息更新到本地数据库并刷新UI
            }
            @Override
            public void onProgress(String msgId, int totalByte, int progressByte) {
                // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
            }
        });

        return flg;
    }

    public static boolean RLYSendFile(String filePath) {

        ECMessage msg3 = ECMessage.createECMessage(ECMessage.Type.FILE);
        msg3.setTo("8a216da859204cc9015929b9cf1c059b#8018313400000009");
        ECFileMessageBody msgBody3  = new ECFileMessageBody();
        msgBody3.setFileName("Tony_2015.zip");
        msgBody3.setFileExt("");
        msgBody3.setLocalUrl(filePath);
        msg3.setBody(msgBody3);
        ECChatManager manager3 = ECDevice.getECChatManager();
        manager3.sendMessage(msg3, new ECChatManager.OnSendMessageListener() {
            @Override
            public void onSendMessageComplete(ECError error, ECMessage message) {
                // 处理消息发送结果
                if(message == null) {
                    Log.d("-------------","发送成功");
                    return ;
                }else{
                    Log.d("-------------",error+"");
                }
            }
            @Override
            public void onProgress(String msgId, int totalByte, int progressByte) {
            }
        });
        return flg;
    }

    /**
     * LeanCloud
     */
    public static boolean LCSendMessage() {

        AVIMClient tom = AVIMClient.getInstance("Tom");
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与Jerry之间的对话
                    client.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setText("耗子，起床！");
                                        // 发送消息
                                        conversation.sendMessage(msg, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.d("Tom & Jerry", "发送成功！");
                                                }
                                                else {
                                                    Log.d("Tom & Jerry", e.toString());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }else {
                    Log.d("Tom & Jerry", e.toString());

                }
            }
        });
        return flg;
    }

    public static boolean LCSendImage(final String path) {
        AVIMClient tom1 = AVIMClient.getInstance("Tom");
        tom1.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与Jerry之间的对话
                    client.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVFile file =new AVFile("1111",path, null);
                                        AVIMFileMessage msgs=new AVIMFileMessage(file);
                                        // 发送消息
                                        conversation.sendMessage(msgs, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.d("Tom & Jerry", "发送成功！");
                                                }
                                                else {
                                                    Log.d("Tom & Jerry", e.toString());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }else {
                    Log.d("Tom & Jerry", e.toString());

                }
            }
        });

        return flg;
    }

    public static boolean LCSendVoice(final String localFilePath, String length) {
        AVIMClient t=AVIMClient.getInstance("Tom");
        t.open(new AVIMClientCallback() {
            @Override
            public void done(final AVIMClient avimClient, AVIMException e) {
                if(e==null){
                    avimClient.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation avimConversation, AVIMException e) {
                            if(e==null) {
                                AVFile file = null;
                                try {
                                    file = AVFile.withAbsoluteLocalPath("忐忑.mp3",localFilePath);
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                                AVIMVideoMessage videoMessage = new AVIMVideoMessage(file);
                                avimConversation.sendMessage(videoMessage, new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVIMException e) {
                                        if(e==null){
                                            Log.d("***************","发送成功");
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });

        return flg;
    }

    public static boolean LCSendFile(String filePath) {
        AVIMClient tom1 = AVIMClient.getInstance("Tom");
        tom1.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与Jerry之间的对话
                    client.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVFile file =new AVFile("萌妹子","http://pic2.zhimg.com/6c10e6053c739ed0ce676a0aff15cf1c.gif", null);
                                        AVIMFileMessage msgs=new AVIMFileMessage(file);
                                        // 发送消息
                                        conversation.sendMessage(msgs, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.d("Tom & Jerry", "发送成功！");
                                                }
                                                else {
                                                    Log.d("Tom & Jerry", e.toString());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }else {
                    Log.d("Tom & Jerry", e.toString());

                }
            }
        });


        return flg;
    }


    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getPicByUri(Uri selectedImage,Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;
            return picturePath;


        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
            return "";


            }
           return file.getAbsolutePath();

        }

    }
}
