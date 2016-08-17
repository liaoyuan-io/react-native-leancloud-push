/**
 * Created by Tong on 2016/8/16.
 */
import { NativeModules, DeviceEventEmitter } from "react-native";

const LeanCloudPushNative = NativeModules.LeanCloudPush;

console.log(LeanCloudPushNative);

const module = {

    getInstallationId: function() {
        return LeanCloudPushNative.getInstallationId().then(function(installationId) {
            console.log("installationId", installationId);
        });
    },

    subscribe: function (fn) {
        DeviceEventEmitter.addListener(LeanCloudPushNative.ON_RECEIVE_EVENT_NAME, function (res) {
            console.log("ON_RECEIVE", res);
        });
    }
};


export default module.exports = module;