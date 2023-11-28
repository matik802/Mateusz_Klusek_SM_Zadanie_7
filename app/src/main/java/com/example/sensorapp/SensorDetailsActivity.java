package com.example.sensorapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private TextView sensorLightTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorLightTextView = findViewById(R.id.sensor_Light_label);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        String name = getIntent().getStringExtra(SensorActivity.KEY_EXTRA_SENSOR_NAME);
        for (Sensor sensor:sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            if (name.equals(sensor.getName())) {
                sensorLight = sensor;
                sensorLightTextView.setText(sensor.getName());
            }
        }
        if (!(name.equals(sensorManager.getSensorList(Sensor.TYPE_ALL).get(1).getName())
                || name.equals(sensorManager.getSensorList(Sensor.TYPE_ALL).get(5).getName())
                || name.equals(sensorManager.getSensorList(Sensor.TYPE_ALL).get(2).getName()))) {
            if (sensorLight == null) {
                sensorLightTextView.setText(R.string.missing_sensor);
            } else {
                sensorLight = null;
                sensorLightTextView.setText(R.string.wrong_sensor);
            }
        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#019361"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sensorLight != null)
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                sensorLightTextView.setText(getResources().getString(R.string.light_sensor_label,currentValue));
                break;
            case Sensor.TYPE_ACCELEROMETER  :
                sensorLightTextView.setText(getResources().getString(R.string.accelerometer_sensor_label,event.values[0],event.values[1],event.values[2]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("onAccuracyChanged!", "test onAccuracyChanged method");
    }
}