package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.OkHttpClient;

public class Item_detail extends AppCompatActivity implements View.OnClickListener {
    private TextView textView,text;
    private String content;
    private Button play_button;
    private ImageView imageView;
    private String title="",category="",date="",author="",website="",description="",total_ep="";
    private ArrayList<Link> ep=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Bundle bundle = getIntent().getExtras();

        content=bundle.getString("content");
        total_ep=bundle.getString("ep");
        text=findViewById(R.id.web);
        textView=findViewById(R.id.textView);
        imageView=findViewById(R.id.imageView2);
        play_button=findViewById(R.id.play);
        play_button.setOnClickListener(this);

        PutData();
    }

    private void PutData(){
        try {
            JSONArray array;
            JSONObject json=new JSONObject(content);
            JSONObject tt = json.getJSONObject("data");
            Glide.with(Item_detail.this).load(tt.getString("image")).into(imageView);
            title=tt.getString("title");
            array=tt.getJSONArray("category");
            for(int i=0;i<array.length();i++){
                if(i!=array.length()-1) {
                    category += array.getString(i) + ",";
                }
                else{
                    category += array.getString(i);
                }
            }
            array=tt.getJSONArray("premiere");
            for(int i=0;i<array.length();i++){
                if(i!=array.length()-1) {
                    date += array.getString(i) + ",";
                }
                else{
                    date += array.getString(i);
                }
            }
            author=tt.getString("author");
            website=tt.getString("website");
            description=tt.getString("description");
            Spanned sp=Html.fromHtml("<a href="+'"'+website+'"'+">"+"官方網站"+"</a>");
            text.setText(sp);
            JSONObject jsonObject=tt.getJSONObject("episodes");
            Iterator<?> keys = jsonObject.keys();

            textView.setText("\n番名: "+title+"\n\n目前集數:  "+total_ep+"\n\n類型: "+category+"\n\n作者: "+author+"\n\n開播日期: "+date+"\n\n描述: "+description);
            text.setMovementMethod(LinkMovementMethod.getInstance());


            while( keys.hasNext() ) {
                String key = (String) keys.next();
                ep.add(new Link(key,jsonObject.getString(key)));

            }




        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                Bundle bundle=new Bundle();
                Intent intent=new Intent();
                bundle.putString("title",title);
                bundle.putSerializable("myarraylist",ep);
                intent.putExtras(bundle);
                intent.setClass(Item_detail.this,Play.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}