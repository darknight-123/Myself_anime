package com.example.myapplication;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.github.rongi.rotate_layout.layout.RotateLayout;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Play extends AppCompatActivity implements View.OnClickListener {


    //private ExoPlayer exoPlayer;

    private String m3u8_url;
    private Button download_btn;
    private SimpleExoPlayer player;
    private ArrayList<Link>playlist;
    private PlayerView playerview;
    private String vid="";
    private String video_id="";
    private String tid="";
    private ImageView fullScreen;
    private boolean isFullScreen=false;
    private ScrollView scroll;
    private RotateLayout rotateLayout;
    private String title;
    private ArrayList<Button> buttonArrayList=new ArrayList<Button>();
    private int current_button=-1;
    private SqlDataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_play);

        playerview=findViewById(R.id.playerview);
        fullScreen=playerview.findViewById(R.id.exo_fullscreen_icon);
        scroll=findViewById(R.id.play_scrollvew);
        fullScreen.setOnClickListener(this);
        rotateLayout=findViewById(R.id.rotateview);
        download_btn=findViewById(R.id.downlaod_btn);
        download_btn.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        title=bundle.getString("title");
        playlist= (ArrayList<Link>) bundle.getSerializable("myarraylist");
        player=new SimpleExoPlayer.Builder(this).build();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        db = new SqlDataBaseHelper(this);
        // 獲取螢幕的寬度
        int screenWidth = displayMetrics.widthPixels;
        playerview.setPlayer(player);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_READY) {

                    if (playWhenReady) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        // 播放中
                    } else {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        // 暫停中
                        // 在這裡執行按下暫停鍵後的相關處理
                    }
                    // 播放就緒
                } else if (playbackState == Player.STATE_ENDED) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    // 播放結束
                } else if (playbackState == Player.STATE_IDLE) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    // 空閒狀態
                }
            }


        });

        LinearLayout ll_test;
        ll_test = findViewById(R.id.ll_test);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(0,5,0,5);


        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(lp1);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((screenWidth/5)
                , LinearLayout.LayoutParams.MATCH_PARENT);

        lp2.rightMargin=5;

        for (int i=0;i<playlist.size();i++) {
            Button btn = new Button(this);
            btn.setTextSize(12);


            String title="";
            if(playlist.get(i).getEp().charAt(0)!='第'){
                title+=playlist.get(i).getEp().charAt(0);
            }
            for(int t=1;t<playlist.get(i).getEp().length();t++){
                if(playlist.get(i).getEp().charAt(t)!='話'){
                    title+=playlist.get(i).getEp().charAt(t);
                }
                else{
                    break;
                }
            }

            btn.setText(title);
            btn.setLayoutParams(lp2);
            btn.setId(i);

            btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.gray));
            buttonArrayList.add(btn);

            if(i%5==0){

                linearLayout=new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                linearLayout.setLayoutParams(lp1);
            }

            btn.setOnClickListener(this);
            linearLayout.addView(btn);
            if(i%5==0){
                ll_test.addView(linearLayout);
            }


        }

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        player.stop();
        // 在這裡執行當用戶按下 Home 鍵時的處理
        // 例如，暫停播放器、保存狀態等
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(isFullScreen){
                scroll.setVisibility(View.VISIBLE);

                //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                if(getSupportActionBar()!=null){
                    getSupportActionBar().show();
                }


                ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams)rotateLayout.getLayoutParams();
                params.width=params.MATCH_PARENT;
                params.height=(int)(300*getApplicationContext().getResources().getDisplayMetrics().density);
                rotateLayout.setAngle(0);
                rotateLayout.setLayoutParams(params);
                isFullScreen=false;
            }
            else{
                if(player.getMediaItemCount()!=0){
                    player.clearMediaItems();
                }
                finish();
                return super.onKeyDown(keyCode, event);
            }

        }

        return false;
    }


    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.exo_fullscreen_icon){
            if(isFullScreen){
                scroll.setVisibility(View.VISIBLE);
                download_btn.setVisibility(View.VISIBLE);
                //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                if(getSupportActionBar()!=null){
                    getSupportActionBar().show();
                }


                ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams)rotateLayout.getLayoutParams();
                params.width=params.MATCH_PARENT;
                params.height=(int)(300*getApplicationContext().getResources().getDisplayMetrics().density);
                rotateLayout.setAngle(0);
                rotateLayout.setLayoutParams(params);

                View decorView = getWindow().getDecorView();
                int flags = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(flags);
                isFullScreen=false;

            }
            else{
                scroll.setVisibility(View.GONE);
                download_btn.setVisibility(View.GONE);
                //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams)rotateLayout.getLayoutParams();
                params.width=params.MATCH_PARENT;
                params.height=params.MATCH_PARENT;

                //playerview.setLayoutParams(params);
                rotateLayout.setLayoutParams(params);
                rotateLayout.setAngle(90);
                if(getSupportActionBar()!=null){
                    getSupportActionBar().hide();
                }
                View decorView = getWindow().getDecorView();
                int flags = View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(flags);
                isFullScreen=true;
            }
        }
        else if(view.getId()==R.id.downlaod_btn){
            if(current_button!=-1) {

                Toast.makeText(this, "下載中", Toast.LENGTH_SHORT).show();
                db.insertData(title, playlist.get(current_button).getEp(), getFilesDir() + "/" + vid + tid + video_id + ".mp4","下載中");
                threadRun();
            }
            else{
                Toast.makeText(this,"請選擇集數",Toast.LENGTH_SHORT).show();
            }

        }
        else{
            if(current_button!=-1){
                buttonArrayList.get(current_button).setBackgroundColor(buttonArrayList.get(current_button).getContext().getResources().getColor(R.color.gray));
            }
            buttonArrayList.get(view.getId()).setBackgroundColor(buttonArrayList.get(view.getId()).getContext().getResources().getColor(R.color.purple_200));
            current_button=view.getId();
            Toast.makeText(this, "加載中", Toast.LENGTH_SHORT).show();

            if (player.getMediaItemCount() != 0) {
                player.clearMediaItems();
            }

            String str = playlist.get(view.getId()).getUrl();
            String str_Split[] = str.split("/");
            tid = "";
            vid = "";
            video_id = "";
            if (str_Split.length == 3) {
                tid = str_Split[1];
                vid = str_Split[2];
            } else {
                video_id = str_Split[0];
            }
            Connection(tid, vid, video_id);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void Connection(String tid,String vid,String video_id){
        Python python=Python.getInstance();


        PyObject pyObject=python.getModule("Connect_server");
        PyObject py=pyObject.callAttr("Get_video",tid,vid,video_id);

        String str=py.toJava(String.class);
        char temp[]=new char[str.length()];
        if(!str.isEmpty()){
            temp=str.toCharArray();
            temp[11]='1';
            temp[12]='6';
            str="";
            for(int i=0;i<temp.length;i++){
                str+=temp[i];
            }
        }
        else{
            Toast.makeText(this,"獲取資源失敗",Toast.LENGTH_SHORT).show();
        }
        Log.i("video",str);
        m3u8_url=str;
        MediaItem mediaItem=MediaItem.fromUri(str);

        player.setMediaItem(mediaItem);

        player.prepare();

        player.play();


    }
    private void threadRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String[] cmd = new String[]{
                                "-i", m3u8_url,
                                "-c", "copy",
                                getFilesDir()+"/"+vid+tid+video_id+".mp4"
                        };

                        int executionId = FFmpeg.execute(cmd);
                        if(executionId== Config.RETURN_CODE_SUCCESS){
                            db.update_status(title, playlist.get(current_button).getEp(),"成功");
                        }
                        else{
                            db.update_status(title, playlist.get(current_button).getEp(),"失敗");
                        }
            }
        }).start();
    }


}