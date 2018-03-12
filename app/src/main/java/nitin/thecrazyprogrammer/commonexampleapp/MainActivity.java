package nitin.thecrazyprogrammer.commonexampleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nitin.thecrazyprogrammer.common.NonAvailabilityHolder;

/**
 * Created by Nitin on 10-03-2018.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        NonAvailabilityHolder nonAvailabilityHolder = new NonAvailabilityHolder(this, findViewById(android.R.id.content));
        nonAvailabilityHolder.setButton("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        nonAvailabilityHolder.setVisibility(View.VISIBLE);
    }
}
