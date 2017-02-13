package com.example.liuhaifeng.imdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuhaifeng on 2017/2/13.
 */

public class LCChattingActivity extends AppCompatActivity {
    @InjectView(R.id.btn_lc_message)
    Button btnLcMessage;
    @InjectView(R.id.btn_lc_image)
    Button btnLcImage;
    @InjectView(R.id.btn_lc_voice)
    Button btnLcVoice;
    @InjectView(R.id.btn_lc_file)
    Button btnLcFile;

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



                break;
            case R.id.btn_lc_image:



                break;
            case R.id.btn_lc_voice:



                break;
            case R.id.btn_lc_file:



                break;
        }
    }
}
