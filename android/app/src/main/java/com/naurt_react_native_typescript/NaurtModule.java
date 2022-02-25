package com.naurt_react_native_typescript;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.naurt_kotlin_sdk.Naurt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NaurtModule extends ReactContextBaseJavaModule {
    NaurtModule(ReactApplicationContext context) {
        super(context);
    }

    public String[] permissions = new String[] {"ACCESS_FINE_LOCATION", "ACCESS_NETWORK_STATE", "WRITE_EXTERNAL_STORAGE"};

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("NAURT_EVENT_ID", "89324");
        return constants;
    }

    @NonNull
    @Override
    public String getName() {
        return "NaurtModule";
    }

    // ================================= Application Wide Variables ====================================
    protected boolean hadPaused = false;
    protected NaurtCallback naurtCallback = null;
    // ================================= Application Wide Variables ====================================

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
            6
        );
    }

    /** Resume the Naurt Engine with a given context*/
    @ReactMethod
    public void resumeNaurt() {
        if (Naurt.INSTANCE.isInitialised().get()) {
            // If we had previously initialised Naurt, resume the engine
            if (hadPaused) {
                Naurt.INSTANCE.resume(getReactApplicationContext().getApplicationContext());

                if (Naurt.INSTANCE.isRunning().get()) {
                    Naurt.INSTANCE.getNaurtPoint().addOnPropertyChangedCallback(naurtCallback);
                }
                hadPaused = false;
            }
        }
    }

    /** Pause the Naurt Engine */
    @ReactMethod
    public void pauseNaurt() {
        if (Naurt.INSTANCE.isInitialised().get()) {
            Naurt.INSTANCE.pause();
            Naurt.INSTANCE.getNaurtPoint().removeOnPropertyChangedCallback(naurtCallback);
            hadPaused = true;
        }
    }

    /** Start the Naurt engine */
    @ReactMethod
    public void startNaurt(Callback callBack) {
        if (Naurt.INSTANCE.isInitialised().get()) {
            Naurt.INSTANCE.start();
            naurtCallback = new NaurtCallback();
            naurtCallback.jsCallback = callBack;
            Naurt.INSTANCE.getNaurtPoint().addOnPropertyChangedCallback(naurtCallback);
        }
    }

    /** Stop the Naurt Engine */
    @ReactMethod
    public void stopNaurt() {
        if (Naurt.INSTANCE.isInitialised().get()) {
            Naurt.INSTANCE.stop();
            naurtCallback = null;
            Naurt.INSTANCE.getNaurtPoint().removeOnPropertyChangedCallback(naurtCallback);
        }
    }

    /** Check to see if the given context has been granted all permissions in the input array */
    @ReactMethod
    public boolean checkPermissions() {
        // Check each permission, requesting if needed
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(
                    getReactApplicationContext().getApplicationContext(),
                    permission
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        Objects.requireNonNull(getReactApplicationContext().getCurrentActivity()),
                        permissions,
                        2935
                );
            }
        }

        // Check after request
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(
                    getReactApplicationContext().getApplicationContext(),
                    permission
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false;
            }
        }

        return true;
    }
}
