package io.liaoyuan.reactnative.leancloudpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by Tong on 2016/8/16.
 */
public class LeanCloudPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
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
