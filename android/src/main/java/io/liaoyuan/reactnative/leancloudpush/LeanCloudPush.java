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

    public static String MODULE_NAME = "LeanCloudPush";

    private static LeanCloudPush singleton;
    private static Map<String, String> backgroundNotificationCache = null;
    private static String ON_RECEIVE = "leancloudPushOnReceive";
    private static String ON_ERROR = "leancloudPushOnError";

    public LeanCloudPush(ReactApplicationContext reactContext) {
        super(reactContext);
        singleton = this;
        PushService.setDefaultPushCallback(getReactApplicationContext(), LeanCloudPushClickHandlerActivity.class);
    }

    protected static void onError(Exception e) {
        if (singleton != null) {
            WritableMap error = Arguments.createMap();
            error.putString("message", e.getLocalizedMessage());
            RCTDeviceEventEmitter emitter = singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class);
            emitter.emit(ON_ERROR, error);
        }
    }

    private static WritableMap getWritableMap(Map<String, String> map) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("action", map.get("action"));
        writableMap.putString("channel", map.get("channel"));
        writableMap.putString("data", map.get("data"));
        return writableMap;
    }

    protected static void onReceive(Map<String, String> map) {
        backgroundNotificationCache = map;
        if (singleton != null) {
            WritableMap pushNotification = getWritableMap(map);
            RCTDeviceEventEmitter emitter = singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class);
            emitter.emit(ON_RECEIVE, pushNotification);
        }
    }

    protected static boolean isActive() {
        return singleton != null;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void getInstallationId(final Promise promise) {
        try {
            AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                        Log.i(MODULE_NAME, "installationId = " + installationId);
                        promise.resolve(installationId);
                    } else {
                        promise.reject(e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(MODULE_NAME, "fail to get installationId");
            promise.reject(e);
        }
    }

    @ReactMethod
    public void getInitialNotification(final Promise promise) {
        if (backgroundNotificationCache != null) {
            Log.i(MODULE_NAME, "initial notification data = " + backgroundNotificationCache.get("data"));
            promise.resolve(getWritableMap(backgroundNotificationCache));
            backgroundNotificationCache = null;
        } else {
            Log.w(MODULE_NAME, "no initial notification");
            promise.resolve(null);
        }
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("ON_RECEIVE", ON_RECEIVE);
        constants.put("ON_ERROR", ON_ERROR);
        return constants;
    }
}
