package com.an.biometric;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;

public class BiometricManager extends BiometricManagerV23 {


    protected BiometricManager(final BiometricBuilder biometricBuilder) {
        this.context = biometricBuilder.context;
        this.title = biometricBuilder.title;
        this.subtitle = biometricBuilder.subtitle;
        this.description = biometricBuilder.description;
        this.negativeButtonText = biometricBuilder.negativeButtonText;
        this.icon = biometricBuilder.icon;
    }


    public void authenticate(@NonNull final BiometricCallback biometricCallback) {

        if (title == null) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_INTERNAl_ERROR, null, "Biometric Dialog title cannot be null");
            return;
        }

        if (subtitle == null) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_INTERNAl_ERROR, null, "Biometric Dialog subtitle cannot be null");
            return;
        }

        if (description == null) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_INTERNAl_ERROR, null, "Biometric Dialog description cannot be null");
            return;
        }

        if (negativeButtonText == null) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_INTERNAl_ERROR, null, "Biometric Dialog negative button text cannot be null");
            return;
        }
        if (icon == null) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_INTERNAl_ERROR, null, "Biometric Dialog icon cannot be null");
            return;
        }

        if (!BiometricUtils.isSdkVersionSupported()) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_SDK_NOT_SUPPORTED, null, null);
            return;
        }

        if (!BiometricUtils.isPermissionGranted(context)) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_NOT_PERMISSION_NOT_GRANTED, null, null);
            return;
        }

        if (!BiometricUtils.isHardwareSupported(context)) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_NOT_SUPPORTED, null, null);
            return;
        }

        if (!BiometricUtils.isFingerprintAvailable(context)) {
            biometricCallback.onAuthenticationError(BiometricError.BIOMETRIC_NOT_AVAILABLE, null, null);
            return;
        }

        displayBiometricDialog(biometricCallback);
    }


    private void displayBiometricDialog(BiometricCallback biometricCallback) {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            displayBiometricPrompt(biometricCallback);
        } else {
            displayBiometricPromptV23(biometricCallback);
        }
    }


    @TargetApi(Build.VERSION_CODES.P)
    private void displayBiometricPrompt(final BiometricCallback biometricCallback) {
        new BiometricPrompt.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButton(negativeButtonText, context.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        biometricCallback.onAuthenticationError(BiometricError.AUTHENTICATION_CANCELLED, null, null);
                    }
                })
                .build()
                .authenticate(new CancellationSignal(), context.getMainExecutor(),
                        new BiometricCallbackV28(biometricCallback));
    }


    public static class BiometricBuilder {

        private String title;
        private String subtitle;
        private String description;
        private String negativeButtonText;
        private Integer icon;

        private Context context;

        public BiometricBuilder(Context context) {
            this.context = context;
        }

        public BiometricBuilder setTitle(@NonNull final String title) {
            this.title = title;
            return this;
        }

        public BiometricBuilder setSubtitle(@NonNull final String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BiometricBuilder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }


        public BiometricBuilder setNegativeButtonText(@NonNull final String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public BiometricBuilder setIcon(@NonNull final Integer icon) {
            this.icon = icon;
            return this;
        }

        public BiometricManager build() {
            return new BiometricManager(this);
        }
    }
}
