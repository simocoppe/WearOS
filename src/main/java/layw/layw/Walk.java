package layw.layw;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class Walk extends WearableActivity {

    private Button btStart;
    private Button btStop;
    private Button btClose;
    private Chronometer chronometer;

    private long lastPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

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
                btStart.setEnabled(false);
                btStop.setEnabled(true);
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
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
    }
}
