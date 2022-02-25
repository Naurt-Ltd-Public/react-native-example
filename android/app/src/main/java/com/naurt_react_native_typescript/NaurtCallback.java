package com.naurt_react_native_typescript;

import androidx.databinding.Observable;

import com.naurt_kotlin_sdk.Naurt;
import com.naurt_kotlin_sdk.NaurtLocation;
import com.facebook.react.bridge.Callback;

public class NaurtCallback extends Observable.OnPropertyChangedCallback {
    public Callback jsCallback;

    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
        NaurtLocation location = Naurt.INSTANCE.getNaurtPoint().get();
        if (location != null) {
            Integer naurtEventId = 89324;
            jsCallback.invoke(naurtEventId, location.getLatitude(), location.getLongitude(), location.getTimestamp());
        }
    }
}
