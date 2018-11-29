package layw.layw;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Type;

public class TypeOfActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Button buttonRunning = findViewById(R.id.buttonRunning);
        buttonRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TypeOfActivity.this, GetHeartRate.class));
            }
        });

        Button buttonBicycle = findViewById(R.id.buttonBicycle);
        buttonBicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TypeOfActivity.this, GetHeartRate.class));
            }
        });

        Button buttonWalking = findViewById(R.id.buttonWalking);
        buttonWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TypeOfActivity.this, GetHeartRate.class));
            }
        });

        /*
        Button buttonRunning = findViewById(R.id.buttonRunning);
        buttonRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TypeOfActivity.this, Running.class));
            }
        });

        Button buttonBicycle = findViewById(R.id.buttonBicycle);
        buttonBicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TypeOfActivity.this, Bicycle.class));
            }
        });

        Button buttonWalking = findViewById(R.id.buttonWalking);
        buttonWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TypeOfActivity.this, Walk.class));
            }
        });*/
    }
}
