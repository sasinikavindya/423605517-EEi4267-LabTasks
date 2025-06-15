package com.s23010381.mylabtasks;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class task04 extends AppCompatActivity implements SensorEventListener {

    private TextView temperatureTextView;
    private SensorManager sensorManager;
    private Sensor temperature;
    private MediaPlayer mediaPlayer;
    private boolean isRunning = false;
    private static final float TEMPERATURE_THRESHOLD = 81.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task04);

        // Initialize TextView with correct ID
        temperatureTextView = findViewById(R.id.temperatureTextView);

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Initialize MediaPlayer with alert sound from raw folder
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.alert);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading audio file", Toast.LENGTH_SHORT).show();
        }

        // Check for temperature sensor
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        } else {
            temperatureTextView.setText("Temperature sensor not available");
            Toast.makeText(this, "No Temperature Sensor Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float currentTemp = event.values[0];
            temperatureTextView.setText(String.format("Current Temperature: %.1f°C", currentTemp));

            if (currentTemp > TEMPERATURE_THRESHOLD && !isRunning) {
                isRunning = true;
                playAlert();
            } else if (currentTemp <= TEMPERATURE_THRESHOLD) {
                isRunning = false;
            }
        }
    }

    private void playAlert() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.seekTo(0);
                isRunning = false;
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (temperature != null) {
            sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
