package com.example.myapplication;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.ListAdapter;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener/* implements View.OnClickListener, AdapterView.OnItemClickListener */{

    private Fragment Querry_Frahment,Save_Frahment;

    private static final String DataBaseName = "DataBaseIt";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "Anime_location";
    private static SQLiteDatabase db;
    private SqlDataBaseHelper sqlDataBaseHelper;
    private  Button Main_btn,Save_btn;

    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 建立SQLiteOpenHelper物件
        sqlDataBaseHelper = new SqlDataBaseHelper(this);

        db = sqlDataBaseHelper.getWritableDatabase(); // 開啟資料庫



    Main_btn=findViewById(R.id.button3);
    Save_btn=findViewById(R.id.button4);
    Main_btn.setOnClickListener(this);
    Save_btn.setOnClickListener(this);

    Querry_Frahment=new Querry_Fragment();
    Save_Frahment=new SaveFragment();
    getSupportFragmentManager().beginTransaction().add(R.id.fl_container,Querry_Frahment,"Home").commit();
    getSupportFragmentManager().beginTransaction().add(R.id.fl_container,Save_Frahment,"Save").commit();
    getSupportFragmentManager().beginTransaction().hide(Save_Frahment).commit();
    FragmentManager fragmentManager = getSupportFragmentManager();
    transaction = fragmentManager.beginTransaction();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button3:
                getSupportFragmentManager().beginTransaction().hide(Save_Frahment).commit();
                getSupportFragmentManager().beginTransaction().show(Querry_Frahment).commit();




                Save_btn.setBackgroundColor(Color.rgb(0,0,255));
                Main_btn.setBackgroundColor(Color.rgb(138,43,226));
                break;
            case R.id.button4:
                getSupportFragmentManager().beginTransaction().hide(Querry_Frahment).commit();


                getSupportFragmentManager().beginTransaction().remove(Save_Frahment).commit();
                Save_Frahment=new SaveFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fl_container,Save_Frahment,"Save").commit();
                getSupportFragmentManager().beginTransaction().show(Save_Frahment).commit();
                Main_btn.setBackgroundColor(Color.rgb(0,0,255));
                Save_btn.setBackgroundColor(Color.rgb(138,43,226));


                // getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,Save_Frahment,"Save").commit();

                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}