package com.naurt_react_native_typescript;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.naurt_kotlin_sdk.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NaurtAndroidModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    protected boolean hadPaused = false;

    public static final String[] NAURT_EVENT_IDS = new String[] {
        "NAURT_IS_INITIALISED",
        "NAURT_IS_VALIDATED",
        "NAURT_IS_RUNNING",
        "NAURT_NEW_POINT",
    };

    private class IsInitialisedCallback extends Observable.OnPropertyChangedCallback {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            WritableMap params = Arguments.createMap();
            params.putBoolean("isInitialised", Naurt.INSTANCE.isInitialised().get());
            sendEvent(getReactApplicationContext(), NAURT_EVENT_IDS[0], params);
        }
    }
    private final IsInitialisedCallback isInitialisedCB = new IsInitialisedCallback();

    private class IsValidatedCallback extends Observable.OnPropertyChangedCallback {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            WritableMap params = Arguments.createMap();
            params.putBoolean("isValidated", Naurt.INSTANCE.isValidated().get());
            sendEvent(getReactApplicationContext(), NAURT_EVENT_IDS[1], params);
        }
    }
    private final IsValidatedCallback isValidatedCB = new IsValidatedCallback();

    private class IsRunningCallback extends Observable.OnPropertyChangedCallback {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            WritableMap params = Arguments.createMap();
            params.putBoolean("isRunning", Naurt.INSTANCE.isRunning().get());
            sendEvent(getReactApplicationContext(), NAURT_EVENT_IDS[2], params);
        }
    }
    private final IsRunningCallback isRunningCB = new IsRunningCallback();

    private class NaurtPointCallback extends Observable.OnPropertyChangedCallback {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (Naurt.INSTANCE.getNaurtPoint().get() != null) {
                WritableMap params = Arguments.createMap();
                params.putDouble("latitude", Objects.requireNonNull(Naurt.INSTANCE.getNaurtPoint().get()).getLatitude());
                params.putDouble("longitude", Objects.requireNonNull(Naurt.INSTANCE.getNaurtPoint().get()).getLongitude());
                params.putString("timestamp", Long.toString(Objects.requireNonNull(Naurt.INSTANCE.getNaurtPoint().get()).getTimestamp()));
                sendEvent(getReactApplicationContext(), NAURT_EVENT_IDS[3], params);
            }
        }
    }
    private final NaurtPointCallback naurtPointCB = new NaurtPointCallback();

    private void addCallbacks() {
        // Set up observables with react events
        Naurt.INSTANCE.isInitialised().addOnPropertyChangedCallback(isInitialisedCB);
        Naurt.INSTANCE.isValidated().addOnPropertyChangedCallback(isValidatedCB);
        Naurt.INSTANCE.isRunning().addOnPropertyChangedCallback(isRunningCB);
        Naurt.INSTANCE.getNaurtPoint().addOnPropertyChangedCallback(naurtPointCB);
    }

    private void removeCallbacks() {
        Naurt.INSTANCE.isInitialised().removeOnPropertyChangedCallback(isInitialisedCB);
        Naurt.INSTANCE.isValidated().removeOnPropertyChangedCallback(isValidatedCB);
        Naurt.INSTANCE.isRunning().removeOnPropertyChangedCallback(isRunningCB);
        Naurt.INSTANCE.getNaurtPoint().removeOnPropertyChangedCallback(naurtPointCB);
    }

    NaurtAndroidModule(ReactApplicationContext context) {
        super(context);

        // Setup lifecycle events handled by android
        getReactApplicationContext().addLifecycleEventListener(this);

        addCallbacks();
    }

    @Override
    public void onHostResume() {
        resumeNaurt();
    }

    @Override
    public void onHostPause() {
        pauseNaurt();
    }

    @Override
    public void onHostDestroy() {
        Naurt.INSTANCE.stop();
        removeCallbacks();
    }

    private void sendEvent(
            ReactContext reactContext,
            String eventName,
            @Nullable WritableMap params
    ) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

    @ReactMethod
    public void addListener(String eventName) {
        // Set up any upstream listeners or background tasks as necessary
    }

    @ReactMethod
    public void removeListeners(Integer count) {
        // Remove upstream listeners, stop unnecessary background tasks
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        constants.put("NAURT_EVENT_IDS", NAURT_EVENT_IDS);
        return constants;
    }

    @NonNull
    @Override
    public String getName() {
        return "NaurtAndroidModule";
    }

    /** Initialise Naurt with a given context */
    @ReactMethod
    public void initialiseNaurt(String apiKey) {
        // Guarded return, to prevent duplicate Initialisations
        if (Naurt.INSTANCE.isInitialised().get()) {
            return;
        }

        Naurt.INSTANCE.initialise(
            apiKey,
            getReactApplicationContext().getApplicationContext(),
            6);
    }

    /** Resume the Naurt Engine with a given context */
    @ReactMethod
    public void resumeNaurt() {
        if (Naurt.INSTANCE.isInitialised().get()) {
            // If we had previously initialised Naurt, resume the engine
            if (hadPaused) {
                Naurt.INSTANCE.resume(getReactApplicationContext().getApplicationContext());
                hadPaused = false;
            }

            addCallbacks();
        }
    }

    /** Pause the Naurt Engine */
    @ReactMethod
    public void pauseNaurt() {
        if (Naurt.INSTANCE.isInitialised().get()) {
            Naurt.INSTANCE.pause();
            hadPaused = true;

            removeCallbacks();
        }
    }

    /** Start the Naurt engine */
    @ReactMethod
    public void startNaurt() {
        if (Naurt.INSTANCE.isInitialised().get()) {
            Naurt.INSTANCE.start();
        }
    }

    /** Stop the Naurt Engine */
    @ReactMethod
    public void stopNaurt() {
        if (Naurt.INSTANCE.isInitialised().get()) {
            Naurt.INSTANCE.stop();
        }
    }
}
