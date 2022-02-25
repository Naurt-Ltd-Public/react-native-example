package com.naurt_react_native_typescript;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.naurt_kotlin_sdk.Naurt;

import java.util.HashMap;
import java.util.Map;

public class NaurtAndroidModule extends ReactContextBaseJavaModule {
    NaurtAndroidModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("NAURT_EVENT_ID", "89324");
        return constants;
    }

    @NonNull
    @Override
    public String getName() {
        return "NaurtAndroidModule";
    }

    // ================================= Application Wide Variables
    // ====================================
    protected boolean hadPaused = false;
    protected NaurtCallback naurtCallback = null;
    // ================================= Application Wide Variables
    // ====================================

    /** Initialise Naurt with a given context */
    @ReactMethod
    // Naurt INSTANCE is not null in here; only null when returning to JS
    // Seems like Observables need to have @React wrappers
    public void initialiseNaurt(String apiKey) {
        Log.i("NaurtAndroidModule", "INITIALISING NAURT: [" + Naurt.INSTANCE.isInitialised().get() + "]");

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

    /** Check if Naurt is initialised */
    @ReactMethod
    public boolean isNaurtInitialised() {
        return Naurt.INSTANCE.isInitialised().get();
    }
}
