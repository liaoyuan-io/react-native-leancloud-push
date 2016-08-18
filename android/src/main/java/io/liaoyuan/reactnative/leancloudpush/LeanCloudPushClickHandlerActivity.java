package io.liaoyuan.reactnative.leancloudpush;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tong on 2016/8/17.
 */
public class LeanCloudPushClickHandlerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processPush();
        finish();
        if (!LeanCloudPush.isActive()) {
            relaunchActivity();
        }
    }

    private void processPush() {
        try {
            Intent intent = getIntent();
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
            String data = intent.getExtras().getString("com.avos.avoscloud.Data");
            Map<String, String> map = new HashMap<String, String>();
            map.put("action", action);
            map.put("channel", channel);
            map.put("data", data);
            LeanCloudPush.onReceive(map);
            Log.i(LeanCloudPush.MODULE_NAME, "onReceive: action = " + action + ", channel = " + channel + ", data = " + data);
        } catch (Exception e) {
            LeanCloudPush.onError(e);
            Log.e(LeanCloudPush.MODULE_NAME, "onError");
        }
    }

    private void relaunchActivity() {
        Log.w(LeanCloudPush.MODULE_NAME, "Not active. Will relaunch MainActivity...");
        PackageManager pm = getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
        startActivity(launchIntent);
    }
}
