package com.example.chaemingyun.deep_nap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


/**
 * Created by admin on 2017-01-16.
 */

public class QrActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "ALL");
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0) {

            if(resultCode == Activity.RESULT_OK)
            {
                //모든 qr코드가 인식됨 인식후 MainActivity 이동
                Intent i=new Intent(QrActivity.this,MainActivity.class);
                startActivity(i);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
