/**
 * Created by Tong on 2016/8/16.
 */
import {NativeModules, DeviceEventEmitter} from 'react-native';
import Rx from 'rx';
import _ from 'lodash';

const LeanCloudPushNative = NativeModules.LeanCloudPush;

export const observable = new Rx.BehaviorSubject();

export const getInstallationId = function () {
    return LeanCloudPushNative.getInstallationId();
};

export const getInitialNotification = function () {
    return LeanCloudPushNative.getInitialNotification();
}

export const initialize = _.once(function () {
    DeviceEventEmitter.addListener(LeanCloudPushNative.ON_RECEIVE, (res) => {
        observable.onNext(res);
    });
    DeviceEventEmitter.addListener(LeanCloudPushNative.ON_ERROR, (res) => {
        observable.onError(res);
    });
});