package com.example.chaemingyun.deep_nap;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private long pressTime;

    @Bind(R.id.bus_stop_list)
    ListView busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        ListViewAdapter adapter=new ListViewAdapter();
        busList.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"송도유원지","38-088");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"원흥아파트","38-336");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"인하대역","37-091");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"장미아파트","37-092");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"학익시장","37-080");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"법원,검찰청","37-074");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"신동아1,2차아파트","37-507");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"신동아3차아파트","37-494");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"선바위역","21-023");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"서초아트자이아파트","22-139");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"서초역","22-136");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"지하철2호선교대역2","22-103");

        busList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item=(ListViewItem)parent.getItemAtPosition(position);

                Drawable busImage=item.getIcon();
                String busTitle=item.getTitle();
                String busNum=item.getDesc();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("확인")        // 제목 설정
                        .setIcon(R.drawable.confirm)
                        .setMessage(busTitle+"을(를) 목적지로 설정하시겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                Toast.makeText(MainActivity.this,"목적지 설정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(pressTime==0){
            Toast.makeText(MainActivity.this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
            pressTime=System.currentTimeMillis();
        }else{
            int seconds=(int)(System.currentTimeMillis()-pressTime);
            if(seconds>2000){
                pressTime=0;
            }else{
                finish();
            }
        }
    }
}
