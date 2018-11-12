package com.an.biometric;

public interface BiometricCallback {

    void onAuthenticationSuccessful();

    void onAuthenticationError(BiometricError error, Integer helpCode, String helpString);

}
