package com.example.chaemingyun.deep_nap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private String seatStr;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //블루투스 맥주소 입력
    private static String address = "98:D3:33:80:7E:CD";
    private long pressTime;

    @Bind(R.id.bus_stop_list)
    ListView busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        seatStr=intent.getStringExtra("seat");

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        ListViewAdapter adapter=new ListViewAdapter();
        busList.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"송도유원지","38-088");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"원흥아파트","38-336");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"인하대역","37-091");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.busstop),"인하대정문","37-099");
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
                                sendData(seatStr+String.valueOf(position));
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
    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...In onResume - Attempting client connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting to Remote...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Creating Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth is enabled...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(getBaseContext(),
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }

    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
}
