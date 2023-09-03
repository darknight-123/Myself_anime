package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.github.rongi.rotate_layout.layout.RotateLayout;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

public class Play_save extends AppCompatActivity implements View.OnClickListener {
    private String path;
    private Button download_btn;
    private SimpleExoPlayer player;
    private ArrayList<Link> playlist;
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
        setContentView(R.layout.activity_play_save);
        Bundle bundle = getIntent().getExtras();
        path=bundle.getString("path");
        playerview=findViewById(R.id.playerview_save);
        fullScreen=playerview.findViewById(R.id.exo_fullscreen_icon);
        scroll=findViewById(R.id.play_scrollvew_save);
        fullScreen.setOnClickListener(this);
        rotateLayout=findViewById(R.id.rotateview_save);
        player=new SimpleExoPlayer.Builder(this).build();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
        connection(path);
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
    public void onClick(View v) {
        if(v.getId()==R.id.exo_fullscreen_icon){
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

                View decorView = getWindow().getDecorView();
                int flags = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(flags);
                isFullScreen=false;

            }
            else{
                scroll.setVisibility(View.GONE);

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
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    public void connection(String url){
        MediaItem mediaItem=MediaItem.fromUri(url);

        player.setMediaItem(mediaItem);

        player.prepare();

        player.play();
    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        player.stop();
        // 在這裡執行當用戶按下 Home 鍵時的處理
        // 例如，暫停播放器、保存狀態等
    }
}