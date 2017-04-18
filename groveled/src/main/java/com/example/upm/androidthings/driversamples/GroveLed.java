package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import mraa.mraa;

public class GroveLed extends Activity {
    private static final String TAG = "GroveLED";

    static {
        try {
            System.loadLibrary("javaupm_grove");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native code library failed to load." + e);
            System.exit(1);
        }
    }

    upm_grove.GroveLed led;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_led);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        led = new upm_grove.GroveLed(gpioIndex);
        ledTask.run();
    }

    Runnable ledTask = new Runnable() {

        @Override
        public void run() {
            //Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                for (int i = 0; i < 10; ++i) {
                    led.on();
                    Thread.sleep(1000);
                    led.off();
                    Thread.sleep(1000);
                }
                led.off();
                led.delete();

            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
