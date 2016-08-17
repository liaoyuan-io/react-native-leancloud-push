package io.liaoyuan.reactnative.leancloudpush;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tshen on 16/8/16.
 */
public class LeanCloudPush extends ReactContextBaseJavaModule {

    private static LeanCloudPush singleton;
    private static String ON_RECEIVE_EVENT_NAME = "io.liaoyuan.reactnative.leancloudpush.onReceive";
    private static String ON_ERROR_EVENT_NAME = "io.liaoyuan.reactnative.leancloudpush.onError";

    public LeanCloudPush(ReactApplicationContext reactContext) {
        super(reactContext);
        singleton = this;
        PushService.setDefaultPushCallback(getReactApplicationContext(), LeanCloudPushClickHandlerActivity.class);
    }

    protected static void onError(Exception e) {
        if (singleton != null) {
            WritableMap error = Arguments.createMap();
            error.putString("message", e.getLocalizedMessage());
            singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class).emit(ON_ERROR_EVENT_NAME, error);
        }
    }

    protected static void onReceive(String action, String channel, String data) {
        if (singleton != null) {
            WritableMap pushNotification = Arguments.createMap();
            pushNotification.putString("action", action);
            pushNotification.putString("channel", channel);
            pushNotification.putString("data", data);
            Log.d("LeanCloudPush", "Sending to DeviceEventEmitter");
            singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class).emit(ON_RECEIVE_EVENT_NAME, pushNotification);
        }
    }

    @Override
    public String getName() {
        return "LeanCloudPush";
    }

    @ReactMethod
    public void getInstallationId(final Promise promise) {
        try {
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

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("ON_RECEIVE", ON_RECEIVE_EVENT_NAME);
        constants.put("ON_ERROR", ON_ERROR_EVENT_NAME);
        return constants;
    }
}
