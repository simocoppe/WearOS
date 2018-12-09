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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class GetHeartRate extends WearableActivity implements SensorEventListener {

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
                startMeasure(mHeartRateSensor);
                btStart.setEnabled(false);
                btStop.setEnabled(true);
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                stopMeasure();
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

    protected void startMeasure(Sensor mHeartRateSensor) {
        super.onResume();
        this.mHeartRateSensor = mHeartRateSensor;
        if (mHeartRateSensor != null) {
            mSensorManager.registerListener(mHeartRateSensorListener, mHeartRateSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        doPost();
    }

    private void doPost() {
        final OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE = MediaType.parse("application/json");

        JSONObject postData = new JSONObject();
        try {
            postData.put("Heartbeat", 80);
            postData.put("Heartbeat", 82);
            postData.put("Heartbeat", 85);
            postData.put("Heartbeat", 100);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postData.toString());

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://layw-server.herokuapp.com/api/v1.0/users/1/heartbeats-real-time")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //String mMessage = e.getMessage();
                //Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                /*String mMessage = response.body().string();
                if (response.isSuccessful()){
                    try {
                        JSONObject json = new JSONObject(mMessage);
                        final String serverResponse = json.getString("successful Response");
                        Log.w(serverResponse, mMessage);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }*/
            }
        });
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
    }

}
