package layw.layw;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;


public class GetHeartRate extends WearableActivity implements SensorEventListener {

    private TextView mTextView;
    private Button btStart;
    private Button btStop;
    private Button btClose;
    private Chronometer chronometer;
    private long lastPause;
    private SensorManager mSensorManager;
    private Sensor mHeartRateSensor;

    private SensorEventListener mHeartRateSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("MY_APP", event.toString());
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("MY_APP", sensor.toString() + " - " + accuracy);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mHeartRateSensor = Objects.requireNonNull(mSensorManager).getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (mSensorManager != null) {
            mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        }

        chronometer = findViewById(R.id.chronometer);
        btStart = findViewById(R.id.buttonStart);
        btStop = findViewById(R.id.buttonStop);
        btClose = findViewById(R.id.buttonCloseActivity);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastPause != 0) {
                    chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                }
                else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
                chronometer.start();
                startMeasure(); //inizio lettura del battito cardiaco
                //deve iniziare l'invio dei dati al server heroku (separato o dentro allo start measure?)
                //se dovessi farlo dentro al metodo, quando chiamo lo stop measure, devo chiudere la connessione con
                //il server
                btStart.setEnabled(false);
                btStop.setEnabled(true);
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                stopMeasure(); //fine lettura del battito cardiaco
                //deve finire l'invio dei dati al server heroku
                chronometer.setBase(SystemClock.elapsedRealtime());
                lastPause = 0;
                btStart.setEnabled(true);
                btStop.setEnabled(false);
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setAmbientEnabled();
    }

    protected void startMeasure() {
        super.onResume();
        if (mHeartRateSensor != null) {
            mSensorManager.registerListener(mHeartRateSensorListener, mHeartRateSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void stopMeasure() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHeartRateSensor != null) {
            mSensorManager.registerListener(mHeartRateSensorListener, mHeartRateSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHeartRateSensor != null) {
            mSensorManager.unregisterListener(mHeartRateSensorListener);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float mHeartRateFloat = event.values[0];

        int mHeartRate = Math.round(mHeartRateFloat);

        mTextView.setText(Integer.toString(mHeartRate));
    }
}
