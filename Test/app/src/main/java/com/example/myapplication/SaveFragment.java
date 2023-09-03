package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Save_Information>save_info;
    private String mParam1;
    private String mParam2;
    private TextView detail;
    private ListView lsv_save;
    private SaveAdapter mListAdapter;
    private Timer timer;
    private String[] items = {"播放", "刪除"};
    private int selectedOption = -1; // 初始化選擇為無
    private SqlDataBaseHelper db;
    public SaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveFragment newInstance(String param1, String param2) {
        SaveFragment fragment = new SaveFragment();
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
        lsv_save = (ListView)getActivity().findViewById(R.id.lsv_save);
        lsv_save.setOnItemClickListener(this);
        db = new SqlDataBaseHelper(getView().getContext());
        setdata();

    }


    public void setdata(){
        SqlDataBaseHelper db=new SqlDataBaseHelper(getView().getContext());

        save_info=db.All_data();
        try {
            List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < save_info.size(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("Item title", save_info.get(i).getTitle());

                item.put("Item ep", save_info.get(i).getEp());
                item.put("Item status", save_info.get(i).getStatus());
                itemlist.add(item);
            }
            // ListView 中所需之資料參數可透過修改 Adapter 的建構子傳入
            mListAdapter = new SaveAdapter(getActivity(), itemlist);

            mListAdapter.notifyDataSetChanged();
            //設定 ListView 的 Adapter
            lsv_save.setAdapter(mListAdapter);
        }
        catch (Exception e){

            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save, container, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showOptionListDialog(position);
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }


    private void showOptionListDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setTitle("選擇一個功能"); // 對話框標題

        // 設置選項列表
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在選項被選擇時執行操作
                selectedOption = which;
                // 可以根據選擇的選項執行相應的操作
                // 例如，顯示選擇的選項
                switch(selectedOption){
                    case 0:

                        Bundle bundle=new Bundle();
                        Intent intent=new Intent();

                        bundle.putString("path",save_info.get(position).getPath());
                        intent.putExtras(bundle);
                        intent.setClass(getActivity(),Play_save.class);
                        startActivity(intent);
                        break;
                    case 1:
                        File fileToDelete = new File(save_info.get(position).getPath());
                        if (fileToDelete.exists()) {
                            boolean deleted = fileToDelete.delete();
                            if (deleted) {
                                Toast.makeText(getContext(),"成功",Toast.LENGTH_SHORT).show();
                                db.del(save_info.get(position).getTitle(),save_info.get(position).getEp());
                                //Toast.makeText(getContext(),save_info.get(position).getTitle()+save_info.get(position).getEp(),Toast.LENGTH_SHORT).show();
                                setdata();
                            } else {
                                // 檔案刪除失敗
                                Toast.makeText(getContext(),"失敗",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 檔案不存在
                            Toast.makeText(getContext(),"失敗",Toast.LENGTH_SHORT).show();
                        }

                    break;

                }
            }
        });

        // 設置取消按鈕
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在取消按鈕點擊事件中執行操作
                dialog.dismiss(); // 關閉對話框
            }
        });

        // 顯示對話框
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}