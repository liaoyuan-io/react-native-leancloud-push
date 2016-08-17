package io.liaoyuan.reactnative.leancloudpush;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.SaveCallback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tshen on 16/8/16.
 */
public class LeanCloudPush extends ReactContextBaseJavaModule {

    private static LeanCloudPush singleton;

    public LeanCloudPush(ReactApplicationContext reactContext) {
        super(reactContext);
        singleton = this;
    }

    protected static void onError(Exception e) {

    }

    protected static void onReceive(String action, String channel, String data) {
        if (singleton != null) {
            WritableMap pushNotification = Arguments.createMap();
            pushNotification.putString("action", action);
            pushNotification.putString("channel", channel);
            pushNotification.putString("data", data);
            singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class).emit("io.liaoyuan.push.LeanCloud.onReceive", pushNotification);
        }
    }

    @Override
    public String getName() {
        return "LeanCloudPush";
    }

    @ReactMethod
    public void initialize(ReadableMap options, final Promise promise) {
        try {
            String appId = options.getString("appId");
            String clientKey = options.getString("clientKey");
            AVOSCloud.initialize(getCurrentActivity(), appId, clientKey);
            AVOSCloud.useAVCloudUS();
            AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                        promise.resolve(installationId);
                    } else {
                        promise.reject(e);
                    }
                }
            });
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void getInstallationId(final Promise promise) {
        try {
            String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
            promise.resolve(installationId);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

}
