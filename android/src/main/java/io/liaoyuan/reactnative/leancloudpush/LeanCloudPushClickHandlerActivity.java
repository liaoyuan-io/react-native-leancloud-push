package io.liaoyuan.reactnative.leancloudpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Tong on 2016/8/17.
 */
public class LeanCloudPushClickHandlerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processPush();
        finish();
    }

    private void processPush() {
        try {
            Intent intent = getIntent();
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
            String data = intent.getExtras().getString("com.avos.avoscloud.Data");
            Log.d("LeanCloudPushReceiver", "action = " + action + ", channel = " + channel + ", data = " + data);
            LeanCloudPush.onReceive(action, channel, data);
        } catch(Exception e) {
            LeanCloudPush.onError(e);
        }
    }
}
