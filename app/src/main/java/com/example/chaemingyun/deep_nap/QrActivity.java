package com.example.chaemingyun.deep_nap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by admin on 2017-01-16.
 */

public class QrActivity extends Activity {
    private static final String TAG="QrActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "ALL");
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(requestCode == 0) {

            if(resultCode == Activity.RESULT_OK)
            {
                String contents = data.getStringExtra("SCAN_RESULT");
                String seatStr=null;
                if(contents.equals("http://m.site.naver.com/0jbHr")) seatStr="a";
                else if(contents.equals("http://m.site.naver.com/0jbKa")) seatStr="b";
                else if(contents.equals("http://m.site.naver.com/0jbKc")) seatStr="c";
                Intent i=new Intent(QrActivity.this,MainActivity.class);
                i.putExtra("seat",seatStr);
                startActivity(i);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
