package com.naurt_react_native_typescript;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class NaurtModule extends ReactContextBaseJavaModule {
    NaurtModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "NaurtModule";
    }

    
}
