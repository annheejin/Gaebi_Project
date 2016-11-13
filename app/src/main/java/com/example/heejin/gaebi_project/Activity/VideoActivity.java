package com.example.heejin.gaebi_project.Activity;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.heejin.gaebi_project.Gaebi;
//import com.example.heejin.gaebi_project.MainActivity;
import com.example.heejin.gaebi_project.R;
import com.example.heejin.gaebi_project.util.VoiceManager;

public class VideoActivity extends Activity implements VoiceManager.VoiceInterface {
    private Gaebi mApplication;
    private String[] mStrings;
    private VideoView videoView;
    private MediaController mediaController;
    private boolean isFirstStart = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mApplication = Gaebi._instance;
        videoView = (VideoView) findViewById(R.id.videoView);
        mApplication.getmVoiceManager().setmListner(this);
        Toast.makeText(VideoActivity.this, "video ", Toast.LENGTH_SHORT).show();
        // 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
        mediaController = new MediaController(this);
        mStrings = getResources().getStringArray(R.array.path);

        // 비디오뷰에 연결
        mediaController.setAnchorView(videoView);

        // "개비" 불렀을 경우 출력하는 동영상 ("개비" ) 이부분이 계속 돌아 갈수 있게 하는 부분
        //영상 1 : 길 안내 서비스 설명
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test1);

        //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용
        videoView.setMediaController(mediaController);

        //비디오뷰에 재생할 동영상주소를 연결
        videoView.setVideoURI(video);

        //비디오뷰를 포커스하도록 지정
        videoView.requestFocus();

        //동영상 재생
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (!isFirstStart) {
                    mApplication.getmVoiceManager().startListening(VideoActivity.this, mStrings);
                    isFirstStart = true;
                } else {
                    startActivity(new Intent(VideoActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });

    }//onCreate End

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VoiceManager.FAILED_RECOGNIZE:
                    mApplication.getmVoiceManager().stopListening();
                    mApplication.getmVoiceManager().startListening(VideoActivity.this, mStrings);
                    break;
                case VoiceManager.SUCCEED_RECOGNIZE:
                    for (int i=0; i<mStrings.length; i++){
                        if(mStrings[i].equals(recognizeStr)){
                            Uri video = Uri.parse("android.resource://"+getPackageName()+"/"+mApplication.getmVoiceManager().getmPathList().get(i));
                            videoView.setMediaController(mediaController);
                            videoView.setVideoURI(video);
                            videoView.requestFocus();
                            videoView.start();
                            mApplication.getmVoiceManager().setRecognize(true);
                        }
                    }
                    //경로 음성입력받은 경로에 해당하는 동영상 출력
                    //예) "정문" 음성입력 이면 -> "정문"에 해당하는 동영상 출력
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mApplication.getmVoiceManager().setmListner(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplication.getmVoiceManager().stopListening();
    }
    @Override
    public void OnError() {
        mHandler.sendEmptyMessageDelayed(VoiceManager.FAILED_RECOGNIZE, 400);
    }
    private String recognizeStr;
    @Override
    public void OnResult(String str) {
        recognizeStr = str;
        mHandler.sendEmptyMessageDelayed(VoiceManager.SUCCEED_RECOGNIZE,400);//핸들러를 이용한 타이머 이벤트 , 타이머에 따라 과정을 반복함 1000=1초
    }




}
