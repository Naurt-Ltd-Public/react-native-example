package com.naurtreactnativeexample

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.databinding.ObservableField
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.naurt_kotlin_sdk.*


class NaurtActivity(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "NaurtSDK"
    }

    // ================================= Application Wide Variables ====================================
    private var hasInitialisedNaurt = false
    private var hadPaused = false
    private var naurtCallback: NaurtCallback? = null
    private var applicationContext: Context = reactContext.applicationContext;
    private lateinit var key: String
    private var precision1: Int = 0
    // ================================= Application Wide Variables ====================================


    private var initialised = false
    private val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    @ReactMethod
    fun initialiseNaurt(apiKey: String, precision:Int) {
        key = apiKey
        precision1 = precision
        // Guarded return, to prevent duplicate Initialisations
        if (hasInitialisedNaurt) {
            return
        }

        Naurt.initialise(
                apiKey = apiKey,
                context = applicationContext,
                precision = precision
        )
        naurtCallback = createNaurtCallback()

        hasInitialisedNaurt = true
    }

    @ReactMethod
    fun start(){
        Thread.sleep(10_000)
        println("IT CALLED THE START FUNCTION")
        Naurt.start()
    }

    @ReactMethod
    fun check(){
        println("------------------------------")
        println(Naurt.naurtPoints)
        println("------------------------------")
    }

    /** Create a Naurt callback, used for observing changes in the Naurt Point */
    internal fun createNaurtCallback(): NaurtCallback {
        println("createNaurtCallback")
        return { it: ObservableField<NaurtLocation> ->
            val location = it.get()
            if (location != null) {
                println("New Naurt Point! [${location.latitude}, ${location.longitude}] at time: ${location.timestamp}")
                sendEvent(reactApplicationContext, location)
            }
        }
    }

    /** Resume the Naurt Engine with a given context*/
    @ReactMethod
    fun resumeNaurt(context: Context, savedInstanceState: Bundle?) {
        // If we are resuming from a Bundled state, check for initialisation
        savedInstanceState?.let {
            hasInitialisedNaurt = savedInstanceState.getBoolean("initialised")
        }

        // If we had previously initialised Naurt, resume the engine
        if (hasInitialisedNaurt && hadPaused) {
            Naurt.resume(context)

            if (Naurt.isRunning.get()) {
                naurtCallback = createNaurtCallback()
                Naurt.naurtPoint.addOnPropertyChanged(naurtCallback!!)
            }

            hadPaused = false
        }
    }

    /** Pause the Naurt Engine */
    @ReactMethod
    fun pauseNaurt(outState: Bundle) {
        if (hasInitialisedNaurt) {
            Naurt.pause()
            Naurt.naurtPoint.removeOnPropertyChanged(naurtCallback!!)
            hadPaused = true
            outState.putBoolean("hasInitialisedNaurt", hasInitialisedNaurt)
        }
    }

    private fun sendEvent(reactContext: ReactApplicationContext, message: NaurtLocation){
        reactContext
                .getJSModule(RCTDeviceEventEmitter::class.java)
                .emit("location", message)
    }



}