package com.example.heejin.gaebi_project.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;
import com.example.heejin.gaebi_project.R;
import java.util.ArrayList;


public class VoiceManager {
    private Activity mActivity;
    private SpeechRecognizer mRecognizer;
    private boolean isRecognize = false;
    private String[] mStrings;
    public static final int FAILED_RECOGNIZE = 1;
    public static final int SUCCEED_RECOGNIZE = 2;
    private ArrayList<Integer> mPathList = new ArrayList<>();

    public VoiceManager() {
        mPathList.add(R.raw._01_main_door);
        mPathList.add(R.raw._02_gachon);
        mPathList.add(R.raw._03_art);
        mPathList.add(R.raw._04_bio);
        mPathList.add(R.raw._05_engineer);
        mPathList.add(R.raw._06_center_library);
        mPathList.add(R.raw._07_hackgundan);
        mPathList.add(R.raw._08_student);
        mPathList.add(R.raw._09_art2);
        mPathList.add(R.raw._10_edu);
        mPathList.add(R.raw._11_global);
        mPathList.add(R.raw._12_law);
        mPathList.add(R.raw._13_vision);
        mPathList.add(R.raw._14_bio2);
        mPathList.add(R.raw._15_medi);
        mPathList.add(R.raw._16_engineer2);
        mPathList.add(R.raw._17_sanhack);
        mPathList.add(R.raw._18_graduate);
        mPathList.add(R.raw._19_domitory);
        mPathList.add(R.raw._20_stadium);
    }//VoiceManager() End

    public void startListening(Activity activity, String[] strings)
    {
        mActivity = activity;
        mStrings = strings;
        isRecognize = false;
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   //음성인식 intent생성
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mActivity.getPackageName()); //데이터 설정
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);    //음성인식 객체
        mRecognizer.setRecognitionListener(mSTTListener);          //음성인식 리스너 등록
        mRecognizer.startListening(i);
    }

    public void stopListening()
    {
        if(mRecognizer != null)
            mRecognizer.stopListening();
    }

    private RecognitionListener mSTTListener = new RecognitionListener(){

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.i("onReadyForSpeech","onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.i("onBeginningOfSpeech","onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float v) {
            Log.i("onRmsChanged","onRmsChanged : "+v);
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.i("onBufferReceived","onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.i("end of speech","end of speech");
            if(!isRecognize){
                mListner.OnError();
            }
        }

        @Override
        public void onError(int i) {
            Log.e("error Log"," error no : "+i);
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> strs =  bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for(String s : strs){
                Log.i("String TAG" , " recognize String : "+s);
                for(String s1:mStrings){
                    if(s.contains(s1)){
                        Toast.makeText(mActivity,s1,Toast.LENGTH_SHORT).show();
                        isRecognize=true;   //비교한 문자값이 맞으면 True
                        stopListening();
                        mListner.OnResult(s1);
                        break;
                    }
                }
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    public interface VoiceInterface{
        public void OnError();
        public void OnResult(String str);
    }
    private VoiceInterface mListner;
    public void setmListner(VoiceInterface listner){
        mListner = listner;
    }

    public ArrayList<Integer> getmPathList() {
        return mPathList;
    }

    public void setRecognize(boolean isRecognize){
        isRecognize = isRecognize;
    }
}
