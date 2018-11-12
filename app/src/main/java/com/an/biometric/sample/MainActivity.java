package com.an.biometric.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricError;
import com.an.biometric.BiometricManager;


public class MainActivity extends AppCompatActivity implements BiometricCallback {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn_authenticate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new BiometricManager.BiometricBuilder(MainActivity.this)
                        .setTitle(getString(R.string.biometric_title))
                        .setSubtitle(getString(R.string.biometric_subtitle))
                        .setDescription(getString(R.string.biometric_description))
                        .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                        .setIcon(R.mipmap.ic_launcher)
                        .build()
                        .authenticate(MainActivity.this);
            }
        });
    }


    @Override
    public void onAuthenticationSuccessful() {

    }

    @Override
    public void onAuthenticationError(BiometricError error, Integer helpCode, String helpString) {
        String errorDescriptor = "Error: " + error.name();
        if (helpCode != null) {
            errorDescriptor = " HelpCode: " + helpCode;
        }
        if (helpString != null) {
            errorDescriptor = " HelpString: " + helpString;
        }
        Toast.makeText(this, errorDescriptor, Toast.LENGTH_SHORT).show();
    }
}
