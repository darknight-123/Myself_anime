package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Querry_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Querry_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Querry_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Querry_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    private static final String ITEM_TITLE = "Item title";
    private static final String ITEM_ICON = "Item icon";
    private EditText edit;
    private Button refresh_btn,query_btn;
    private  String string="";
    private ListView lsv_main;
    private ListAdapter mListAdapter;
    ArrayList<Information> info=new ArrayList<Information>();

    private OkHttpClient okHttpClient =new OkHttpClient().newBuilder().build();

    public static Querry_Fragment newInstance(String param1, String param2) {
        Querry_Fragment fragment = new Querry_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        Get_airing();
        lsv_main = (ListView)getActivity().findViewById(R.id.lsv_main);
        lsv_main.setOnItemClickListener(this);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set_data();
        refresh_btn=getActivity().findViewById(R.id.refresh);
        edit=getActivity().findViewById(R.id.query_name);

        query_btn=getActivity().findViewById(R.id.query);
        refresh_btn.setOnClickListener(this);
        query_btn.setOnClickListener(this);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_querry_, container, false);

        return inflater.inflate(R.layout.fragment_querry_, container, false);
    }

    public void Set_data(){
        try {
            List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < info.size(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put(ITEM_TITLE, info.get(i).getTitle());
                item.put(ITEM_ICON, info.get(i).getImage());
                item.put("Item ep", info.get(i).getEp());
                item.put("Item watch", info.get(i).getWatch());
                itemlist.add(item);
            }
            // ListView 中所需之資料參數可透過修改 Adapter 的建構子傳入
            mListAdapter = new ListAdapter(getActivity(), itemlist);

            mListAdapter.notifyDataSetChanged();
            //設定 ListView 的 Adapter
            lsv_main.setAdapter(mListAdapter);
        }
        catch (Exception e){

            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void Get_airing(){
        try {
            Request request =new Request.Builder()
                    .url("https://myself-bbs.jacob.workers.dev/list/airing")//連結的網址
                    .build();
            Call call=okHttpClient.newCall(request);
            //異步執行
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("OkHttp result:", e.getMessage());

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str=response.body().string();


                    try {

                        JSONObject json=new JSONObject(str);
                        JSONObject tt = json.getJSONObject("data");
                        JSONArray tr=tt.getJSONArray("data");
                        info.clear();
                        for(int i=0;i<tr.length();i++){
                            JSONObject object=tr.getJSONObject(i);
                            Information temp=new Information(object.getString("id"),object.getString("title"),object.getString("link"),object.getString("ep"),object.getString("image"),object.getString("watch"));
                            info.add(temp);
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

            });
        }
        catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
    private void Getquerydata(String name){
        try {
            Request request =new Request.Builder()
                    .url("https://myself-bbs.jacob.workers.dev/search/"+name)//連結的網址
                    .build();
            Call call=okHttpClient.newCall(request);
            //異步執行
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("OkHttp result:", e.getMessage());

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str=response.body().string();

                    try {

                        JSONObject json=new JSONObject(str);

                        JSONArray tr=json.getJSONArray("data");
                        info.clear();
                        for(int i=0;i<tr.length();i++){
                            JSONObject object=tr.getJSONObject(i);
                            Information temp=new Information(object.getString("id"),object.getString("title"),object.getString("link"),object.getString("ep"),object.getString("image"),object.getString("watch"));
                            info.add(temp);
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

            });
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }
    private void Getdata(String id){
        string="";
        try {
            Request request =new Request.Builder()
                    .url("https://myself-bbs.jacob.workers.dev/anime/"+id)//連結的網址
                    .build();
            Call call=okHttpClient.newCall(request);
            //異步執行
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("OkHttp result:", e.getMessage());

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    string=response.body().string();
                    //Log.d("OkHttp response:", str);
                    //Log.i("Information",str);
                }

            });
        }
        catch (Exception e){

            e.printStackTrace();
        }

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                Get_airing();
                Toast.makeText(getActivity(),"刷新中,請等待",Toast.LENGTH_SHORT).show();
                try {

                    Thread.sleep(2000);
                    Set_data();
                } catch (InterruptedException e) {
                    Toast.makeText(getActivity(),"搜尋失敗",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.query:
                if(edit.length()==0)
                {
                    Toast.makeText(getActivity(),"請輸入搜尋內容",Toast.LENGTH_SHORT).show();
                }
                else{
                    Getquerydata(edit.getText().toString());
                    Toast.makeText(getActivity(),"搜尋中,請等待",Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(2000);
                        Set_data();
                        edit.setText("");
                    } catch (InterruptedException e) {
                        Toast.makeText(getActivity(),"搜尋失敗",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
        }
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lsv_main:


                try {
                    Toast.makeText(getActivity(),"搜尋中,請等待",Toast.LENGTH_SHORT).show();
                    Getdata(info.get(position).getId());
                    Thread.sleep(1000);
                    if(string.isEmpty()){
                        Toast.makeText(getActivity(),"獲取失敗,請重按一遍",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Bundle bundle=new Bundle();
                        Intent intent=new Intent();
                        bundle.putString("content",string);
                        bundle.putString("ep",info.get(position).getEp());
                        intent.putExtras(bundle);
                        intent.setClass(getActivity(),Item_detail.class);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}